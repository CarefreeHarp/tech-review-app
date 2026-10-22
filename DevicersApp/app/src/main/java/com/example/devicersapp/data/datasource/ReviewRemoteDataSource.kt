package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
/** Operaciones de reseñas; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface ReviewRemoteDataSource {
    suspend fun getReviews(): List<ReviewInfo>
    /** Emite reseñas de los autores seleccionados; los transportes sin escucha entregan una lectura. */
    fun listenReviews(userIds: Set<Int>? = null): Flow<List<ReviewInfo>> = flow {
        emit(if (userIds == null) getReviews() else getReviewsByUsers(userIds))
    }
    suspend fun getReviewById(reviewId: Int): ReviewInfo
    suspend fun getReviewsByUser(userId: Int): List<ReviewInfo>
    /** Consulta únicamente autores seleccionados; cada transporte puede agrupar los IDs. */
    suspend fun getReviewsByUsers(userIds: Set<Int>): List<ReviewInfo> = userIds.flatMap { getReviewsByUser(it) }
    suspend fun getReviewsByProduct(productId: Int): List<ReviewInfo>
    suspend fun createReview(draft: ReviewDraft): ReviewInfo
    suspend fun updateReview(reviewId: Int, changes: ReviewChanges): ReviewInfo
    suspend fun deleteReview(reviewId: Int)
}
