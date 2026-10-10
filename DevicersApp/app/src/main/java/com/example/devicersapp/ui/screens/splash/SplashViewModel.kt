package com.example.devicersapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Comprueba si existe una sesión de Firebase antes de decidir el primer destino de la aplicación. */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState

    init {
        checkUser()
    }

    /** Restaura el perfil vinculado a Auth antes de permitir el acceso a la aplicación. */
    private fun checkUser() {
        viewModelScope.launch {
            // Mantiene la pantalla de bienvenida visible sin bloquear el hilo principal.
            delay(2000)
            val restored = authRepository.restoreSession()
            _uiState.update { currentState ->
                currentState.copy(
                    isUserAuthenticated = restored.isSuccess && authRepository.currentProfile != null
                )
            }
        }
    }
}
