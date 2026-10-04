package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.dto.*
import javax.inject.Inject

class ReviewRepository @Inject constructor(private val source: ReviewRemoteDataSource) {
    suspend fun getReviews(): List<ReviewDto> = source.getReviews()

    suspend fun getReviewById(reviewId: Int): ReviewDto = source.getReviewById(reviewId)

    suspend fun getReviewsByUser(userId: Int): List<ReviewDto> = source.getReviewsByUser(userId)

    suspend fun getReviewsByProduct(productId: Int): List<ReviewDto> =
        source.getReviewsByProduct(productId)

    suspend fun createReview(request: CreateReviewRequestDto): ReviewDto =
        source.createReview(request)

    suspend fun updateReview(reviewId: Int, request: UpdateReviewRequestDto): ReviewDto =
        source.updateReview(reviewId, request)

    suspend fun deleteReview(reviewId: Int): Unit = source.deleteReview(reviewId)
}
