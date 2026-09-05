package com.example.devicersapp.ui.screens.profile_search_results

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProfileProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva la consulta y los seguimientos visibles en los resultados de perfiles. */
@HiltViewModel
class ProfileSearchResultsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSearchResultsState())
    val uiState: StateFlow<ProfileSearchResultsState> = _uiState

    init {
        loadResults()
    }

    /** Carga los perfiles disponibles cuando se crea el ViewModel. */
    private fun loadResults() {
        _uiState.update { it.copy(results = LocalProfileProvider.profiles) }
    }

    /** Conserva el texto con el que se afina la búsqueda. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    /** Marca un perfil como seguido dentro de la sesión visible. */
    fun onFollow(profileId: String) {
        _uiState.update { state ->
            state.copy(followedProfileIds = state.followedProfileIds + profileId)
        }
    }
}
