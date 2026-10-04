package com.example.devicersapp.ui.screens.create_review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.data.local.LocalProductProvider
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
 * Conserva el estado y la lógica de búsqueda para crear una reseña.
 */
@HiltViewModel
class CreateReviewViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(CreateReviewState())

    val uiState: StateFlow<CreateReviewState> =
        _uiState

    init {
        loadProducts()
    }

    /**
     * Carga los productos disponibles desde el backend.
     */
    fun loadProducts() {

        viewModelScope.launch {

            _uiState.update { currentState ->
                currentState.copy(
                    isLoading = true,
                    errorMessage = null
                )
            }

            val result =
                productRepository.getProducts()

            if (result.isSuccess) {

                val products =
                    result.getOrNull() ?: emptyList()

                _uiState.update { currentState ->
                    currentState.copy(
                        categories = LocalProductProvider.categories,
                        products = products,
                        filteredProducts = products,
                        isLoading = false,
                        errorMessage = null
                    )
                }

            } else {

                val exception =
                    result.exceptionOrNull()

                val message = when (exception) {

                    is ConnectException ->
                        "No fue posible conectarse con el servidor."

                    is SocketTimeoutException ->
                        "El servidor está tardando demasiado en responder."

                    else ->
                        "No fue posible cargar los productos."
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

    /**
     * Actualiza el texto de búsqueda.
     */
    fun onSearchTextChange(searchText: String) {

        _uiState.update { currentState ->

            val newState =
                currentState.copy(
                    searchText = searchText
                )

            newState.copy(
                filteredProducts =
                    filterProducts(newState)
            )
        }
    }

    /**
     * Actualiza la categoría seleccionada.
     */
    fun onCategoryChange(categoryId: String) {

        _uiState.update { currentState ->

            val newState =
                currentState.copy(
                    selectedCategoryId = categoryId
                )

            newState.copy(
                filteredProducts =
                    filterProducts(newState)
            )
        }
    }

    /**
     * Filtra los productos.
     */
    private fun filterProducts(
        state: CreateReviewState
    ): List<ProductDto> {

        return state.products.filter { product ->

            val selectedCategoryId = when (state.selectedCategoryId) {
                "cellphones" -> 1
                "audio" -> 3
                "computers" -> 2
                else -> null
            }

            val matchesCategory =
                state.selectedCategoryId == "all" ||
                        product.categoryId == selectedCategoryId

            val search = state.searchText.trim()

            val matchesSearch =
                search.isBlank() ||
                        product.name.contains(
                            search,
                            ignoreCase = true
                        ) ||
                        product.model?.contains(
                            search,
                            ignoreCase = true
                        ) == true ||
                        product.description?.contains(
                            search,
                            ignoreCase = true
                        ) == true

            matchesCategory && matchesSearch
        }
    }
}