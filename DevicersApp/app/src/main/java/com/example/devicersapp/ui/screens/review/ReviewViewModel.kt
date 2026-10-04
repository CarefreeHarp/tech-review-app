package com.example.devicersapp.ui.screens.review

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.UsersRepository
import com.example.devicersapp.ui.mappers.toReviewContent
import com.example.devicersapp.ui.mappers.toProductContent
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
    private val users: UsersRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState: StateFlow<ReviewState> = _uiState

    /** Carga la reseña solicitada y el producto asociado. */
    private var loadJob: Job? = null

    fun loadReview(reviewId: Int) {
        loadJob?.cancel()
        _uiState.value = ReviewState(loading = true)
        loadJob = viewModelScope.launch {
            try {
                val review = reviews.getReviewById(reviewId)
                val product = products.getProductById(review.articleId)
                val author = users.getUserById(review.userId)
                _uiState.value = ReviewState(
                    product = product.toProductContent(),
                    review = review.copy(article = product, user = author).toReviewContent()
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.value = ReviewState(error = "No se pudo cargar la reseña. Intenta nuevamente.")
            }
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
