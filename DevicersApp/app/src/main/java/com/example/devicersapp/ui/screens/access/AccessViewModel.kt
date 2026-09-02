package com.example.devicersapp.ui.screens.access

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Conserva y modifica el estado de la pantalla de acceso. */
class AccessViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AccessState())
    val uiState: StateFlow<AccessState> = _uiState

    /** Actualiza el correo escrito por el usuario. */
    fun onEmailChange(email: String) {
        _uiState.update { currentState ->
            currentState.copy(email = email)
        }
    }

    /** Actualiza la contraseña escrita por el usuario. */
    fun onPasswordChange(password: String) {
        _uiState.update { currentState ->
            currentState.copy(password = password)
        }
    }

    /** Alterna la visibilidad de la contraseña. */
    fun onPasswordVisibilityChange() {
        _uiState.update { currentState ->
            currentState.copy(
                isPasswordVisible = !currentState.isPasswordVisible
            )
        }
    }
}