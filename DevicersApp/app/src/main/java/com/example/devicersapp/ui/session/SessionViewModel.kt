package com.example.devicersapp.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Gestiona las acciones de sesión disponibles desde elementos globales de la interfaz. */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SessionState())
    val uiState: StateFlow<SessionState> = _uiState

    init {
        observeCurrentProfile()
    }

    /** Observa la cuenta autenticada para mantener actualizado el alias de la barra superior. */
    private fun observeCurrentProfile() {
        viewModelScope.launch {
            authRepository.currentUserState.collectLatest { user ->
                val displayName = user?.displayName?.trim().orEmpty()
                _uiState.update {
                    it.copy(
                        currentProfileHandle = displayName
                            .removePrefix("@")
                            .takeIf { name -> name.isNotBlank() }
                            ?.let { name -> "@$name" }
                            .orEmpty(),
                        profileImageUrl = user?.photoUrl?.toString()
                    )
                }
            }
        }
    }

    /** Cierra la sesión activa mediante la fuente remota de autenticación. */
    fun signOut() {
        authRepository.signOut()
    }
}
