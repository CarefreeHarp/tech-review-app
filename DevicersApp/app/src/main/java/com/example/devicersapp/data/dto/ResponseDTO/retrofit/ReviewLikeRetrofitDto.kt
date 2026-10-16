package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para likes de reseñas; las asociaciones anidadas son opcionales según el endpoint. */
data class ReviewLikeRetrofitDto(
    val user_id: Int,
    val created_at: String? = null,
    val id: Int,
    val review_id: Int,
    val createdAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun ReviewLikeRetrofitDto.toReviewLikeInfo(): ReviewLikeInfo = ReviewLikeInfo(
    userId = user_id,
    createdAt = created_at ?: createdAt,
    id = id,
    reviewId = review_id
)
