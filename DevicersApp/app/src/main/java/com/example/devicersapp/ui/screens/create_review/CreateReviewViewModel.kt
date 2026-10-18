
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
                    ProductCategoryContent(it.id.toString(), R.string.all, it.name, it.parentCategoryId?.toString())
                }
            } catch (cancelled: CancellationException) { throw cancelled
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
                // catch reemplaza al try-catch: un error del listener detiene el flujo y se muestra en pantalla.
                .catch { error -> showLoadError(error) }
                // collect se ejecuta con la carga inicial y otra vez cada vez que cambia el catálogo.
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

    /**
     * Filtra los productos.
     */
    private fun filterProducts(
        state: CreateReviewState
    ): List<ProductInfo> {

        return state.products.filter { product ->

            val parents = state.categories.associate {
                it.id to it.parentCategoryId
            }

            var categoryId: String? = product.categoryId.toString()
            val visited = mutableSetOf<String>()
            var matchesCategory = state.selectedCategoryId == "all"

            while (categoryId != null && visited.add(categoryId)) {
                if (categoryId == state.selectedCategoryId) {
                    matchesCategory = true
                }
                categoryId = parents[categoryId]
            }

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
