package com.example.devicersapp.ui.screens.create_review

import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.ui.models.ProductSearchContent

/** Representa el estado visible de la pantalla para crear una reseña. */
data class CreateReviewState(
    val categories: List<ProductCategoryContent> = emptyList(),
    val products: List<ProductSearchContent> = emptyList(),
    val filteredProducts: List<ProductSearchContent> = emptyList(),
    val searchText: String = "",
    val selectedCategoryId: String = "all"
)