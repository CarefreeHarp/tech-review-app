package com.example.devicersapp.ui.screens.create_review

import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ProductCategoryContent

/**
 * Representa el estado visible de la pantalla para crear una reseña.
 */
data class CreateReviewState(
    val categories: List<ProductCategoryContent> = emptyList(),
    val products: List<ProductInfo> = emptyList(),
    val filteredProducts: List<ProductInfo> = emptyList(),
    val searchText: String = "",
    val selectedCategoryId: String = "all",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)