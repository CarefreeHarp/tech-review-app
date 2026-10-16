package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de reseñas; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface ReviewRemoteDataSource {
    suspend fun getReviews(): List<ReviewInfo>
    suspend fun getReviewById(reviewId: Int): ReviewInfo
    suspend fun getReviewsByUser(userId: Int): List<ReviewInfo>
    suspend fun getReviewsByProduct(productId: Int): List<ReviewInfo>
    suspend fun createReview(draft: ReviewDraft): ReviewInfo
    suspend fun updateReview(reviewId: Int, changes: ReviewChanges): ReviewInfo
    suspend fun deleteReview(reviewId: Int)
}
