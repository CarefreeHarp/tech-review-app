package com.example.devicersapp.ui.screens.request_product

/** Representa el formulario y las validaciones de la solicitud de un producto. */
data class RequestProductState(
    val productName: String = "",
    val category: String = "",
    val brand: String = "",
    val releaseDate: String = "",
    val isReleaseDateValid: Boolean = true,
    val showReleaseDateError: Boolean = false,
    val canSendRequest: Boolean = false
)
