
package com.example.devicersapp.ui.screens.create_review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
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
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _uiState =
        MutableStateFlow(CreateReviewState())

    val uiState: StateFlow<CreateReviewState> =
        _uiState

    /** Recolección activa del catálogo en vivo; se reemplaza al reintentar la carga. */
    private var productsJob: Job? = null

    init {
        loadProducts()
    }

    /**
     * Carga las categorías y escucha el catálogo en vivo: cada producto agregado a la base de datos
     * aparece en el listado sin recargar la pantalla.
     */
    fun loadProducts() {
        productsJob?.cancel()

        _uiState.update { currentState ->
            currentState.copy(
                isLoading = true,
                errorMessage = null
            )
        }

        productsJob = viewModelScope.launch {
            val categories = try {
                categoryRepository.getCategories().map {
                    ProductCategoryContent(
                        id = it.id.toString(),
                        labelResId = R.string.all,
                        label = it.name,
                        parentCategoryId = it.parentCategoryId?.toString()
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                showLoadError(error)
                return@launch
            }

            _uiState.update { currentState ->
                currentState.copy(
                    categories = listOf(
                        ProductCategoryContent("all", R.string.all)
                    ) + categories
                )
            }

            productRepository.getProductsLive()
                // Un fallo del catálogo detiene la escucha y conserva el mensaje de error en el estado.
                .catch { error -> showLoadError(error) }
                .collect { products ->
                    _uiState.update { currentState ->
                        val loadedState = currentState.copy(
                            products = products,
                            isLoading = false,
                            errorMessage = null
                        )

                        // Conserva la búsqueda y la categoría elegidas cuando llegan productos nuevos.
                        loadedState.copy(
                            filteredProducts = filterProducts(loadedState)
                        )
                    }
                }
        }
    }

    /**
     * Traduce el error de carga del catálogo al mensaje visible.
     */
    private fun showLoadError(error: Throwable) {
        val message = when (error) {
            is ConnectException -> R.string.catalog_connection_error
            is SocketTimeoutException -> R.string.catalog_timeout_error
            else -> R.string.catalog_load_error
        }

        _uiState.update { currentState ->
            currentState.copy(
                isLoading = false,
                errorMessage = message
            )
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

    /** Aplica la categoría seleccionada, sus descendientes y la consulta de texto al mismo catálogo. */
    private fun filterProducts(state: CreateReviewState): List<ProductInfo> {
        val parentCategories = state.categories.associate { it.id to it.parentCategoryId }
        val query = state.searchText.trim()

        return state.products.filter { product ->
            val matchesCategory = matchesSelectedCategory(
                product.categoryId.toString(),
                state.selectedCategoryId,
                parentCategories
            )
            val matchesSearch = query.isBlank() ||
                product.name.contains(query, ignoreCase = true) ||
                product.model?.contains(query, ignoreCase = true) == true ||
                product.description?.contains(query, ignoreCase = true) == true

            matchesCategory && matchesSearch
        }
    }

    /** Recorre los padres una sola vez y detiene los ciclos de una jerarquía inconsistente. */
    private fun matchesSelectedCategory(
        productCategoryId: String,
        selectedCategoryId: String,
        parentCategories: Map<String, String?>
    ): Boolean {
        var categoryId: String? = productCategoryId
        val visitedCategoryIds = mutableSetOf<String>()
        var matchesCategory = selectedCategoryId == "all"

        while (categoryId != null && visitedCategoryIds.add(categoryId)) {
            if (categoryId == selectedCategoryId) matchesCategory = true
            categoryId = parentCategories[categoryId]
        }
        return matchesCategory
    }
}
