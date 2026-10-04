package com.example.devicersapp.ui.screens.rate_product

import com.example.devicersapp.data.dto.ProductDto

/**
 * Representa el producto calificado y el formulario completo de la reseña.
 */
data class RateProductState(
    val product: ProductDto? = null,
    val rating: Int = 0,
    val title: String = "",
    val experience: String = "",
    val advantage: String = "",
    val disadvantage: String = "",
    val isLoading: Boolean = false,
    val isPublishing: Boolean = false,
    val errorMessage: String? = null,
    val publishError: String? = null,
    val publishedSuccessfully: Boolean = false
)