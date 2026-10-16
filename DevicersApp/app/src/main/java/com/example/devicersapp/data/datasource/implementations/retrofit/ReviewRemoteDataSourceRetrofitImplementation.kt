package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa reseñas con Express y convierte sus respuestas a los modelos comunes. */
class ReviewRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: ReviewRetrofitService
) : ReviewRemoteDataSource {
    override suspend fun getReviews(): List<ReviewInfo> = service.getReviews().map { it.toReviewInfo() }
    override suspend fun getReviewById(reviewId: Int): ReviewInfo = service.getReviewById(reviewId).toReviewInfo()
    override suspend fun getReviewsByUser(userId: Int): List<ReviewInfo> = service.getReviewsByUser(userId).map { it.toReviewInfo() }
    override suspend fun getReviewsByProduct(productId: Int): List<ReviewInfo> = service.getReviewsByProduct(productId).map { it.toReviewInfo() }
    override suspend fun createReview(draft: ReviewDraft): ReviewInfo {
        require(draft.userId > 0 && draft.articleId > 0 && draft.rating in 1..5 && draft.body.isNotBlank())
        val created = service.create(draft.toCreateReviewRequestDto())
        // Las escrituras antiguas responden sin contadores; el GET obtiene la representación completa.
        return getReviewById(created.id)
    }
    override suspend fun updateReview(reviewId: Int, changes: ReviewChanges): ReviewInfo {
        changes.rating?.let { require(it in 1..5) }
        changes.body?.let { require(it.isNotBlank()) }
        service.update(reviewId, changes.toUpdateReviewRequestDto())
        return getReviewById(reviewId)
    }
    override suspend fun deleteReview(reviewId: Int) {
        updateReview(reviewId, ReviewChanges(isActive = false))
    }
}
