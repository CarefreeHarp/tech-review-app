package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.dto.CreateReviewRequestDto
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.data.dto.UpdateReviewRequestDto
import javax.inject.Inject

class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewRemoteDataSource
) {

    suspend fun getReviews(): Result<List<ReviewDto>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviews()
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(reviewId: Int): Result<ReviewDto> {
        return try {
            val review = reviewRemoteDataSource.getReviewById(reviewId)
            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUser(userId: Int): Result<List<ReviewDto>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByUser(userId)
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByProduct(productId: Int): Result<List<ReviewDto>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByProduct(productId)
            Result.success(reviews)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(
        request: CreateReviewRequestDto
    ): Result<ReviewDto> {
        return try {
            val review = reviewRemoteDataSource.createReview(request)
            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(
        reviewId: Int,
        request: UpdateReviewRequestDto
    ): Result<ReviewDto> {
        return try {
            val review = reviewRemoteDataSource.updateReview(
                reviewId,
                request
            )

            Result.success(review)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: Int): Result<Unit> {
        return try {
            reviewRemoteDataSource.deleteReview(reviewId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}