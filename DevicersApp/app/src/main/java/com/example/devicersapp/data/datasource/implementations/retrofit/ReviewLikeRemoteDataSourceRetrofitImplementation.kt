package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa likes de reseñas con Express y convierte sus respuestas a los modelos comunes. */
class ReviewLikeRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: ReviewLikeRetrofitService
) : ReviewLikeRemoteDataSource {
    override suspend fun getReviewLikes(reviewId: Int?): List<ReviewLikeInfo> = service.getReviewLikes()
        .filter { reviewId == null || it.review_id == reviewId }.map { it.toReviewLikeInfo() }
}
