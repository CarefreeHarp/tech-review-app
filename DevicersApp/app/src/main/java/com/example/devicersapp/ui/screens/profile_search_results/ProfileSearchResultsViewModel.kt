package com.example.devicersapp.ui.screens.profile_search_results
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta perfiles a partir del usuario actual de Firebase Auth. */
@HiltViewModel
class ProfileSearchResultsViewModel @Inject constructor(
    private val users: UserRepository,
    private val reviews: ReviewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileSearchResultsState())
    val uiState = _uiState.asStateFlow()
    private var profiles: List<ProfileSearchResultContent> = emptyList()
    init { loadResults() }
    /** Carga la identidad bajo demanda y conserva los resultados solo en el estado de esta pantalla. */
    fun loadResults() {
        if (_uiState.value.loading) return
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val currentId = users.getAuthenticatedUser().id
                val counts = reviews.getReviews().getOrThrow().filter { it.isActive }.groupingBy { it.userId }.eachCount()
                profiles = users.getUsers(currentId).filter { it.isActive }
                    .map { it.toSearchContent(counts[it.id] ?: 0) }
                _uiState.update { it.copy(results = filteredProfiles(it.searchText), loading = false) }
            } catch (cancelled: CancellationException) { throw cancelled
            } catch (error: Exception) { _uiState.update { it.copy(loading = false, error = R.string.profile_search_load_error) } }
        }
    }
    /** Actualiza el filtro visible de la lista. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText, results = filteredProfiles(searchText)) }
    }

    private fun filteredProfiles(text: String): List<ProfileSearchResultContent> = profiles.filter {
        it.username.orEmpty().contains(text.trim(), ignoreCase = true)
    }
}
