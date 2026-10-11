package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.ProductInfo
import com.google.gson.Gson

/**
 * Artículo tal como se guarda en una base documental, con su marca y categoría ya resueltas por nombre.
 *
 * Las reseñas guardan una copia de estos mismos campos para mostrarse sin consultar el artículo.
 */
data class ArticleDocumentDto(
    val id: Int,
    val name: String,
    val model: String?,
    val description: String?,
    val imageUrl: String?,
    val brandId: Int,
    val brandName: String,
    val categoryId: Int,
    val categoryName: String,
    val releaseDate: String?,
    val specifications: Map<String, Any?>?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String
)

/** Traduce el artículo al modelo de producto que ya consumen las pantallas. */
fun ArticleDocumentDto.toProductInfo(): ProductInfo = ProductInfo(
    id = id,
    categoryId = categoryId,
    brandId = brandId,
    name = name,
    model = model,
    description = description,
    imageUrl = imageUrl,
    releaseDate = releaseDate,
    // Conserva el mismo texto JSON que entregaba la API para las especificaciones.
    specifications = specifications?.let { Gson().toJson(it) },
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt
)
