package com.example.devicersapp.ui.screens.profile_saved_reviews

import com.example.devicersapp.domain.usecase.SavedReviewsUseCase
import com.example.devicersapp.domain.usecase.ProfileContentUseCase

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.core.config.CURRENT_USER_ID
import com.example.devicersapp.data.dto.toReviewContent
import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta los guardados del usuario temporal y conserva la identidad de Firebase. */
@HiltViewModel
class ProfileSavedReviewsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val profileContent: ProfileContentUseCase,
    private val savedReviews: SavedReviewsUseCase,
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileSavedReviewsState())
    val uiState: StateFlow<ProfileSavedReviewsState> = _uiState
    private var loadJob: Job? = null

    init {
        loadSavedReviews()
    }

    /** Refresca el perfil y las reseñas realmente guardadas en el backend. */
    fun loadSavedReviews() {
        loadJob?.cancel()
        val authenticatedUser = authRepository.currentUser
        _uiState.update {
            it.copy(email = authenticatedUser?.email.orEmpty(),
                profileImageUrl = authenticatedUser?.photoUrl?.toString(),
                loading = true, errorMessageResId = null,
                profile = null, savedReviews = emptyList())
        }
        loadJob = viewModelScope.launch {
            try {
                val (profile, saved) = coroutineScope {
                    val profile = async {
                        val own = reviewRepository.getReviewsByUser(CURRENT_USER_ID).getOrThrow()
                            .count { it.isActive && it.userId == CURRENT_USER_ID }
                        profileContent.getProfileContent(CURRENT_USER_ID, own)
                    }
                    val saved = async { savedReviews.getSavedReviews(CURRENT_USER_ID).getOrThrow() }
                    profile.await() to saved.await()
                }
                _uiState.update {
                    it.copy(profile = profile.copy(imageUrl = null),
                        savedReviews = saved.map { review -> review.toReviewContent() }, loading = false)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(loading = false, errorMessageResId = R.string.profile_saved_load_error) }
            }
        }
    }

    /** Publica la selección de la pestaña de reseñas antes de navegar a ella. */
    fun onReviewsSelected() {
        _uiState.update { it.copy(isReviewsSelected = true) }
    }

    /** Mantiene activa la pestaña de elementos guardados. */
    fun onSavedSelected() {
        _uiState.update { it.copy(isReviewsSelected = false) }
    }
}
