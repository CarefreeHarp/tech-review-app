package com.example.devicersapp.ui.screens.rate_product

import com.example.devicersapp.ui.models.ProductContent

/** Representa el producto calificado y el formulario completo de la reseña. */
data class RateProductState(
    val product: ProductContent? = null,
    val rating: Int = 0,
    val title: String = "",
    val experience: String = "",
    val advantage: String = "",
    val disadvantage: String = ""
)
