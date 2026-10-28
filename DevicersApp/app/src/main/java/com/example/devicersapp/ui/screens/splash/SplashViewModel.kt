package com.example.devicersapp.ui.screens.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.utils.Utils
import com.example.devicersapp.data.repository.UserRepository
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
    private val users: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())
    val uiState: StateFlow<SplashState> = _uiState

    init {
        checkUser()
    }

    /** Consulta la sesión persistida por Firebase sin cargar una sesión propia ni depender de la base de datos. */
    private fun checkUser() {
        viewModelScope.launch {
            // Mantiene la pantalla de bienvenida visible sin bloquear el hilo principal.
            delay(2000)
            val account = Utils.getCurrentUser()
            val destination = when {
                account == null -> SplashDestination.LOGIN
                users.findByFirebaseUid(account.uid) == null -> SplashDestination.COMPLETE_GOOGLE_PROFILE
                else -> SplashDestination.HOME
            }
            _uiState.update { it.copy(destination = destination) }
        }
    }
}
