package com.example.devicersapp.ui.screens.profile_search_results

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import com.example.devicersapp.data.repository.UsersRepository
import com.example.devicersapp.ui.mappers.toSearchContent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProfileProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva la consulta y los seguimientos visibles en los resultados de perfiles. */
@HiltViewModel
class ProfileSearchResultsViewModel @Inject constructor(private val users: UsersRepository) : ViewModel() {

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
                val results = users.getUsers().map { it.toSearchContent() }
                _uiState.update { it.copy(results = results, loading = false) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.update { it.copy(loading = false, error = "No se pudieron cargar los usuarios.") } }
        }
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
