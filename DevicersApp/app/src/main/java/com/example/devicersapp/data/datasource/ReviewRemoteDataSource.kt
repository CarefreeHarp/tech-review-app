package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.data.dto.CreateReviewRequestDto
import com.example.devicersapp.data.dto.UpdateReviewRequestDto

/** Contrato de acceso remoto de review; los errores se propagan al consumidor. */
interface ReviewRemoteDataSource {
    suspend fun getReviews(): List<ReviewDto>

    suspend fun getReviewById(reviewId: Int): ReviewDto

    suspend fun getReviewsByUser(userId: Int): List<ReviewDto>

    suspend fun getReviewsByProduct(productId: Int): List<ReviewDto>

    suspend fun createReview(request: CreateReviewRequestDto): ReviewDto

    suspend fun updateReview(reviewId: Int, request: UpdateReviewRequestDto): ReviewDto

    suspend fun deleteReview(reviewId: Int): Unit
}
