package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de likes de reseñas con las rutas y campos de Firestore v9. */
class ReviewLikeRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewLikeRemoteDataSource {
    private val reviews get() = firestore.collection("reviews")
    private val notifications get() = firestore.collection("notifications")
    private val sequence get() = firestore.collection("counters").document("next_id")

    override suspend fun getReviewLikes(reviewId: Int?): List<ReviewLikeInfo> {
        val references = if (reviewId != null) listOf(reviews.document(reviewId.toString()))
            else reviews.get(Source.SERVER).await().documents.map { it.reference }
        return references.flatMap { review -> review.collection("likes").get(Source.SERVER).await().documents.map {
            it.toReviewLikeFirestoreDto().toReviewLikeInfo(review.id.toInt())
        } }
    }

    /** El documento del like usa el ID del usuario, así que basta comprobar si existe en cada reseña. */
    override suspend fun getLikedReviewIds(userId: Int, reviewIds: Set<Int>): Set<Int> {
        require(userId > 0)
        return coroutineScope {
            reviewIds.map { reviewId ->
                async {
                    val like = reviews.document(reviewId.toString()).collection("likes").document(userId.toString())
                    reviewId.takeIf { like.get(Source.SERVER).await().exists() }
                }
            }.awaitAll().filterNotNull().toSet()
        }
    }

    /**
     * Alterna el like dentro de una transacción: el documento en likes, el contador likes_count de la
     * reseña y la notificación del autor se crean o se eliminan juntos, o no se aplica ninguno.
     */
    override suspend fun sendReviewLike(reviewId: Int, userId: Int): ReviewLikeStatus {
        require(reviewId > 0 && userId > 0)
        val reviewRef = reviews.document(reviewId.toString())
        val likeRef = reviewRef.collection("likes").document(userId.toString())
        // El SDK cliente no admite consultas dentro de una transacción; las notificaciones previas
        // de este mismo like se localizan antes para eliminarlas junto con el documento del like.
        val previousNotifications = notifications.whereEqualTo("review_id", reviewId).get(Source.SERVER).await()
            .documents.filter {
                it.getString("type") == NotificationTypes.REVIEW_LIKE && it.getLong("actor_user_id") == userId.toLong()
            }.map { it.reference }
        val firstNotificationId = notificationIdSeed()
        return firestore.runTransaction { transaction ->
            // Firestore exige completar todas las lecturas de la transacción antes de la primera escritura.
            val review = transaction.get(reviewRef)
            check(review.exists()) { "No existe la reseña." }
            val authorId = review.integer("user_id")
            val likes = review.integer("likes_count")
            val wasLiked = transaction.get(likeRef).exists()
            // El autor no recibe notificaciones por los likes que da a sus propias reseñas.
            val notificationId = if (!wasLiked && authorId != userId) {
                var next = transaction.get(sequence).getLong("next_notification_id")?.toInt() ?: firstNotificationId
                check(next > 0)
                while (transaction.get(notifications.document(next.toString())).exists()) {
                    check(next < Int.MAX_VALUE) { "Se agotaron los IDs de notificación." }
                    next++
                }
                next
            } else null

            previousNotifications.forEach { transaction.delete(it) }
            if (wasLiked) {
                transaction.delete(likeRef)
                transaction.update(reviewRef, "likes_count", FieldValue.increment(-1))
                ReviewLikeStatus(isLiked = false, likes = (likes - 1).coerceAtLeast(0))
            } else {
                transaction.set(likeRef, CreateReviewLikeFirestoreDto(userId).fields())
                transaction.update(reviewRef, "likes_count", FieldValue.increment(1))
                notificationId?.let { id ->
                    val notification = CreateNotificationFirestoreDto(user_id = authorId, actor_user_id = userId,
                        type = NotificationTypes.REVIEW_LIKE, review_id = reviewId, comment_id = null)
                    transaction.set(notifications.document(id.toString()), notification.fields(id))
                    // merge conserva las secuencias de usuarios y reseñas en este mismo documento.
                    transaction.set(sequence, mapOf("next_notification_id" to id.toLong() + 1), SetOptions.merge())
                }
                ReviewLikeStatus(isLiked = true, likes = likes + 1)
            }
        }.await()
    }

    /** Primer ID disponible cuando counters/next_id aún no tiene la secuencia de notificaciones. */
    private suspend fun notificationIdSeed(): Int {
        sequence.get(Source.SERVER).await().getLong("next_notification_id")?.let { return it.toInt() }
        // Continúa después del mayor ID existente para no sobrescribir notificaciones previas.
        val last = notifications.orderBy("id", Query.Direction.DESCENDING).limit(1).get(Source.SERVER).await()
            .documents.firstOrNull()
        return (last?.integer("id") ?: 0) + 1
    }
}
