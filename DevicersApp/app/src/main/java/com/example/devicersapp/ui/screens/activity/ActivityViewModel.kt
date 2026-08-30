package com.example.devicersapp.ui.screens.activity

import androidx.lifecycle.ViewModel
import com.example.devicersapp.data.local.LocalActivityProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Obtiene y conserva el contenido y las acciones de la pantalla de actividad. */
class ActivityViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ActivityState())
    val uiState: StateFlow<ActivityState> = _uiState

    init {
        loadActivities()
    }

    /** Carga los grupos de actividad que se mostrarán en pantalla. */
    private fun loadActivities() {
        _uiState.update { currentState ->
            currentState.copy(
                activityGroups = LocalActivityProvider.activityGroups(
                    currentTimeMillis = System.currentTimeMillis()
                )
            )
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