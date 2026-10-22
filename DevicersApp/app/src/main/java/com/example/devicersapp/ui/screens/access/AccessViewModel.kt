package com.example.devicersapp.ui.screens.access

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Conserva y modifica el estado de la pantalla de acceso. */
@HiltViewModel
class AccessViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val users: UserRepository
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

    /**
     * Valida los campos obligatorios, inicia sesión, asocia el token FCM y notifica a la vista únicamente
     * cuando Firebase confirma el acceso.
     */
    fun onSignIn(onSignInSuccess: () -> Unit) {
        val current = _uiState.value
        val validationError = when {
            current.email.isBlank() && current.password.isBlank() -> R.string.auth_error_required_fields
            current.email.isBlank() -> R.string.auth_error_email_required
            current.password.isBlank() -> R.string.auth_error_password_required
            else -> null
        }
        _uiState.update { state -> state.copy(
            signInErrorMessage = null,
            signInErrorMessageResId = validationError
        ) }
        if (validationError != null) return

        viewModelScope.launch {
            if (signIn()) {
                // Las cuentas creadas antes de las notificaciones push obtienen aquí su token; el acceso no depende de ello.
                users.registerFcmToken()
                onSignInSuccess()
            }
        }
    }

    /** Inicia sesión y refleja en el estado el error contenido en el resultado. */
    private suspend fun signIn(): Boolean {
        val result = authRepository.signIn(
            email = _uiState.value.email,
            password = _uiState.value.password
        )
        return result.fold(
            onSuccess = { true },
            onFailure = { exception ->
                _uiState.update { state ->
                    state.copy(signInErrorMessage = exception.message.orEmpty())
                }
                false
            }
        )
    }
}
