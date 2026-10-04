package com.example.devicersapp.ui.screens.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Obtiene y conserva el contenido y las acciones del detalle de una reseña.
 */
@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReviewState())
    val uiState: StateFlow<ReviewState> = _uiState

    /**
     * Carga la reseña solicitada desde el backend.
     */
    fun loadReview(reviewId: Int) {

        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val reviewResult =
                reviewRepository.getReviewById(reviewId)

            if (reviewResult.isSuccess) {

                val review = reviewResult.getOrNull()

                if (review != null) {

                    val productResult =
                        productRepository.getProductById(
                            review.articleId
                        )

                    val product =
                        if (productResult.isSuccess) {
                            productResult.getOrNull()
                        } else {
                            null
                        }

                    _uiState.update { currentState ->
                        currentState.copy(
                            review = review,
                            product = product,
                            replies = emptyList(),
                            replyText = "",
                            expandedReplies = emptyMap(),
                            isLoading = false,
                            errorMessage = null
                        )
                    }

                } else {

                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            errorMessage = "No se encontró la reseña."
                        )
                    }
                }

            } else {

                val exception =
                    reviewResult.exceptionOrNull()

                val message = when (exception) {

                    is ConnectException ->
                        "No fue posible conectarse con el servidor."

                    is SocketTimeoutException ->
                        "El servidor está tardando demasiado en responder."

                    else ->
                        "No fue posible cargar la reseña."
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        errorMessage = message
                    )
                }
            }
        }
    }

    fun onReplyTextChange(replyText: String) {
        _uiState.update { currentState ->
            currentState.copy(
                replyText = replyText
            )
        }
    }

    fun onViewAnswers(replyIndex: Int) {
        _uiState.update { currentState ->

            val isExpanded =
                currentState.expandedReplies[replyIndex] == true

            currentState.copy(
                expandedReplies =
                    currentState.expandedReplies +
                            (replyIndex to !isExpanded)
            )
        }
    }

    fun clearReplyText() {
        _uiState.update { currentState ->
            currentState.copy(
                replyText = ""
            )
        }
    }
}