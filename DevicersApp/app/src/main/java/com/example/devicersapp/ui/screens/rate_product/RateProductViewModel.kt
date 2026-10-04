package com.example.devicersapp.ui.screens.rate_product

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.data.dto.CreateReviewRequestDto
import com.example.devicersapp.ui.mappers.toProductContent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProductProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva el producto calificado y el contenido que la persona escribe en su reseña. */
@HiltViewModel
class RateProductViewModel @Inject constructor(private val products: ProductRepository, private val reviews: ReviewRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RateProductState())
    val uiState: StateFlow<RateProductState> = _uiState

    /** Carga el producto solicitado o el predeterminado cuando no llega un identificador. */
    private var loadJob: Job? = null
    fun loadProduct(productId: Int?) {
        if (_uiState.value.product?.id == productId) return
        loadJob?.cancel()
        _uiState.value = RateProductState(loading = true)
        loadJob = viewModelScope.launch {
            try {
                require(productId != null && productId > 0)
                val product = products.getProductById(productId).getOrThrow().toProductContent()
                _uiState.value = RateProductState(product = product)
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.value = RateProductState(error = "No se pudo cargar el producto. Elige uno del catálogo actualizado.") }
        }
    }

    /** Conserva el borrador si falla la red; evita envíos simultáneos. */
    fun publish() {
        val draft = _uiState.value
        if (draft.saving || draft.published) return
        val productId = draft.product?.id ?: return
        if (draft.rating !in 1..5 || draft.experience.isBlank()) {
            _uiState.update { it.copy(error = "Selecciona una calificación y escribe tu experiencia.") }; return
        }
        _uiState.update { it.copy(saving = true, error = null) }
        viewModelScope.launch {
            try {
                val body = buildString {
                    append(draft.experience.trim())
                    if (draft.advantage.isNotBlank()) append("\n\nVentajas: ${draft.advantage.trim()}")
                    if (draft.disadvantage.isNotBlank()) append("\n\nDesventajas: ${draft.disadvantage.trim()}")
                }
                reviews.createReview(CreateReviewRequestDto(CURRENT_USER_ID, productId, draft.rating, body, draft.title.trim())).getOrThrow()
                _uiState.update { it.copy(saving = false, published = true) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.update { it.copy(saving = false, error = "No se pudo publicar. Tu borrador se conserva; intenta de nuevo.") } }
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
