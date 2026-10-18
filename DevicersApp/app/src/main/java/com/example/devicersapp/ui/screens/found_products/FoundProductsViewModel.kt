package com.example.devicersapp.ui.screens.found_products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.CategoryRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class FoundProductsViewModel @Inject constructor(
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FoundProductsState())
    val uiState = _uiState.asStateFlow()
    private var products: List<ProductInfo> = emptyList()
    private var loadJob: Job? = null

    /**
     * Aplica los filtros de la búsqueda sobre el catálogo en vivo: los productos agregados a la base
     * de datos aparecen en los resultados sin repetir la búsqueda.
     */
    fun loadProducts(productName: String, category: String, minimumRating: Float, sortBy: String) {
        loadJob?.cancel()
        products = emptyList()
        _uiState.update { it.copy(isLoading = true, searchText = productName, results = emptyList(), errorMessage = null) }
        loadJob = viewModelScope.launch {
            // Las categorías y las calificaciones se leen una vez; el catálogo de productos queda escuchándose.
            val (categories, reviews) = try {
                categoryRepository.getCategories() to
                    reviewRepository.getReviews().getOrThrow().filter { it.isActive }.groupBy { it.articleId }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                showLoadError()
                return@launch
            }
            val requestedName = when (category) {
                "cellphones" -> "Celulares"
                "audio" -> "Audio"
                "computers" -> "Computadores"
                else -> category
            }
            val roots = categories.filter {
                it.name.equals(requestedName, true) || it.id.toString() == category
            }.map { it.id }.toSet()
            val parents = categories.associate { it.id to it.parentCategoryId }

            productRepository.getProductsLive()
                // catch reemplaza al try-catch: un error del listener detiene el flujo y se muestra en pantalla.
                .catch { showLoadError() }
                // collect se ejecuta con la carga inicial y otra vez cada vez que cambia el catálogo.
                .collect { liveProducts ->
                    val catalogProducts = liveProducts.map { it.copy(reviews = reviews[it.id].orEmpty()) }
                    val filtered = catalogProducts.filter { product ->
                        var id: Int? = product.categoryId
                        val visited = mutableSetOf<Int>()
                        var matches = category == "all"
                        while (id != null && visited.add(id)) {
                            if (id in roots) matches = true
                            id = parents[id]
                        }
                        matches && average(product) >= minimumRating
                    }
                    products = when (sortBy) {
                        "recent" -> filtered.sortedByDescending { runCatching { Instant.parse(it.createdAt).toEpochMilli() }.getOrDefault(0L) }
                        "rated" -> filtered.sortedByDescending(::average)
                        else -> filtered
                    }
                    // Conserva el texto que la persona esté escribiendo al llegar productos nuevos.
                    _uiState.update { it.copy(results = filterProducts(it.searchText), isLoading = false, errorMessage = null) }
                }
        }
    }

    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText, results = filterProducts(searchText)) }
    }

    private fun showLoadError() {
        _uiState.update { it.copy(isLoading = false, errorMessage = R.string.catalog_load_error) }
    }

    private fun filterProducts(text: String): List<ProductInfo> = products.filter { product ->
        val query = text.trim()
        query.isEmpty() || listOfNotNull(product.name, product.model, product.description).any { it.contains(query, true) }
    }

    private fun average(product: ProductInfo): Double = product.reviews.filter { it.isActive }
        .map { it.rating }.average().takeIf { it.isFinite() } ?: 0.0
}
