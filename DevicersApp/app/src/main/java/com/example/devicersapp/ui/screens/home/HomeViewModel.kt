package com.example.devicersapp.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/** Conserva y modifica el estado de la pantalla principal. */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        loadFeed()
    }

    /** Carga desde el backend las reseñas del feed con su artículo, autor y conteos. */
    private fun loadFeed() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true, errorMessageResId = null)
        }

        viewModelScope.launch {
            reviewRepository.getFeedReviews()
                .onSuccess { feedReviews ->
                    _uiState.update { currentState ->
                        currentState.copy(feedReviews = feedReviews, isLoading = false)
                    }
                }
                .onFailure { error ->
                    // Sin conexión se sugiere revisar la red; un error HTTP se atribuye al servidor.
                    val messageResId = if (error is IOException) {
                        R.string.home_feed_error_connection
                    } else {
                        R.string.home_feed_error_server
                    }

                    _uiState.update { currentState ->
                        currentState.copy(isLoading = false, errorMessageResId = messageResId)
                    }
                }
        }
    }

    /** Vuelve a solicitar el feed después de un error de carga. */
    fun onRetryClick() {
        loadFeed()
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
