package com.example.devicersapp.ui.screens.product

import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.data.dto.ReviewDto

/** Representa el estado visible de la pantalla de detalle de producto. */
data class ProductState(
    val product: ProductDto? = null,
    val reviews: List<ReviewDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)