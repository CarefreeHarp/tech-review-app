package com.example.devicersapp.data.dto

import com.google.gson.annotations.SerializedName
import com.google.gson.JsonElement

/** Datos de la API; las relaciones anidadas solo están presentes en algunas consultas. */
data class ProductDto(
    val id: Int,
    @SerializedName("category_id") val categoryId: Int,
    @SerializedName("brand_id") val brandId: Int,
    val name: String,
    val model: String?,
    val description: String?,
    @SerializedName("image_url") val imageUrl: String?,
    @SerializedName("release_date") val releaseDate: String?,
    val specifications: JsonElement?,
    @SerializedName("is_active") val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val reviews: List<ReviewDto>? = null,
)
