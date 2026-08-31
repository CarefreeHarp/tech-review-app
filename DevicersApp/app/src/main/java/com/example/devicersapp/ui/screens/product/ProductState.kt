package com.example.devicersapp.ui.screens.product

import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa el estado visible de la pantalla de detalle de producto. */
data class ProductState(
    val product: ProductContent? = null,
    val ratingSummary: RatingSummaryContent? = null,
    val reviews: List<ReviewContent> = emptyList()
)