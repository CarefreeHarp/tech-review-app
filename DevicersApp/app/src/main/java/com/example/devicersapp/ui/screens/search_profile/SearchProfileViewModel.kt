package com.example.devicersapp.ui.screens.search_profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva la consulta y los filtros con los que se acota la búsqueda de perfiles. */
@HiltViewModel
class SearchProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SearchProfileState())
    val uiState: StateFlow<SearchProfileState> = _uiState

    /** Conserva el texto con el que se busca un perfil. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    /** Conserva el nombre de usuario con el que se filtran los perfiles. */
    fun onUsernameChange(username: String) {
        _uiState.update { it.copy(username = username) }
    }

    /** Conserva los intereses con los que se filtran los perfiles. */
    fun onInterestsChange(interests: String) {
        _uiState.update { it.copy(interests = interests) }
    }

    /** Cambia la cantidad mínima de reseñas exigida a los perfiles. */
    fun onMinimumReviewsChange(minimumReviews: Float) {
        _uiState.update { it.copy(minimumReviews = minimumReviews) }
    }

    /** Cambia la relación con la que se acotan los perfiles buscados. */
    fun onRelationshipChange(relationship: String) {
        _uiState.update { it.copy(relationship = relationship) }
    }

    /** Cambia el criterio con el que se ordenan los resultados. */
    fun onSortChange(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy) }
    }

    /** Restablece los filtros y conserva el texto de la búsqueda general. */
    fun onClearFilters() {
        _uiState.update { state ->
            SearchProfileState(searchText = state.searchText)
        }
    }
}
