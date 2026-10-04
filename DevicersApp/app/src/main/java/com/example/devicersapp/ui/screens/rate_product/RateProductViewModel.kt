package com.example.devicersapp.ui.screens.rate_product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject
import com.example.devicersapp.data.dto.CreateReviewRequestDto
import com.example.devicersapp.data.repository.ReviewRepository

/** Conserva el producto calificado y el contenido que la persona escribe en su reseña. */
@HiltViewModel
class RateProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RateProductState())
    val uiState: StateFlow<RateProductState> = _uiState

    /**
     * Carga el producto seleccionado desde el backend.
     */
    fun loadProduct(productId: Int) {

        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val result =
                productRepository.getProductById(productId)

            if (result.isSuccess) {

                val product = result.getOrNull()

                _uiState.update { currentState ->
                    currentState.copy(
                        product = product,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } else {

                val exception = result.exceptionOrNull()

                val message = when (exception) {

                    is ConnectException ->
                        "No fue posible conectarse con el servidor."

                    is SocketTimeoutException ->
                        "El servidor está tardando demasiado en responder."

                    else ->
                        "No fue posible cargar el producto."
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

    fun publishReview() {

        val state = _uiState.value
        val product = state.product ?: return

        if (state.rating == 0) {
            _uiState.update {
                it.copy(
                    publishError = "Selecciona una calificación."
                )
            }
            return
        }

        if (state.experience.isBlank()) {
            _uiState.update {
                it.copy(
                    publishError = "Escribe tu experiencia con el producto."
                )
            }
            return
        }

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    isPublishing = true,
                    publishError = null
                )
            }

            var body = state.experience

            if (state.advantage.isNotBlank()) {
                body += "\n\nVentaja: ${state.advantage}"
            }

            if (state.disadvantage.isNotBlank()) {
                body += "\n\nDesventaja: ${state.disadvantage}"
            }

            val request = CreateReviewRequestDto(
                userId = 1,
                articleId = product.id,
                rating = state.rating,
                body = body,
                title = state.title.ifBlank { null }
            )

            val result =
                reviewRepository.createReview(request)

            if (result.isSuccess) {

                _uiState.update {
                    it.copy(
                        isPublishing = false,
                        publishError = null,
                        publishedSuccessfully = true
                    )
                }

            } else {

                val exception = result.exceptionOrNull()

                val message = when (exception) {

                    is java.net.ConnectException ->
                        "No fue posible conectarse con el servidor."

                    is java.net.SocketTimeoutException ->
                        "El servidor está tardando demasiado en responder."

                    else ->
                        "No fue posible publicar la reseña."
                }

                _uiState.update {
                    it.copy(
                        isPublishing = false,
                        publishError = message
                    )
                }
            }
        }
    }

    /** Actualiza la cantidad de estrellas seleccionadas para el producto. */
    fun onRatingChange(rating: Int) {
        _uiState.update { it.copy(rating = rating) }
    }

    /** Actualiza el título con el que se encabeza la reseña. */
    fun onTitleChange(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    /** Actualiza el relato de la experiencia con el producto. */
    fun onExperienceChange(experience: String) {
        _uiState.update { it.copy(experience = experience) }
    }

    /** Actualiza la ventaja destacada del producto. */
    fun onAdvantageChange(advantage: String) {
        _uiState.update { it.copy(advantage = advantage) }
    }

    /** Actualiza la desventaja destacada del producto. */
    fun onDisadvantageChange(disadvantage: String) {
        _uiState.update { it.copy(disadvantage = disadvantage) }
    }

    /** Solicita calificar otro producto distinto al que se muestra. */
    fun onChangeProduct() {
        // TODO: Implementar la selección de otro producto.
    }
}
