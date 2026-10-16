package com.example.devicersapp.ui.screens.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Mantiene la cabecera de Activity sin cargar notificaciones de likes ni seguimientos. */
@HiltViewModel
class ActivityViewModel @Inject constructor(private val users: UserRepository) : ViewModel() {
    private val state = MutableStateFlow(ActivityState())
    val uiState = state.asStateFlow()
    init { loadActivity() }

    /** Consulta solo el perfil propio necesario para mostrar el avatar de la cabecera. */
    fun loadActivity() {
        state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val profile = users.getAuthenticatedUser()
                state.update { it.copy(currentProfile = profile, loading = false) }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                state.update { it.copy(loading = false, error = R.string.activity_load_error) }
            }
        }
    }
}
