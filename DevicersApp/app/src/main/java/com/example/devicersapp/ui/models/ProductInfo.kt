package com.example.devicersapp.ui.models

/** Datos de producto independientes del contrato JSON del backend. */
data class ProductInfo(
    val id: Int,
    val categoryId: Int,
    val brandId: Int,
    val name: String,
    val model: String?,
    val description: String?,
    val imageUrl: String?,
    val releaseDate: String?,
    val specifications: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val reviews: List<ReviewInfo> = emptyList(),
)
