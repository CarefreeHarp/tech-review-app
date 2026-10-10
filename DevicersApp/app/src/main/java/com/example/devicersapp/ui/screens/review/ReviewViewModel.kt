package com.example.devicersapp.ui.screens.review

import com.example.devicersapp.data.repository.CommentLikeRepository
import com.example.devicersapp.data.repository.CommentRepository
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewLikeRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.UsersRepository

import com.example.devicersapp.data.repository.SessionRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.data.dto.toReviewContent
import com.example.devicersapp.data.dto.toProductContent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Obtiene y conserva el contenido y las acciones del detalle de una reseña. */
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviews: ReviewRepository,
    private val products: ProductRepository,
    private val users: UsersRepository,
    private val comments: CommentRepository,
    private val reviewLikes: ReviewLikeRepository,
    private val commentLikes: CommentLikeRepository,
    private val session: SessionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState: StateFlow<ReviewState> = _uiState

    /** Carga la reseña solicitada y el producto asociado. */
    private var loadJob: Job? = null

    fun loadReview(reviewId: Int) {
        if (_uiState.value.deletionRequested) return
        loadJob?.cancel()
        _uiState.update { ReviewState(loading = true) }
        loadJob = viewModelScope.launch {
            try {
                val review = products.getReviewContents(listOf(reviews.getReviewById(reviewId).getOrThrow()), users, comments, reviewLikes, commentLikes).single()
                val content = review.toReviewContent()
                _uiState.update {
                    ReviewState(
                        product = requireNotNull(review.article).toProductContent(),
                        review = content,
                        canManage = review.userId == session.requireCurrentProfile().id,
                        replies = content.comments
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { ReviewState(error = "No se pudo cargar la reseña. Intenta nuevamente.") }
            }
        }
    }

    /** Abre una reseña del feed local sin confundir su ID con el de la API. */
    fun loadLocalReview(reviewId: Int) {
        loadJob?.cancel()
        val review = LocalReviewProvider.findById(reviewId)
        val product = LocalReviewProvider.findProductByReviewId(reviewId)
        _uiState.update { if (review != null && product != null) {
            ReviewState(product = product, review = review, replies = review.comments, isLocal = true)
        } else {
            ReviewState(error = "No se encontró la reseña.", isLocal = true)
        } }
    }

    /** Expone el menú únicamente para la reseña remota de la sesión actual. */
    fun setActionsMenuExpanded(expanded: Boolean) {
        _uiState.update { it.copy(actionsMenuExpanded = expanded && it.canManage && it.review?.authorId == session.currentProfile.value?.id?.toString() && !it.deletionRequested) }
    }

    /** Autoriza la salida inmediata al perfil, que se encargará de completar la eliminación. */
    fun requestDeletion(): Int? {
        val current = _uiState.value
        val review = current.review ?: return null
        if (!current.canManage || current.isLocal || review.authorId != session.currentProfile.value?.id?.toString() ||
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
