package com.example.devicersapp.ui.screens.access

/** Representa el estado visible de la pantalla de acceso. */
data class AccessState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val signInErrorMessage: String? = null
)
