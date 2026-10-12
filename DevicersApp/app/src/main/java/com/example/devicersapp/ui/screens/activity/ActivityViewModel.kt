package com.example.devicersapp.ui.screens.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.DiscoveryRepository
import com.example.devicersapp.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val repository: DiscoveryRepository,
    private val session: SessionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ActivityState())
    val uiState = _uiState.asStateFlow()

    init { loadActivity() }

    fun loadActivity() {
        if (_uiState.value.loading) return
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val userId = session.requireCurrentProfile().id
                val result = repository.getActivity(userId)
                if (session.currentProfile.value?.id != userId) return@launch
                _uiState.update { it.copy(activityGroups = result.groups,
                    followedActivityIds = result.followedActivityIds,
                    profileImages = result.actors.mapValues { (_, actor) -> actor.profileImageUrl },
                    actorNames = result.actors.mapValues { (_, actor) -> actor.username },
                    productNames = result.productNames, loading = false) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.update { it.copy(loading = false, error = "No se pudo cargar la actividad.") } }
        }
    }

    /** La actividad es de solo lectura: no simula escrituras de seguimiento. */
    fun followActivity(activityId: String) = Unit
}
