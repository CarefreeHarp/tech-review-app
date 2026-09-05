package com.example.devicersapp.ui.screens.product

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva y carga el estado de la pantalla de detalle de producto. */
@HiltViewModel
class ProductViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProductState())
    val uiState: StateFlow<ProductState> = _uiState

    /**
     * Carga el producto correspondiente al identificador recibido desde navegación.
     */
    fun loadProduct(productNameResId: Int) {
        val product =
            LocalProductProvider.getProductByNameResId(productNameResId)
                ?: LocalProductProvider.product

        _uiState.update { currentState ->
            currentState.copy(
                product = product,
                ratingSummary = LocalProductProvider.ratingSummary,
                reviews = LocalReviewProvider.reviewsForProduct(
                    product.nameResId
                )
            )
        }
    }
}
