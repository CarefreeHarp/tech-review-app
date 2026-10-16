package com.example.devicersapp.data.dto.RequestDTO.retrofit
import com.example.devicersapp.ui.models.ProductSuggestion
/** Solicitud de producto con ID repetible y user_id, sin firebase_uid. */
data class CreateProductRequestDto(val id: String, val user_id: Int, val product_name: String, val image_url: String?,
    val category: String, val brand: String, val release_date: String, val status: String = "pending")
/** Copia los campos del formulario sin reinterpretar la fecha de lanzamiento. */
fun ProductSuggestion.toCreateProductRequestDto(id: String, userId: Int) =
    CreateProductRequestDto(id, userId, productName, imageUrl, category, brand, releaseDate)
