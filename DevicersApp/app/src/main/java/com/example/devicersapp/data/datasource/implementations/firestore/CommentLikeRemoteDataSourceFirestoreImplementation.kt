package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de likes de comentarios con las rutas y campos de Firestore v9. */
class CommentLikeRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : CommentLikeRemoteDataSource {
    /** Modifica juntos el like, el contador y la notificación; nunca notifica un like propio. */
    override suspend fun sendCommentLike(reviewId: Int, commentId: Int, userId: Int): CommentLikeStatus {
        require(reviewId > 0 && commentId > 0 && userId > 0)
        val reviewRef = firestore.collection("reviews").document(reviewId.toString())
        val commentRef = reviewRef.collection("comments").document(commentId.toString())
        val likeRef = commentRef.collection("likes").document(userId.toString())
        val notifications = firestore.collection("notifications")
        val sequence = firestore.collection("counters").document("next_id")
        val previous = notifications.whereEqualTo("comment_id", commentId).get(Source.SERVER).await().documents
            .filter { it.getString("type") == NotificationTypes.COMMENT_LIKE && it.getLong("review_id") == reviewId.toLong() && it.getLong("actor_user_id") == userId.toLong() }
        val seed = sequence.get(Source.SERVER).await().getLong("next_notification_id")?.toInt()
            ?: ((notifications.orderBy("id", Query.Direction.DESCENDING).limit(1).get(Source.SERVER).await()
                .documents.firstOrNull()?.integer("id") ?: 0) + 1)
        return firestore.runTransaction { transaction ->
            val review = transaction.get(reviewRef)
            val comment = transaction.get(commentRef)
            val actor = transaction.get(firestore.collection("users").document(userId.toString()))
            check(review.exists() && review.getBoolean("is_active") == true && comment.exists() && comment.getBoolean("is_active") == true && actor.exists()) { "No existe el comentario o el usuario." }
            val authorId = comment.integer("user_id")
            val existing = transaction.get(likeRef)
            val wasLiked = existing.exists()
            val notificationId = if (!wasLiked && authorId != userId) {
                var next = transaction.get(sequence).getLong("next_notification_id")?.toInt() ?: seed
                check(next > 0)
                while (transaction.get(notifications.document(next.toString())).exists()) {
                    check(next < Int.MAX_VALUE)
                    next++
                }
                next
            } else null
            val likes = ((comment.getLong("likes_count")?.toInt() ?: 0) + if (wasLiked) -1 else 1).coerceAtLeast(0)
            // Las consultas previas cubren datos antiguos sin notification_id; el documento cubre reintentos concurrentes.
            val removedNotifications = previous.map { it.reference }.toMutableSet()
            existing.getLong("notification_id")?.let { removedNotifications.add(notifications.document(it.toString())) }
            removedNotifications.forEach { transaction.delete(it) }
            if (wasLiked) transaction.delete(likeRef)
            else transaction.set(likeRef, CreateCommentLikeFirestoreDto(userId, notificationId).fields())
            transaction.update(commentRef, "likes_count", likes)
            notificationId?.let { id ->
                transaction.set(notifications.document(id.toString()), CreateNotificationFirestoreDto(authorId, userId,
                    NotificationTypes.COMMENT_LIKE, reviewId, commentId).fields(id))
                transaction.set(sequence, mapOf("next_notification_id" to id.toLong() + 1), SetOptions.merge())
            }
            CommentLikeStatus(!wasLiked, likes)
        }.await()
    }

    override suspend fun getCommentLikes(reviewId: Int?, commentId: Int?): List<CommentLikeInfo> {
        val reviews = if (reviewId != null) listOf(firestore.collection("reviews").document(reviewId.toString()))
            else firestore.collection("reviews").get(Source.SERVER).await().documents.map { it.reference }
        return reviews.flatMap { review ->
            val comments = if (commentId != null && reviewId != null)
                listOf(review.collection("comments").document(commentId.toString()))
            else review.collection("comments").get(Source.SERVER).await().documents.filter { commentId == null || it.id == commentId.toString() }.map { it.reference }
            comments.flatMap { comment -> comment.collection("likes").get(Source.SERVER).await().documents.map {
                it.toCommentLikeFirestoreDto().toCommentLikeInfo(review.id.toInt(), comment.id.toInt())
            } }
        }
    }
}
