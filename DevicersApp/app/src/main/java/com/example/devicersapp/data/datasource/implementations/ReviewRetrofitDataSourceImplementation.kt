package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.datasource.services.ReviewRetrofitService
import com.example.devicersapp.data.dto.CreateReviewRequestDto
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.data.dto.UpdateReviewRequestDto
import javax.inject.Inject

class ReviewRetrofitDataSourceImplementation @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {

    override suspend fun getReviews(): List<ReviewDto> {
        return service.getReviews()
    }

    override suspend fun getReviewById(reviewId: Int): ReviewDto {
        return service.getReviewById(reviewId)
    }

    override suspend fun getReviewsByUser(userId: Int): List<ReviewDto> {
        return service.getReviewsByUser(userId)
    }

    override suspend fun getReviewsByProduct(productId: Int): List<ReviewDto> {
        return service.getReviewsByProduct(productId)
    }

    override suspend fun createReview(
        request: CreateReviewRequestDto
    ): ReviewDto {
        return service.createReview(request)
    }

    override suspend fun updateReview(
        reviewId: Int,
        request: UpdateReviewRequestDto
    ): ReviewDto {
        return service.updateReview(reviewId, request)
    }

    override suspend fun deleteReview(reviewId: Int) {
        service.deleteReview(reviewId)
    }
}