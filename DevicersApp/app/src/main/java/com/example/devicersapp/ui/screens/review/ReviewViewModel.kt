package com.example.devicersapp.ui.screens.review

import com.example.devicersapp.data.repository.CommentLikeRepository
import com.example.devicersapp.data.repository.ReviewLikeRepository
import com.example.devicersapp.data.repository.ReviewRepository

import com.example.devicersapp.data.repository.UserRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.devicersapp.ui.models.toReviewContent
import com.example.devicersapp.ui.models.toProductContent
import com.example.devicersapp.R
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Obtiene y conserva el contenido y las acciones del detalle de una reseña. */
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val articleReviews: ReviewRepository,
    private val users: UserRepository,
    private val reviewLikes: ReviewLikeRepository,
    private val commentLikes: CommentLikeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState: StateFlow<ReviewState> = _uiState

    private var loadJob: Job? = null

    /** ID numérico del usuario autenticado; identifica su like y no se muestra en pantalla. */
    private var currentUserId: Int? = null

    /** Carga la reseña solicitada, el producto asociado y si el usuario autenticado ya le dio like. */
    fun loadReview(reviewId: Int) {
        if (_uiState.value.deletionRequested) return
        loadJob?.cancel()
        _uiState.update { ReviewState(loading = true) }
        loadJob = viewModelScope.launch {
            try {
                val currentId = users.getAuthenticatedUser().id
                currentUserId = currentId
                val review = articleReviews.getReviewDetail(reviewId, currentId).getOrThrow()
                val content = review.toReviewContent()
                _uiState.update {
                    ReviewState(
                        product = review.article.toProductContent(),
                        review = content,
                        canManage = review.review.userId == currentId,
                        replies = content.comments
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { ReviewState(error = R.string.review_load_error) }
            }
        }
    }

    /** Abre una reseña del feed local sin confundir su ID con el de la API. */
    fun loadLocalReview(reviewId: Int) {
        loadJob?.cancel()
        loadReview(reviewId)

    }

    /**
     * Da o quita el like de la reseña. El corazón cambia de inmediato y después adopta el estado
     * confirmado por la fuente de datos, o recupera el anterior si la escritura falla.
     */
    fun onLikeClick() {
        val current = _uiState.value
        val original = current.review ?: return
        val userId = currentUserId ?: return
        if (current.likeInProgress) return

        _uiState.update {
            it.copy(
                review = original.copy(
                    isLiked = !original.isLiked,
                    likes = if (original.isLiked) original.likes - 1 else original.likes + 1
                ),
                likeInProgress = true
            )
        }
        viewModelScope.launch {
            val result = reviewLikes.sendReviewLike(original.id, userId)
            _uiState.update { state ->
                // Si la pantalla recargó otra reseña mientras tanto, solo se libera el indicador.
                val shown = state.review?.takeIf { it.id == original.id }
                    ?: return@update state.copy(likeInProgress = false)
                val confirmed = result.fold(
                    onSuccess = { status -> shown.copy(isLiked = status.isLiked, likes = status.likes) },
                    onFailure = { shown.copy(isLiked = original.isLiked, likes = original.likes) }
                )
                state.copy(review = confirmed, likeInProgress = false)
            }
        }
    }

    /** Da o quita el like del comentario con confirmación remota y restauración si falla. */
    fun onCommentLikeClick(commentId: Int) {
        val current = _uiState.value
        val reviewId = current.review?.id ?: return
        val userId = currentUserId ?: return
        val original = current.replies.firstOrNull { it.id == commentId } ?: return
        if (commentId <= 0 || commentId in current.pendingCommentLikeIds) return
        updateCommentLike(commentId, !original.isLiked,
            (original.likes + if (original.isLiked) -1 else 1).coerceAtLeast(0), true)
        viewModelScope.launch {
            val result = commentLikes.sendCommentLike(reviewId, commentId, userId)
            if (_uiState.value.review?.id != reviewId) return@launch
            result.onSuccess { updateCommentLike(commentId, it.isLiked, it.likes, false) }
                .onFailure {
                    updateCommentLike(commentId, original.isLiked, original.likes, false)
                    _uiState.update { it.copy(commentLikeError = R.string.review_comment_like_error) }
                }
        }
    }

    /** Aplica los valores al hilo y a la copia de comentarios del detalle sin alterar otras reacciones. */
    private fun updateCommentLike(commentId: Int, liked: Boolean, likes: Int, pending: Boolean) {
        _uiState.update { state ->
            val replies = state.replies.map { if (it.id == commentId) it.copy(isLiked = liked, likes = likes) else it }
            state.copy(replies = replies, review = state.review?.copy(comments = replies), commentLikeError = null,
                pendingCommentLikeIds = if (pending) state.pendingCommentLikeIds + commentId else state.pendingCommentLikeIds - commentId)
        }
    }

    /** Expone el menú únicamente para la reseña remota de la sesión actual. */
    fun setActionsMenuExpanded(expanded: Boolean) {
        _uiState.update { it.copy(actionsMenuExpanded = expanded && it.canManage && !it.deletionRequested) }
    }

    /** Autoriza la salida inmediata al perfil, que se encargará de completar la eliminación. */
    fun requestDeletion(): Int? {
        val current = _uiState.value
        val review = current.review ?: return null
        if (!current.canManage || current.isLocal ||
            current.deletionRequested) return null
        _uiState.update { it.copy(deletionRequested = true, actionsMenuExpanded = false) }
        return review.id
    }

    /** Actualiza el texto escrito en el compositor de respuestas. */
    fun onReplyTextChange(replyText: String) {
        _uiState.update { currentState ->
            currentState.copy(replyText = replyText)
        }
    }

    /** Alterna la visibilidad de las respuestas asociadas a un comentario. */
    fun onViewAnswers(replyIndex: Int) {
        _uiState.update { currentState ->
            val isExpanded =
                currentState.expandedReplies[replyIndex] == true

            currentState.copy(
                expandedReplies = currentState.expandedReplies +
                        (replyIndex to !isExpanded)
            )
        }
    }

    /** Limpia el compositor después de enviar una respuesta válida. */
    fun clearReplyText() {
        _uiState.update { currentState ->
            currentState.copy(replyText = "")
        }
    }
}
