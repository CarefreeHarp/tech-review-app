package com.example.devicersapp.ui.screens.rate_product

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProductProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva el producto calificado y el contenido que la persona escribe en su reseña. */
@HiltViewModel
class RateProductViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(RateProductState())
    val uiState: StateFlow<RateProductState> = _uiState

    /** Carga el producto solicitado o el predeterminado cuando no llega un identificador. */
    fun loadProduct(productNameResId: Int?) {
        val product = productNameResId?.let(LocalProductProvider::getProductByNameResId)
            ?: LocalProductProvider.product

        _uiState.update { it.copy(product = product) }
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
