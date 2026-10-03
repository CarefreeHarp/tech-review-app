package com.example.devicersapp.data.dto

import com.google.gson.annotations.SerializedName

/** Datos de la API; las relaciones anidadas solo están presentes en algunas consultas. */
data class ReviewDto(
    val id: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("article_id") val articleId: Int,
    val rating: Int,
    val title: String?,
    val body: String,
    @SerializedName("is_active") val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val user: UserDto? = null,
    val article: ProductDto? = null,
)
