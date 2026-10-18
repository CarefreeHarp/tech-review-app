package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
/** Respuesta de Express al alternar un like: el estado final y el conteo calculado por SQL. */
data class ReviewLikeStatusRetrofitDto(
    val review_id: Int,
    val user_id: Int,
    val is_liked: Boolean,
    val likes_count: Int
)
/** Traduce la respuesta al mismo estado confirmado que entrega la implementación de Firestore. */
fun ReviewLikeStatusRetrofitDto.toReviewLikeStatus(): ReviewLikeStatus = ReviewLikeStatus(
    isLiked = is_liked,
    likes = likes_count
)
