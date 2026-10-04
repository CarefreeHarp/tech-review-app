package com.example.devicersapp.ui.screens.create_review

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.CancellationException
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.ui.mappers.toSearchContent
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProductProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva el estado y la lógica de búsqueda para crear una reseña. */
@HiltViewModel
class CreateReviewViewModel @Inject constructor(private val repository: ProductRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReviewState())
    val uiState: StateFlow<CreateReviewState> = _uiState

    init {
        loadProducts()
    }

    /** Carga las categorías y productos disponibles. */
    fun loadProducts() {
        if (_uiState.value.loading) return
        _uiState.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val products = repository.getProducts().filter { it.isActive }.map { it.toSearchContent() }
                _uiState.update {
                    val updated = it.copy(products = products, loading = false)
                    updated.copy(filteredProducts = filterProducts(updated))
                }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) { _uiState.update { it.copy(loading = false, error = "No se pudo cargar el catálogo.") } }
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
