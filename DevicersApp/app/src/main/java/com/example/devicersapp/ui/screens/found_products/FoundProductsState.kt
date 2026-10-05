package com.example.devicersapp.ui.screens.found_products

import com.example.devicersapp.ui.models.ProductInfo

/**
 * Representa el estado visible de la pantalla de productos encontrados.
 */
data class FoundProductsState(
    val results: List<ProductInfo> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)