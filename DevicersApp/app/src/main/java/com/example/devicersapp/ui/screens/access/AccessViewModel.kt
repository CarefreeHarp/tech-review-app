package com.example.devicersapp.ui.screens.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Conserva y modifica el estado de la pantalla de acceso. */
@HiltViewModel
class AccessViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

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

    /** Inicia sesión y notifica a la vista únicamente cuando Firebase confirma el acceso. */
    fun onSignIn(onSignInSuccess: () -> Unit) {
        _uiState.update { state -> state.copy(signInErrorMessage = null) }

        viewModelScope.launch {
            if (signIn()) {
                onSignInSuccess()
            }
        }
    }

    /** Inicia sesión y devuelve si Firebase completó la operación correctamente. */
    private suspend fun signIn(): Boolean = try {
        authRepository.signIn(
            email = _uiState.value.email,
            password = _uiState.value.password
        )
        true
    } catch (exception: Exception) {
        _uiState.update { state ->
            state.copy(signInErrorMessage = exception.message.toString())
        }
        false
    }
}
