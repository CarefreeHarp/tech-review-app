package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.ReviewLikeInfo

/** Like que un usuario dio a una reseña. */
data class ReviewLikeDto(
    val id: Int,
    val user_id: Int,
    val review_id: Int,
)

/** Traduce los campos de la API al modelo de reacción del front. */
fun ReviewLikeDto.toReviewLikeInfo(): ReviewLikeInfo = ReviewLikeInfo(
    id = id,
    userId = user_id,
    reviewId = review_id,
)
