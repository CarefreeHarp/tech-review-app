package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para solicitudes de productos; las asociaciones anidadas son opcionales según el endpoint. */
data class ProductRequestRetrofitDto(
    val user_id: Int,
    val product_name: String,
    val image_url: String? = null,
    val category: String? = null,
    val brand: String? = null,
    val release_date: String? = null,
    val status: String,
    val createdAt: String? = null,
    val id: String
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun ProductRequestRetrofitDto.toProductRequestInfo(): ProductRequestInfo = ProductRequestInfo(
    userId = user_id,
    productName = product_name,
    imageUrl = image_url,
    category = category,
    brand = brand,
    releaseDate = release_date,
    status = status,
    createdAt = checkNotNull(createdAt),
    id = id
)
