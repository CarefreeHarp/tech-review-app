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
    private val reviews: ReviewRepository,
    private val follows: FollowRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileSearchResultsState())
    val uiState = _uiState.asStateFlow()
    private var profiles: List<ProfileSearchResultContent> = emptyList()
    init { loadResults() }
    /** Carga la identidad bajo demanda y conserva los resultados solo en el estado de esta pantalla. */
    fun loadResults() {
        if (_uiState.value.loading || _uiState.value.followsInProgress.isNotEmpty()) return
        _uiState.update { it.copy(loading = true, error = null, followError = null) }
        viewModelScope.launch {
            try {
                val currentId = users.getAuthenticatedUser().id
                val followedIds = follows.getFollowedUserIds(currentId)
                val counts = reviews.getReviews().getOrThrow().filter { it.isActive }.groupingBy { it.userId }.eachCount()
                profiles = users.getUsers(currentId).filter { it.isActive }
                    .map { it.toSearchContent(counts[it.id] ?: 0) }
                _uiState.update { it.copy(results = filteredProfiles(it.searchText), followedUserIds = followedIds, loading = false) }
            } catch (cancelled: CancellationException) { throw cancelled
            } catch (error: Exception) { _uiState.update { it.copy(loading = false, error = R.string.profile_search_load_error) } }
        }
    }
    /** Actualiza el filtro visible de la lista. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText, results = filteredProfiles(searchText)) }
    }

    /** Actualiza el seguimiento de inmediato y restaura la relación original si falla la persistencia. */
    fun onFollowClick(profileId: String) {
        val id = profileId.toIntOrNull()?.takeIf { it > 0 } ?: return
        val original = _uiState.value
        if (original.loading || id in original.followsInProgress || profiles.none { it.id == profileId }) return
        val wasFollowed = id in original.followedUserIds
        val following = !wasFollowed
        _uiState.update { it.copy(
            followedUserIds = if (following) it.followedUserIds + id else it.followedUserIds - id,
            followsInProgress = it.followsInProgress + id, followError = null
        ) }
        viewModelScope.launch {
            try {
                val actor = users.getAuthenticatedUser()
                val status = follows.setFollowing(actor.id, id, following).getOrThrow()
                _uiState.update { it.copy(
                    followedUserIds = if (status.isFollowing) it.followedUserIds + id else it.followedUserIds - id,
                    followsInProgress = it.followsInProgress - id
                ) }
            } catch (cancelled: CancellationException) { throw cancelled
            } catch (error: Exception) {
                _uiState.update { it.copy(
                    followedUserIds = if (wasFollowed) it.followedUserIds + id else it.followedUserIds - id,
                    followsInProgress = it.followsInProgress - id, followError = R.string.profile_follow_error
                ) }
            }
        }
    }

    private fun filteredProfiles(text: String): List<ProfileSearchResultContent> = profiles.filter {
        it.username.orEmpty().contains(text.trim(), ignoreCase = true)
    }
}
