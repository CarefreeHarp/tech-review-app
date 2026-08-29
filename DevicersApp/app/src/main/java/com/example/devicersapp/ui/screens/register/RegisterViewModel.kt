package com.example.devicersapp.ui.screens.register

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Gestiona la validación y las acciones de presentación del registro. */
class RegisterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterState())
    val uiState: StateFlow<RegisterState> = _uiState

    /** Actualiza el nombre que la persona escribe en el formulario. */
    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    /** Actualiza el correo y vuelve a calcular las validaciones relacionadas. */
    fun onEmailChange(email: String) {
        _uiState.update { state -> state.withValidation(email = email) }
    }

    /** Actualiza la contraseña y vuelve a calcular las validaciones relacionadas. */
    fun onPasswordChange(password: String) {
        _uiState.update { state -> state.withValidation(password = password) }
    }

    /** Actualiza la confirmación de contraseña y vuelve a calcular su coincidencia. */
    fun onConfirmationPasswordChange(confirmationPassword: String) {
        _uiState.update { state -> state.withValidation(confirmationPassword = confirmationPassword) }
    }

    /** Alterna la visibilidad compartida de los dos campos de contraseña. */
    fun onPasswordVisibilityChange() {
        _uiState.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
    }

    /** Valida el formulario y devuelve si sus datos permiten crear la cuenta. */
    fun onCreateAccount(): Boolean {
        var isRegistrationValid = false

        _uiState.update { state ->
            val validatedState = state.withValidation()
            isRegistrationValid = validatedState.isEmailValid &&
                validatedState.isPasswordValid &&
                validatedState.isConfirmationPasswordValid
            validatedState.copy(showValidationWarning = true)
        }
        return isRegistrationValid
    }

    /** Construye un estado consistente sin duplicar las reglas de validación. */
    private fun RegisterState.withValidation(
        email: String = this.email,
        password: String = this.password,
        confirmationPassword: String = this.confirmationPassword
    ): RegisterState {
        val emailIsValid = email.matches(EMAIL_REGEX)
        val passwordIsValid = password.matches(PASSWORD_REGEX)

        return copy(
            email = email,
            password = password,
            confirmationPassword = confirmationPassword,
            isEmailValid = emailIsValid,
            isPasswordValid = passwordIsValid,
            isConfirmationPasswordValid = confirmationPassword.isNotEmpty() &&
                password == confirmationPassword
        )
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
        val PASSWORD_REGEX = Regex("^(?=(?:.*\\d){3,}).{6,}$")
    }
}
