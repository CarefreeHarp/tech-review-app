package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.dto.toReviewInfo
import com.example.devicersapp.data.dto.toCreateReviewRequestDto
import com.example.devicersapp.data.dto.toUpdateReviewRequestDto
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.models.ReviewDraft
import com.example.devicersapp.ui.models.ReviewChanges
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

/** Consulta y administra reseñas; traduce únicamente los DTOs de esta entidad. */
class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewRemoteDataSource
) {
    suspend fun getReviews(): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviews().map { it.toReviewInfo() }
            Result.success(reviews)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewById(reviewId: Int): Result<ReviewInfo> {
        return try {
            val review = reviewRemoteDataSource.getReviewById(reviewId).toReviewInfo()
            Result.success(review)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByUser(userId: Int): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByUser(userId).map { it.toReviewInfo() }
            Result.success(reviews)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getReviewsByProduct(productId: Int): Result<List<ReviewInfo>> {
        return try {
            val reviews = reviewRemoteDataSource.getReviewsByProduct(productId).map { it.toReviewInfo() }
            Result.success(reviews)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createReview(
        request: ReviewDraft
    ): Result<ReviewInfo> {
        return try {
            val review = reviewRemoteDataSource.createReview(request.toCreateReviewRequestDto()).toReviewInfo()
            Result.success(review)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReview(
        reviewId: Int,
        request: ReviewChanges
    ): Result<ReviewInfo> {
        return try {
            val review = reviewRemoteDataSource.updateReview(
                reviewId,
                request.toUpdateReviewRequestDto()
            ).toReviewInfo()

            Result.success(review)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteReview(reviewId: Int): Result<Unit> {
        return try {
            reviewRemoteDataSource.deleteReview(reviewId)
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}
