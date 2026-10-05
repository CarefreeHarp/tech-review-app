package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.utils.images.localImageResIdFor
import kotlin.math.roundToInt
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.*

import com.google.gson.JsonElement

/** Datos de la API; las relaciones anidadas solo están presentes en algunas consultas. */
data class ProductDto(
    val id: Int,
    val category_id: Int,
    val brand_id: Int,
    val name: String,
    val model: String?,
    val description: String?,
    val image_url: String?,
    val release_date: String?,
    val specifications: JsonElement?,
    val is_active: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val reviews: List<ReviewDto>? = null,
)

/** Traduce los campos de la API al modelo de producto del front. */
fun ProductDto.toProductInfo(): ProductInfo = ProductInfo(
    id = id,
    categoryId = category_id,
    brandId = brand_id,
    name = name,
    model = model,
    description = description,
    imageUrl = image_url,
    releaseDate = release_date,
    specifications = specifications?.toString(),
    isActive = is_active,
    createdAt = createdAt,
    updatedAt = updatedAt,
    reviews = reviews.orEmpty().map { it.toReviewInfo() },
)

/** Adapta el modelo del front al contenido visual utilizado por la pantalla. */
fun ProductInfo.toProductContent() =
    ProductContent(
        nameResId = R.string.rate_product_name,
        brandResId = R.string.rate_product_brand,
        imageResId = localImageResIdFor(imageUrl) ?: R.drawable.logo_icono_claro,
        imageDescriptionResId = R.string.rate_product_image_description,
        id = id,
        name = name,
        metadata = model.orEmpty(),
        imageUrl = imageUrl,
    )

/** Adapta el modelo del front al contenido visual utilizado por la pantalla. */
fun ProductInfo.toSearchContent() =
    ProductSearchContent(
        id = id.toString(),
        categoryId = categoryId.toString(),
        searchTerms = listOfNotNull(name, model),
        nameResId = R.string.rate_product_name,
        brandResId = R.string.rate_product_brand,
        categoryResId = R.string.rate_product_brand,
        imageDescriptionResId = R.string.rate_product_image_description,
        imageResId = localImageResIdFor(imageUrl) ?: R.drawable.logo_icono_claro,
        rating = reviews.filter { it.isActive }.map { it.rating }.average().takeIf { it.isFinite() }?.roundToInt() ?: 0,
        name = name,
        metadata = model.orEmpty(),
        imageUrl = imageUrl,
    )
