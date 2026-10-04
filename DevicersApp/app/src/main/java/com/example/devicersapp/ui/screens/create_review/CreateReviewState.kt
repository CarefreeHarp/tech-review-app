package com.example.devicersapp.ui.screens.create_review

import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.ui.models.ProductCategoryContent

/**
 * Representa el estado visible de la pantalla para crear una reseña.
 */
data class CreateReviewState(
    val categories: List<ProductCategoryContent> = emptyList(),
    val products: List<ProductDto> = emptyList(),
    val filteredProducts: List<ProductDto> = emptyList(),
    val searchText: String = "",
    val selectedCategoryId: String = "all",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)