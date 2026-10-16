package com.example.devicersapp.ui.screens.profile_saved_reviews
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.models.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
/** Consulta los guardados del usuario actual sin una copia global de su sesión. */
@HiltViewModel
class ProfileSavedReviewsViewModel @Inject constructor(
    private val users: UserRepository,
    private val bookmarks: ReviewBookmarkRepository,
    private val reviews: ReviewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileSavedReviewsState())
    val uiState: StateFlow<ProfileSavedReviewsState> = _uiState
    private var loadJob: Job? = null
    init { loadSavedReviews() }
    /** Actualiza el perfil y los detalles de las reseñas guardadas desde la implementación seleccionada. */
    fun loadSavedReviews() {
        loadJob?.cancel()
        _uiState.update { it.copy(loading = true, errorMessageResId = null) }
        loadJob = viewModelScope.launch {
            try {
                val user = users.getAuthenticatedUser()
                val profile = users.getProfile(user.id)
                val saved = bookmarks.getReviewBookmarks(user.id).map {
                    reviews.getReviewDetail(it.reviewId).getOrThrow().toReviewContent()
                }
                _uiState.update { it.copy(profile = user.toProfileContent(profile.reviews.size,
                    profile.followerCount, profile.followingCount), profileImageUrl = user.profileImageUrl,
                    email = user.email, savedReviews = saved, loading = false) }
            } catch (cancelled: CancellationException) { throw cancelled
            } catch (error: Exception) { _uiState.update { it.copy(loading = false, errorMessageResId = R.string.profile_saved_load_error) } }
        }
    }
    /** Selecciona visualmente la pestaña de reseñas. */
    fun onReviewsSelected() { _uiState.update { it.copy(isReviewsSelected = true) } }
    /** Selecciona visualmente la pestaña de guardados. */
    fun onSavedSelected() { _uiState.update { it.copy(isReviewsSelected = false) } }
}
