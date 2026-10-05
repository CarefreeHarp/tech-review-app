package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.ReviewBookmarkInfo

/** Campos de una reseña guardada tal como los devuelve la API. */
data class ReviewBookmarkDto(val user_id: Int, val review_id: Int)

/** Traduce la relación de guardado al modelo utilizado por el front. */
fun ReviewBookmarkDto.toReviewBookmarkInfo() = ReviewBookmarkInfo(
    userId = user_id,
    reviewId = review_id
)
