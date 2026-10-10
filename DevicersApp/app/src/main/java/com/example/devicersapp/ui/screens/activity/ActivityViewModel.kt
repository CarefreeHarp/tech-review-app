package com.example.devicersapp.ui.screens.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.UsersRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalActivityProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Obtiene y conserva el contenido y las acciones de la pantalla de actividad. */
@HiltViewModel
class ActivityViewModel @Inject constructor(private val users: UsersRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityState())
    val uiState: StateFlow<ActivityState> = _uiState

    init {
        loadActivity()
    }

    /** Carga los grupos de actividad que se mostrarán en pantalla. */
    private fun loadActivity() {
        _uiState.update { currentState ->
            currentState.copy(
                activityGroups = LocalActivityProvider.activityGroups(
                    currentTimeMillis = System.currentTimeMillis()
                )
            )
        }
        viewModelScope.launch {
            val ids = _uiState.value.activityGroups.flatMap { it.notifications }
                .mapNotNull { it.actorProfileId.toIntOrNull() }.toSet()
            val images = try {
                users.getProfileImages(ids)
            } catch (error: CancellationException) {
                throw error
            } catch (_: Exception) {
                // La actividad local sigue disponible mientras no se puede consultar la foto.
                emptyMap()
            }
            _uiState.update { it.copy(profileImages = images) }
        }
    }

    /** Marca como seguido al usuario asociado con una actividad. */
    fun followActivity(activityId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                followedActivityIds =
                    currentState.followedActivityIds + activityId
            )
        }
    }
}
