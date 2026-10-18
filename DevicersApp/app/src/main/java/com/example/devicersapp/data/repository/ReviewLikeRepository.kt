package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Operaciones de likes de reseñas independientes de la tecnología de persistencia. */
class ReviewLikeRepository(private val source: ReviewLikeRemoteDataSource) {
    suspend fun getReviewLikes(reviewId: Int? = null): List<ReviewLikeInfo> = source.getReviewLikes(reviewId)
    /** Da o quita el like del usuario y entrega el estado confirmado, o el error de la fuente seleccionada. */
    suspend fun sendReviewLike(reviewId: Int, userId: Int): Result<ReviewLikeStatus> =
        repositoryResult { source.sendReviewLike(reviewId, userId) }
}
