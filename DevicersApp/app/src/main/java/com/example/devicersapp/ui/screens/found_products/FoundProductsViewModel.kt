package com.example.devicersapp.ui.screens.found_products

import com.example.devicersapp.data.repository.CategoryRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.ui.models.ProductInfo
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
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FoundProductsState())
    val uiState: StateFlow<FoundProductsState> = _uiState

    private var products: List<ProductInfo> = emptyList()

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

            val result = productRepository.getProducts().mapCatching { products ->
                products to categoryRepository.getCategories().getOrThrow()
            }

            if (result.isSuccess) {

                val (allProducts, categories) = result.getOrThrow()
                val requestedName = when (category) {
                    "cellphones" -> "Celulares"
                    "audio" -> "Audio"
                    "computers" -> "Computadores"
                    else -> category
                }
                val rootIds = categories.filter { it.name.equals(requestedName, ignoreCase = true) || it.id.toString() == category }.map { it.id }.toSet()
                val parents = categories.associate { it.id to it.parentCategoryId }

                var filteredProducts =
                    allProducts.filter { product ->

                        val matchesName =
                            productName.isBlank() ||
                                    product.name.contains(
                                        productName,
                                        ignoreCase = true
                                    )

                        var categoryId: Int? = product.categoryId
                        val visited = mutableSetOf<Int>()
                        var matchesCategory = category == "all"
                        while (categoryId != null && visited.add(categoryId)) {
                            if (categoryId in rootIds) matchesCategory = true
                            categoryId = parents[categoryId]
                        }

                        val matchesRating =
                            if (product.reviews.none { it.isActive }) {

                                minimumRating <= 0f

                            } else {

                                val average =
                                    product.reviews.filter { it.isActive }
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