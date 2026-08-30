package com.example.devicersapp.ui.screens.create_review

import androidx.lifecycle.ViewModel
import com.example.devicersapp.data.local.LocalProductProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Conserva el estado y la lógica de búsqueda para crear una reseña. */
class CreateReviewViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState

    init {
        loadProducts()
    }

    /** Carga las categorías y productos disponibles. */
    private fun loadProducts() {
        _uiState.update { currentState ->
            val products = LocalProductProvider.products

            currentState.copy(
                categories = LocalProductProvider.categories,
                products = products,
                filteredProducts = products
            )
        }
    }

    /** Actualiza el texto de búsqueda y vuelve a filtrar los productos. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { currentState ->
            val newState = currentState.copy(searchText = searchText)
            newState.copy(
                filteredProducts = filterProducts(newState)
            )
        }
    }

    /** Actualiza la categoría activa y vuelve a filtrar los productos. */
    fun onCategoryChange(categoryId: String) {
        _uiState.update { currentState ->
            val newState = currentState.copy(
                selectedCategoryId = categoryId
            )

            newState.copy(
                filteredProducts = filterProducts(newState)
            )
        }
    }

    /** Filtra el catálogo según la categoría seleccionada y el texto escrito. */
    private fun filterProducts(
        state: CreateReviewState
    ): List<com.example.devicersapp.ui.models.ProductSearchContent> {
        return state.products.filter { product ->
            val matchesCategory =
                state.selectedCategoryId == "all" ||
                        product.categoryId == state.selectedCategoryId

            val matchesSearch =
                state.searchText.isBlank() ||
                        product.searchTerms.any { term ->
                            term.contains(
                                state.searchText.trim(),
                                ignoreCase = true
                            )
                        }

            matchesCategory && matchesSearch
        }
    }
}