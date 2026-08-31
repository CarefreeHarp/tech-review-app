package com.example.devicersapp.ui.screens.found_products

import com.example.devicersapp.ui.models.ProductSearchContent

/** Representa el estado visible de la pantalla de productos encontrados. */
data class FoundProductsState(
    val results: List<ProductSearchContent> = emptyList(),
    val searchText: String = ""
)