package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para artículos; las asociaciones anidadas son opcionales según el endpoint. */
data class ProductRetrofitDto(
    val id: Int,
    val category_id: Int,
    val category_name: String,
    val brand_name: String,
    val name: String,
    val model: String? = null,
    val description: String? = null,
    val image_url: String? = null,
    val release_date: String? = null,
    val specifications: String? = null,
    val stores_count: Int,
    val is_active: Boolean,
    val created_at: String? = null,
    val updated_at: String? = null,
    val brand_id: Int? = null,
    val stores: List<ArticleStoreDto>? = null,
    val reviews: List<ReviewRetrofitDto>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun ProductRetrofitDto.toProductInfo(): ProductInfo = ProductInfo(
    id = id,
    categoryId = category_id,
    categoryName = category_name,
    brandName = brand_name,
    name = name,
    model = model,
    description = description,
    imageUrl = image_url,
    releaseDate = release_date,
    specifications = specifications,
    storesCount = stores_count,
    isActive = is_active,
    createdAt = checkNotNull(created_at ?: createdAt) { "Fecha created_at ausente" },
    updatedAt = checkNotNull(updated_at ?: updatedAt) { "Fecha updated_at ausente" },
    brandId = brand_id,
    stores = stores.orEmpty().map { it.toArticleStoreInfo() },
    reviews = reviews.orEmpty().map { it.toReviewInfo() }
)
