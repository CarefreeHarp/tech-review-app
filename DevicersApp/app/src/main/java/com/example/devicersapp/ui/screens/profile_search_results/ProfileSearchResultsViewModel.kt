package com.example.devicersapp.ui.screens.profile_search_results

import com.example.devicersapp.data.repository.FollowRepository

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import com.example.devicersapp.data.repository.UsersRepository
import com.example.devicersapp.data.dto.toSearchContent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.core.config.CURRENT_USER_ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva la consulta y los seguimientos visibles en los resultados de perfiles. */
@HiltViewModel
class ProfileSearchResultsViewModel @Inject constructor(private val users: UsersRepository, private val follows: FollowRepository) : ViewModel() {

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
                val results = users.getUsers(excludeUserId = CURRENT_USER_ID).filter { it.isActive }.map { it.toSearchContent() }
                val followedIds = follows.getFollows().filter { it.followerId == CURRENT_USER_ID }.map { it.followedId.toString() }.toSet()
                _uiState.update { it.copy(results = results, followedProfileIds = followedIds, loading = false) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.update { it.copy(loading = false, error = "No se pudieron cargar los usuarios.") } }
        }
    }

    /** Conserva el texto con el que se afina la búsqueda. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    /** Renueva el estado consultado; el backend actual solo permite leer seguimientos. */
    fun onFollow(profileId: String) {
        loadResults()
    }
}
