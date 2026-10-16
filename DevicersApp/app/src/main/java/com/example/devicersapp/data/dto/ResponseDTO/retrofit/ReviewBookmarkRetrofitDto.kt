package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para reseñas guardadas; las asociaciones anidadas son opcionales según el endpoint. */
data class ReviewBookmarkRetrofitDto(
    val review_id: Int,
    val created_at: String? = null,
    val user_id: Int,
    val createdAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun ReviewBookmarkRetrofitDto.toReviewBookmarkInfo(): ReviewBookmarkInfo = ReviewBookmarkInfo(
    reviewId = review_id,
    createdAt = created_at ?: createdAt,
    userId = user_id
)
