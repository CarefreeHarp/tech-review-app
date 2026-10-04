package com.example.devicersapp.ui.screens.found_products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Conserva y modifica el estado de la pantalla de productos encontrados.
 */
@HiltViewModel
class FoundProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoundProductsState())
    val uiState: StateFlow<FoundProductsState> = _uiState

    private var products: List<ProductDto> = emptyList()

//    init {
//        loadProducts()
//    }

    /**
     * Carga los productos disponibles desde el backend.
     */
    fun loadProducts(
        productName: String,
        category: String,
        minimumRating: Float,
        sortBy: String
    ) {

        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val result = productRepository.getProducts()

            if (result.isSuccess) {

                val allProducts =
                    result.getOrNull() ?: emptyList()

                var filteredProducts =
                    allProducts.filter { product ->

                        val matchesName =
                            productName.isBlank() ||
                                    product.name.contains(
                                        productName,
                                        ignoreCase = true
                                    )

                        val matchesCategory =
                            when (category) {

                                "all" ->
                                    true

                                "cellphones" ->
                                    product.categoryId == 2

                                "audio" ->
                                    product.categoryId == 1 ||
                                            product.categoryId == 3

                                else ->
                                    true
                            }

                        val matchesRating =
                            if (product.reviews.isNullOrEmpty()) {

                                true

                            } else {

                                val average =
                                    product.reviews
                                        .map { review ->
                                            review.rating
                                        }
                                        .average()

                                average >= minimumRating
                            }

                        matchesName &&
                                matchesCategory &&
                                matchesRating
                    }

                filteredProducts =
                    when (sortBy) {

                        "recent" ->
                            filteredProducts.sortedByDescending {
                                it.createdAt
                            }

                        else ->
                            filteredProducts
                    }

                products = filteredProducts

                _uiState.update { currentState ->
                    currentState.copy(
                        results = filteredProducts,
                        searchText = productName,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } else {

                val exception =
                    result.exceptionOrNull()

                val message =
                    when (exception) {

                        is ConnectException ->
                            "No fue posible conectarse con el servidor."

                        is SocketTimeoutException ->
                            "El servidor está tardando demasiado en responder."

                        else ->
                            "Ocurrió un error al cargar los productos."
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

//    fun loadProducts() {
//
//        viewModelScope.launch {
//
//            _uiState.update { currentState ->
//                currentState.copy(
//                    isLoading = true,
//                    errorMessage = null
//                )
//            }
//
//            val result = productRepository.getProducts()
//
//            if (result.isSuccess) {
//
//                products = result.getOrNull() ?: emptyList()
//
//                _uiState.update { currentState ->
//                    currentState.copy(
//                        results = products,
//                        isLoading = false,
//                        errorMessage = null
//                    )
//                }
//
//            } else {
//
//                val exception = result.exceptionOrNull()
//
//                val message = when (exception) {
//
//                    is ConnectException ->
//                        "No fue posible conectarse con el servidor."
//
//                    is SocketTimeoutException ->
//                        "El servidor está tardando demasiado en responder."
//
//                    else ->
//                        "Ocurrió un error al cargar los productos."
//                }
//
//                _uiState.update { currentState ->
//                    currentState.copy(
//                        isLoading = false,
//                        errorMessage = message
//                    )
//                }
//            }
//        }
//    }

    /**
     * Actualiza el texto escrito y filtra los productos.
     */
    fun onSearchTextChange(searchText: String) {

        val filteredProducts =
            if (searchText.isBlank()) {

                products

            } else {

                products.filter { product ->

                    product.name.contains(
                        searchText,
                        ignoreCase = true
                    ) ||
                            product.model?.contains(
                                searchText,
                                ignoreCase = true
                            ) == true ||
                            product.description?.contains(
                                searchText,
                                ignoreCase = true
                            ) == true
                }
            }

        _uiState.update { currentState ->
            currentState.copy(
                searchText = searchText,
                results = filteredProducts
            )
        }
    }
}