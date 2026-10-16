package com.example.devicersapp.ui.models

/** Datos comunes del artículo; brandId solo existe en SQL y las especificaciones conservan el JSON como texto. */
data class ProductInfo(
    val id: Int,
    val categoryId: Int,
    val brandId: Int?,
    val name: String,
    val model: String?,
    val description: String?,
    val imageUrl: String?,
    val releaseDate: String?,
    val specifications: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val brandName: String = "",
    val categoryName: String = "",
    val storesCount: Int = 0,
    val stores: List<ArticleStoreInfo> = emptyList(),
    val reviews: List<ReviewInfo> = emptyList(),
)
