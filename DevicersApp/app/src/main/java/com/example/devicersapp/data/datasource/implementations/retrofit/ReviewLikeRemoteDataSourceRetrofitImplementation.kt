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
    override suspend fun getLikedReviewIds(userId: Int, reviewIds: Set<Int>): Set<Int> = service.getReviewLikes()
        .filter { it.user_id == userId && it.review_id in reviewIds }.map { it.review_id }.toSet()
    /**
     * Express alterna el like en una transacción SQL que también crea o elimina la notificación del autor,
     * igual que la transacción de Firestore; la respuesta trae el estado y el conteo ya confirmados.
     */
    override suspend fun sendReviewLike(reviewId: Int, userId: Int): ReviewLikeStatus {
        require(reviewId > 0 && userId > 0)
        return service.toggleReviewLike(reviewId, ToggleReviewLikeRequestDto(user_id = userId)).toReviewLikeStatus()
    }
}
