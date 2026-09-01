package com.example.devicersapp.ui.screens.review

import androidx.lifecycle.ViewModel
import com.example.devicersapp.data.local.LocalReviewProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Obtiene y conserva el contenido y las acciones del detalle de una reseña. */
class ReviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState: StateFlow<ReviewState> = _uiState

    /** Carga la reseña solicitada y el producto asociado. */
    fun loadReview(reviewId: Int) {
        val review = LocalReviewProvider.findById(reviewId)
        val product = LocalReviewProvider.findProductByReviewId(reviewId)

        _uiState.update { currentState ->
            currentState.copy(
                product = product,
                review = review,
                replies = review?.comments.orEmpty(),
                replyText = "",
                expandedReplies = emptyMap()
            )
        }
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