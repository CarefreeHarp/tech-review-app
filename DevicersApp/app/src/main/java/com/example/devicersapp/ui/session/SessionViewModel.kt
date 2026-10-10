package com.example.devicersapp.ui.session

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
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

    /** Combina la cuenta de Auth con su perfil de Firestore y comparte ambos identificadores. */
    private fun observeCurrentProfile() {
        viewModelScope.launch {
            combine(authRepository.currentUserState, authRepository.currentProfileState) { user, profile ->
                user to profile?.takeIf { it.firebaseUid == user?.uid }
            }.collectLatest { (user, profile) ->
                val username = profile?.username.orEmpty()
                _uiState.update {
                    it.copy(
                        currentProfileHandle = username.asUserHandle(),
                        profileImageUrl = profile?.profileImageUrl,
                        firebaseUid = user?.uid,
                        userId = profile?.id,
                        profile = profile
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
