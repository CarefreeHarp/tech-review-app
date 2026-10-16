package com.example.devicersapp.ui.screens.home

import com.example.devicersapp.data.repository.ReviewRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.R
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/** Conserva y modifica el estado de la pantalla principal. */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val articleReviews: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    init {
        loadFeed()
    }

    /** Carga desde la fuente seleccionada las reseñas del feed con su artículo, autor y conteos. */
    private fun loadFeed() {
        _uiState.update { currentState ->
            currentState.copy(isLoading = true, errorMessageResId = null)
        }

        viewModelScope.launch {
            try {
                val feed = articleReviews.getFeedReviews().getOrThrow()
                _uiState.update { current ->
                    current.copy(
                        allFeedReviews = feed,
                        feedReviews = if (current.isForYouSelected) feed else emptyList(),
                        isLoading = false
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessageResId = if (e is IOException) R.string.home_feed_error_connection else R.string.home_feed_error_server) }
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
                isForYouSelected = true,
                feedReviews = currentState.allFeedReviews
            )
        }
    }

    /** Selecciona visualmente "Siguiendo" sin cargar reseñas de cuentas seguidas. */
    fun onFollowingClick() {
        _uiState.update { currentState ->
            currentState.copy(
                isForYouSelected = false,
                feedReviews = emptyList()
            )
        }
    }
}
