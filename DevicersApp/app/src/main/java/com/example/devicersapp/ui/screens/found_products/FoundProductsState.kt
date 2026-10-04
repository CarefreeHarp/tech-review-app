package com.example.devicersapp.ui.screens.found_products

import com.example.devicersapp.data.dto.ProductDto

/**
 * Representa el estado visible de la pantalla de productos encontrados.
 */
data class FoundProductsState(
    val results: List<ProductDto> = emptyList(),
    val searchText: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)