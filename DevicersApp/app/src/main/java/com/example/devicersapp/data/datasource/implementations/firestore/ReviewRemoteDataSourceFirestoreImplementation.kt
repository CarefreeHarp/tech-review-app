package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/** Implementa las operaciones de reseñas con las rutas y campos de Firestore v9. */
class ReviewRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : ReviewRemoteDataSource {
    private val reviews get() = firestore.collection("reviews")
    override suspend fun getReviews(): List<ReviewInfo> =
        reviews.get(Source.SERVER).await().documents.map { it.toReviewFirestoreDto().toReviewInfo() }
    /** Escucha altas, cambios y bajas, agrupando el filtro IN sin duplicar reseñas entre emisiones. */
    override fun listenReviews(userIds: Set<Int>?): Flow<List<ReviewInfo>> {
        if (userIds == null) return reviews.listenReviewQuery()
        if (userIds.isEmpty()) return flowOf(emptyList())
        val queries = userIds.sorted().chunked(30).map { ids ->
            reviews.whereIn("user_id", ids).listenReviewQuery()
        }
        return combine(queries) { batches -> batches.flatMap { it }.distinctBy { it.id } }
    }

    /** Libera el listener al cancelar el feed y propaga errores de consulta o decodificación. */
    private fun Query.listenReviewQuery(): Flow<List<ReviewInfo>> = callbackFlow {
        var hasServerSnapshot = false
        val listener = addSnapshotListener(MetadataChanges.INCLUDE) { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            if (snapshot != null) {
                // La caché inicial puede estar incompleta: no confirma que el feed esté vacío.
                // INCLUDE entrega la confirmación del servidor aunque los documentos no cambien.
                if (!hasServerSnapshot && snapshot.metadata.isFromCache) return@addSnapshotListener
                if (!snapshot.metadata.isFromCache) hasServerSnapshot = true
                runCatching { snapshot.documents.map { it.toReviewFirestoreDto().toReviewInfo() } }
                    .onSuccess { trySend(it) }
                    .onFailure { close(it) }
            }
        }
        awaitClose { listener.remove() }
    }.distinctUntilChanged()
    override suspend fun getReviewById(reviewId: Int): ReviewInfo {
        require(reviewId > 0)
        return reviews.document(reviewId.toString()).get(Source.SERVER).await().also { snapshot ->
            if (!snapshot.exists()) throw NoSuchElementException("No existe ${snapshot.reference.path}")
        }.toReviewFirestoreDto().toReviewInfo()
    }
    override suspend fun getReviewsByUser(userId: Int): List<ReviewInfo> =
        reviews.whereEqualTo("user_id", userId).get(Source.SERVER).await().documents.map { it.toReviewFirestoreDto().toReviewInfo() }
    /** Divide el filtro IN en grupos de hasta 30 autores sin descargar la colección completa. */
    override suspend fun getReviewsByUsers(userIds: Set<Int>): List<ReviewInfo> =
        userIds.toList().chunked(30).flatMap { ids ->
            reviews.whereIn("user_id", ids).get(Source.SERVER).await().documents
                .map { it.toReviewFirestoreDto().toReviewInfo() }
        }
    override suspend fun getReviewsByProduct(productId: Int): List<ReviewInfo> =
        reviews.whereEqualTo("article_id", productId).get(Source.SERVER).await().documents.map { it.toReviewFirestoreDto().toReviewInfo() }

    override suspend fun createReview(draft: ReviewDraft): ReviewInfo {
        require(draft.userId > 0 && draft.articleId > 0 && draft.rating in 1..5 && draft.body.isNotBlank())
        val sequence = firestore.collection("counters").document("next_id")
        val id = firestore.runTransaction { transaction ->
            check(transaction.get(firestore.collection("users").document(draft.userId.toString())).exists())
            check(transaction.get(firestore.collection("articles").document(draft.articleId.toString())).exists())
            var next = transaction.get(sequence).integer("next_review_id")
            check(next > 0)
            while (transaction.get(reviews.document(next.toString())).exists()) {
                check(next < Int.MAX_VALUE) { "Se agotaron los IDs de reseña." }
                next++
            }
            transaction.set(reviews.document(next.toString()), draft.toCreateReviewFirestoreDto().fields(next))
            // merge conserva la secuencia de usuarios en este mismo documento.
            transaction.set(sequence, mapOf("next_review_id" to next.toLong() + 1), SetOptions.merge())
            next
        }.await()
        return getReviewById(id)
    }

    override suspend fun updateReview(reviewId: Int, changes: ReviewChanges): ReviewInfo {
        require(reviewId > 0)
        changes.rating?.let { require(it in 1..5) }
        changes.body?.let { require(it.isNotBlank()) }
        val reference = reviews.document(reviewId.toString())
        firestore.runTransaction { transaction ->
            check(transaction.get(reference).exists()) { "No existe la reseña." }
            changes.userId?.let {
                require(it > 0)
                check(transaction.get(firestore.collection("users").document(it.toString())).exists())
            }
            changes.articleId?.let {
                require(it > 0)
                check(transaction.get(firestore.collection("articles").document(it.toString())).exists())
            }
            transaction.update(reference, changes.toUpdateReviewFirestoreDto().fields())
        }.await()
        return getReviewById(reviewId)
    }

    /** Retirada lógica común a ambas implementaciones; conserva el hilo y sus relaciones. */
    override suspend fun deleteReview(reviewId: Int) {
        updateReview(reviewId, ReviewChanges(isActive = false))
    }
}
