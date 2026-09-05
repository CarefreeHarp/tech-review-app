package com.example.devicersapp.ui.session

import androidx.lifecycle.ViewModel
import com.example.devicersapp.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/** Gestiona las acciones de sesión disponibles desde elementos globales de la interfaz. */
@HiltViewModel
class SessionViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    /** Cierra la sesión activa mediante la fuente remota de autenticación. */
    fun signOut() {
        authRepository.signOut()
    }
}
