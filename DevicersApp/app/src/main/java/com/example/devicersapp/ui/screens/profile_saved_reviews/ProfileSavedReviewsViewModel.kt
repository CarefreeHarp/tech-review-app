package com.example.devicersapp.ui.screens.profile_saved_reviews

import androidx.lifecycle.ViewModel
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Prepara la lista local de reseñas que la persona guardó. */
class ProfileSavedReviewsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProfileSavedReviewsState(profile = LocalProfileProvider.profile)
    )
    val uiState: StateFlow<ProfileSavedReviewsState> = _uiState

    init {
        loadSavedReviews()
    }

    /** Carga una única vez las reseñas guardadas desde los proveedores locales. */
    private fun loadSavedReviews() {
        _uiState.update {
            ProfileSavedReviewsState(
                profile = LocalProfileProvider.profile,
                savedReviews = LocalProfileProvider.savedReviews.mapNotNull { savedReview ->
                    LocalReviewProvider.findById(savedReview.reviewId)
                }
            )
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
