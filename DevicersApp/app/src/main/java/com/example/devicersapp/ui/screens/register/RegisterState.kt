package com.example.devicersapp.ui.screens.register

/** Representa el estado observable completo del formulario de registro. */
data class RegisterState(
    val username: String = "",
    val email: String = "",
    val password: String = "",
    val confirmationPassword: String = "",
    val isPasswordVisible: Boolean = false,
    val showValidationWarning: Boolean = false,
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isConfirmationPasswordValid: Boolean = false,
    val registrationErrorMessage: String? = null
)
