package com.example.devicersapp.ui.screens.home

import com.example.devicersapp.data.repository.ReviewLikeRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.FollowRepository
import kotlinx.coroutines.Job
import com.example.devicersapp.data.repository.UserRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.FeedReviewContent
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
    private val articleReviews: ReviewRepository,
    private val users: UserRepository,
    private val reviewLikes: ReviewLikeRepository,
    private val follows: FollowRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())
    val uiState: StateFlow<HomeState> = _uiState

    /** ID numérico del usuario autenticado; identifica sus likes y no se muestra en pantalla. */
    private var currentUserId: Int? = null

    private var loadJob: Job? = null

    /** Reconsulta la pestaña activa cada vez que Home vuelve a primer plano. */
    fun onResume() { loadFeed() }

    /** Carga desde la fuente seleccionada las reseñas del feed con su artículo, autor, conteos y likes propios. */
    private fun loadFeed() {
        loadJob?.cancel()
        val forYou = _uiState.value.isForYouSelected
        _uiState.update { currentState ->
            currentState.copy(isLoading = true, errorMessageResId = null)
        }

        loadJob = viewModelScope.launch {
            try {
                val userId = users.getAuthenticatedUser().id
                currentUserId = userId
                val authors = if (forYou) null else follows.getFollowedUserIds(userId)
                val feed = articleReviews.getFeedReviews(userId, authors).getOrThrow()
                _uiState.update { current ->
                    current.copy(
                        allFeedReviews = feed,
                        feedReviews = feed,
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

    /**
     * Da o quita el like de una reseña. La tarjeta cambia de inmediato y después adopta el estado
     * confirmado por la fuente de datos, o recupera el anterior si la escritura falla.
     */
    fun onLikeClick(reviewId: Int) {
        val userId = currentUserId ?: return
        val current = _uiState.value
        if (reviewId in current.pendingLikeReviewIds) return
        val original = current.allFeedReviews.firstOrNull { it.reviewId == reviewId } ?: return

        updateLike(
            reviewId = reviewId,
            isLiked = !original.isLiked,
            likes = if (original.isLiked) original.likes - 1 else original.likes + 1,
            isPending = true
        )
        viewModelScope.launch {
            reviewLikes.sendReviewLike(reviewId, userId)
                .onSuccess { status -> updateLike(reviewId, status.isLiked, status.likes, isPending = false) }
                .onFailure { updateLike(reviewId, original.isLiked, original.likes, isPending = false) }
        }
    }

    /** Aplica el estado de like a la reseña tanto en el feed completo como en la pestaña visible. */
    private fun updateLike(reviewId: Int, isLiked: Boolean, likes: Int, isPending: Boolean) {
        fun List<FeedReviewContent>.withLike() =
            map { if (it.reviewId == reviewId) it.copy(isLiked = isLiked, likes = likes) else it }

        _uiState.update { current ->
            current.copy(
                allFeedReviews = current.allFeedReviews.withLike(),
                feedReviews = current.feedReviews.withLike(),
                pendingLikeReviewIds = if (isPending) current.pendingLikeReviewIds + reviewId
                    else current.pendingLikeReviewIds - reviewId
            )
        }
    }

    /** Selecciona Para ti y vuelve a consultar sus reseñas. */
    fun onForYouClick() {
        _uiState.update { it.copy(isForYouSelected = true, feedReviews = emptyList()) }
        loadFeed()
    }

    /** Consulta los autores seguidos y únicamente las reseñas de esas cuentas. */
    fun onFollowingClick() {
        _uiState.update { it.copy(isForYouSelected = false, feedReviews = emptyList()) }
        loadFeed()
    }
}
