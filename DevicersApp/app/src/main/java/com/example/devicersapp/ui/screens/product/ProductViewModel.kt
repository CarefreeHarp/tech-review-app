package com.example.devicersapp.ui.screens.product

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import java.net.ConnectException
import java.net.SocketTimeoutException

import android.util.Log

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProductState())
    val uiState: StateFlow<ProductState> = _uiState

    fun loadProduct(productId: Int) {

        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val productResult =
                productRepository.getProductById(productId)

            if (productResult.isSuccess) {

                val product = productResult.getOrNull()

                val reviewsResult =
                    reviewRepository.getReviewsByProduct(productId)

                val reviews =
                    if (reviewsResult.isSuccess) {
                        reviewsResult.getOrNull() ?: emptyList()
                    } else {
                        emptyList()
                    }

                _uiState.update { currentState ->
                    currentState.copy(
                        product = product,
                        reviews = reviews,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } else {

                val exception = productResult.exceptionOrNull()

                val message = when (exception) {

                    is ConnectException ->
                        "No fue posible conectarse con el servidor."

                    is SocketTimeoutException ->
                        "El servidor está tardando demasiado en responder."

                    else ->
                        "No fue posible cargar la información del producto."
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
}