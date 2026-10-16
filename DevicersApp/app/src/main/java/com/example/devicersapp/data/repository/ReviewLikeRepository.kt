package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Operaciones de likes de reseñas independientes de la tecnología de persistencia. */
class ReviewLikeRepository(private val source: ReviewLikeRemoteDataSource) {
    suspend fun getReviewLikes(reviewId: Int? = null): List<ReviewLikeInfo> = source.getReviewLikes(reviewId)
}
