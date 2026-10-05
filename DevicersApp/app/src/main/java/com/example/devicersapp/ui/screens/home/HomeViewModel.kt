package com.example.devicersapp.ui.screens.home

import com.example.devicersapp.domain.usecase.ReviewFeedUseCase
import com.example.devicersapp.data.repository.FollowRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.R
import com.example.devicersapp.core.config.CURRENT_USER_ID
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

/** Conserva y modifica el estado de la pantalla principal. */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val feedUseCase: ReviewFeedUseCase,
    private val follows: FollowRepository
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
            try {
                val (feed, follows) = coroutineScope {
                    val feed = async { feedUseCase.getFeedReviews().getOrThrow() }
                    val follows = async { follows.getFollows().filter { it.followerId == CURRENT_USER_ID }.map { it.followedId }.toSet() }
                    feed.await() to follows.await()
                }
                _uiState.update { current ->
                    current.copy(
                        allFeedReviews = feed,
                        followedAuthorIds = follows,
                        feedReviews = if (current.isForYouSelected) feed else feed.filter { it.authorId in follows },
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

    /** Selecciona la sección "Siguiendo". */
    fun onFollowingClick() {
        _uiState.update { currentState ->
            currentState.copy(
                isForYouSelected = false,
                feedReviews = currentState.allFeedReviews.filter { it.authorId in currentState.followedAuthorIds }
            )
        }
    }
}
