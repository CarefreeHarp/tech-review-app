package com.example.devicersapp.data.dto

import com.google.gson.annotations.SerializedName

/** Categoría de un artículo tal como la devuelve el endpoint `categories`. */
data class CategoryDto(
    val id: Int,
    @SerializedName("parent_category_id") val parentCategoryId: Int?,
    val name: String,
    val description: String?,
)
