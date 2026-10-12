package com.example.devicersapp.ui.screens.profile_search_results

import com.example.devicersapp.data.repository.DiscoveryRepository

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva la consulta y los seguimientos visibles en los resultados de perfiles. */
@HiltViewModel
class ProfileSearchResultsViewModel @Inject constructor(
    private val repository: DiscoveryRepository,
    private val session: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSearchResultsState())
    val uiState: StateFlow<ProfileSearchResultsState> = _uiState

    init {
        loadResults()
    }

    /** Carga los perfiles disponibles cuando se crea el ViewModel. */
    fun loadResults() {
        if (_uiState.value.loading) return
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val currentUserId = session.requireCurrentProfile().id
                val result = repository.getProfiles(currentUserId)
                if (session.currentProfile.value?.id != currentUserId) return@launch
                _uiState.update { it.copy(results = result.profiles, followedProfileIds = result.followedIds, loading = false) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.update { it.copy(loading = false, error = "No se pudieron cargar los usuarios.") } }
        }
    }

    /** Conserva el texto con el que se afina la búsqueda. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    /** Renueva el estado consultado; esta pantalla solo permite leer seguimientos. */
    fun onFollow(profileId: String) {
        loadResults()
    }
}
