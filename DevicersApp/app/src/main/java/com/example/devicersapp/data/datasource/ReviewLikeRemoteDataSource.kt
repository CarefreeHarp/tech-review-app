package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.ReviewLikeDto

/** Contrato remoto exclusivo de likes de reseñas; propaga los errores de consulta. */
interface ReviewLikeRemoteDataSource {
    suspend fun getReviewLikes(): List<ReviewLikeDto>
}
