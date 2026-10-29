package com.example.devicersapp.ui.screens.rate_product

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.models.ReviewDraft
import com.example.devicersapp.ui.models.toProductContent
import com.example.devicersapp.R
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva el producto calificado y el contenido que la persona escribe en su reseña. */
@HiltViewModel
class RateProductViewModel @Inject constructor(
    private val products: ProductRepository,
    private val reviews: ReviewRepository,
    private val users: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RateProductState())
    val uiState: StateFlow<RateProductState> = _uiState

    private var loadJob: Job? = null

    /** Carga el producto solicitado; un ID inválido conserva la respuesta de error de la pantalla. */
    fun loadProduct(productId: Int?) {
        if (_uiState.value.product?.id == productId) return
        loadJob?.cancel()
        _uiState.update { RateProductState(loading = true) }
        loadJob = viewModelScope.launch {
            try {
                require(productId != null && productId > 0)
                val product = products.getProductById(productId).getOrThrow().toProductContent()
                _uiState.update { RateProductState(product = product) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { RateProductState(error = R.string.rate_product_load_error) }
            }
        }
    }

    /** Conserva el borrador si falla la red; evita envíos simultáneos. */
    fun publish() {
        val draft = _uiState.value
        if (draft.saving || draft.published) return
        val productId = draft.product?.id ?: return
        if (draft.rating !in 1..5 || draft.experience.isBlank()) {
            _uiState.update { it.copy(error = R.string.edit_review_validation_error) }
            return
        }
        _uiState.update { it.copy(saving = true, error = null) }
        viewModelScope.launch {
            try {
                val userId = users.getAuthenticatedUser().id
                val body = buildString {
                    append(draft.experience.trim())
                    if (draft.advantage.isNotBlank()) append("\n\nVentajas: ${draft.advantage.trim()}")
                    if (draft.disadvantage.isNotBlank()) append("\n\nDesventajas: ${draft.disadvantage.trim()}")
                }
                reviews.createReview(
                    ReviewDraft(
                        userId = userId,
                        articleId = productId,
                        rating = draft.rating,
                        body = body,
                        title = draft.title.trim()
                    )
                ).getOrThrow()
                _uiState.update { it.copy(saving = false, published = true) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { it.copy(saving = false, error = R.string.rate_product_publish_error) }
            }
        }
    }

    /** Actualiza la cantidad de estrellas seleccionadas para el producto. */
    fun onRatingChange(rating: Int) {
        if (_uiState.value.saving) return
        _uiState.update { it.copy(rating = rating) }
    }

    /** Actualiza el título con el que se encabeza la reseña. */
    fun onTitleChange(title: String) {
        if (_uiState.value.saving) return
        _uiState.update { it.copy(title = title) }
    }

    /** Actualiza el relato de la experiencia con el producto. */
    fun onExperienceChange(experience: String) {
        if (_uiState.value.saving) return
        _uiState.update { it.copy(experience = experience) }
    }

    /** Actualiza la ventaja destacada del producto. */
    fun onAdvantageChange(advantage: String) {
        if (_uiState.value.saving) return
        _uiState.update { it.copy(advantage = advantage) }
    }

    /** Actualiza la desventaja destacada del producto. */
    fun onDisadvantageChange(disadvantage: String) {
        if (_uiState.value.saving) return
        _uiState.update { it.copy(disadvantage = disadvantage) }
    }

    /** Solicita calificar otro producto distinto al que se muestra. */
    fun onChangeProduct() {
        // TODO: Implementar la selección de otro producto.
    }
}
