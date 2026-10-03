package com.example.devicersapp.data.dto

import com.google.gson.annotations.SerializedName

/** Cuerpo de actualización parcial; Gson omite los campos nulos. */
data class UpdateReviewRequestDto(
    val rating: Int? = null,
    val title: String? = null,
    val body: String? = null,
    @SerializedName("is_active") val isActive: Boolean? = null,
    @SerializedName("user_id") val userId: Int? = null,
    @SerializedName("article_id") val articleId: Int? = null,
)
