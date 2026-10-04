package com.example.devicersapp.data.dto

import com.google.gson.annotations.SerializedName

/** Like que un usuario dio a una reseña. */
data class ReviewLikeDto(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("review_id") val reviewId: Int,
)
