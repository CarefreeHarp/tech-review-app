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

/** Mantiene los resultados de búsqueda filtrados sobre el catálogo en vivo. */
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
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
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
                // Un fallo del catálogo detiene la escucha y conserva el mensaje de error en el estado.
                .catch { showLoadError() }
                .collect { liveProducts ->
                    val catalogProducts = liveProducts.map { it.copy(reviews = reviews[it.id].orEmpty()) }
                    val filteredProducts = catalogProducts.filter { product ->
                        matchesCategory(product.categoryId, category, roots, parents) &&
                            activeAverageRating(product) >= minimumRating
                    }
                    products = sortProducts(filteredProducts, sortBy)
                    // Conserva el texto que la persona esté escribiendo al llegar productos nuevos.
                    _uiState.update { it.copy(results = filterProducts(it.searchText), isLoading = false, errorMessage = null) }
                }
        }
    }

    /** Vuelve a aplicar la consulta de texto a los productos que ya cumplen los filtros. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText, results = filterProducts(searchText)) }
    }

    private fun showLoadError() {
        _uiState.update { it.copy(isLoading = false, errorMessage = R.string.catalog_load_error) }
    }

    /** Filtra por nombre, modelo o descripción sin modificar el texto escrito en pantalla. */
    private fun filterProducts(text: String): List<ProductInfo> {
        val query = text.trim()
        return products.filter { product ->
            query.isEmpty() || listOfNotNull(product.name, product.model, product.description)
                .any { it.contains(query, ignoreCase = true) }
        }
    }

    /** Incluye descendientes de las categorías encontradas y detiene los ciclos entre padres. */
    private fun matchesCategory(
        productCategoryId: Int,
        requestedCategory: String,
        rootCategoryIds: Set<Int>,
        parentCategories: Map<Int, Int?>
    ): Boolean {
        var categoryId: Int? = productCategoryId
        val visitedCategoryIds = mutableSetOf<Int>()
        var matchesCategory = requestedCategory == "all"
        while (categoryId != null && visitedCategoryIds.add(categoryId)) {
            if (categoryId in rootCategoryIds) matchesCategory = true
            categoryId = parentCategories[categoryId]
        }
        return matchesCategory
    }

    /** Mantiene el orden recibido salvo que la búsqueda pida fecha o calificación descendente. */
    private fun sortProducts(products: List<ProductInfo>, sortBy: String): List<ProductInfo> = when (sortBy) {
        "recent" -> products.sortedByDescending {
            runCatching { Instant.parse(it.createdAt).toEpochMilli() }.getOrDefault(0L)
        }
        "rated" -> products.sortedByDescending(::activeAverageRating)
        else -> products
    }

    /** Los productos sin reseñas activas tienen promedio cero en los filtros y en el orden. */
    private fun activeAverageRating(product: ProductInfo): Double = product.reviews.filter { it.isActive }
        .map { it.rating }.average().takeIf { it.isFinite() } ?: 0.0
}
