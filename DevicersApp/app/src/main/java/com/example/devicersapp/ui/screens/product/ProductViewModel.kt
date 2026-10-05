package com.example.devicersapp.ui.screens.product

import com.example.devicersapp.domain.usecase.ReviewContentUseCase

import com.example.devicersapp.R
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta el producto y todas las reseñas visibles con sus autores y datos asociados. */
@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository,
    private val reviewContent: ReviewContentUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProductState())
    val uiState: StateFlow<ProductState> = _uiState
    private var loadJob: Job? = null

    /** Renueva el detalle; una consulta fallida no se presenta como ausencia de reseñas. */
    fun loadProduct(productId: Int) {
        loadJob?.cancel()
        _uiState.update { ProductState(isLoading = true) }
        loadJob = viewModelScope.launch {
            try {
                val product = productRepository.getProductById(productId).getOrThrow()
                val records = reviewRepository.getReviewsByProduct(productId).getOrThrow()
                    .filter { it.articleId == productId && it.isActive }
                val reviews = reviewContent.getReviewContents(records)
                _uiState.update { ProductState(product = product, reviews = reviews) }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                _uiState.update { ProductState(errorMessageResId = R.string.remote_product_load_error) }
            }
        }
    }
}
