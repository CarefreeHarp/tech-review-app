
package com.example.devicersapp.ui.screens.create_review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.CategoryRepository
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
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository
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

            val result = try {
                val products = productRepository.getProducts().getOrThrow()
                val categories = categoryRepository.getCategories()
                Result.success(products to categories)
            } catch (cancelled: kotlinx.coroutines.CancellationException) { throw cancelled
            } catch (error: Exception) { Result.failure(error) }

            if (result.isSuccess) {

                val (products, categoryRecords) = result.getOrThrow()
                val categories = categoryRecords.map {
                    ProductCategoryContent(it.id.toString(), R.string.all, it.name, it.parentCategoryId?.toString())
                }

                _uiState.update { currentState ->
                    val loadedState = currentState.copy(
                        categories = listOf(
                            ProductCategoryContent("all", R.string.all)
                        ) + categories,
                        products = products,
                        isLoading = false,
                        errorMessage = null
                    )

                    // La búsqueda sigue disponible durante la carga; conserva la consulta más reciente.
                    loadedState.copy(
                        filteredProducts = filterProducts(loadedState)
                    )
                }

            } else {

                val exception =
                    result.exceptionOrNull()

                val message = when (exception) {

                    is ConnectException ->
                        R.string.catalog_connection_error

                    is SocketTimeoutException ->
                        R.string.catalog_timeout_error

                    else ->
                        R.string.catalog_load_error
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
