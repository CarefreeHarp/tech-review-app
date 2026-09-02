package com.example.devicersapp.ui.screens.home

import androidx.lifecycle.ViewModel
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Conserva y modifica el estado de la pantalla principal. */
class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        loadFeed()
    }

    /** Carga las reseñas del feed junto con la información de sus autores. */
    private fun loadFeed() {
        val feedItems = LocalReviewProvider.reviews.map { review ->
            HomeFeedItem(
                review = review,
                author = requireNotNull(
                    LocalProfileProvider.getProfileById(review.authorId)
                )
            )
        }

        _uiState.update { currentState ->
            currentState.copy(
                feedItems = feedItems
            )
        }
    }

    /** Selecciona la sección "Para ti". */
    fun onForYouClick() {
        _uiState.update { currentState ->
            currentState.copy(
                isForYouSelected = true
            )
        }
    }

    /** Selecciona la sección "Siguiendo". */
    fun onFollowingClick() {
        _uiState.update { currentState ->
            currentState.copy(
                isForYouSelected = false
            )
        }
    }
}