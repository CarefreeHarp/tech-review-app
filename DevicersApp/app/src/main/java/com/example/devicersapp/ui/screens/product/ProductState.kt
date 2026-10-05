package com.example.devicersapp.ui.screens.product

import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ReviewInfo

/** Representa el estado visible de la pantalla de detalle de producto. */
data class ProductState(
    val product: ProductInfo? = null,
    val reviews: List<ReviewInfo> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    @param:androidx.annotation.StringRes val errorMessageResId: Int? = null
)