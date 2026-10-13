package com.example.devicersapp.ui.screens.found_products

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.DiscoveryRepository
import com.example.devicersapp.ui.models.ProductInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class FoundProductsViewModel @Inject constructor(private val repository: DiscoveryRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(FoundProductsState())
    val uiState = _uiState.asStateFlow()
    private var products: List<ProductInfo> = emptyList()
    private var loadJob: Job? = null

    fun loadProducts(productName: String, category: String, minimumRating: Float, sortBy: String) {
        loadJob?.cancel()
        products = emptyList()
        _uiState.update { it.copy(isLoading = true, searchText = productName, results = emptyList(), errorMessage = null) }
        loadJob = viewModelScope.launch {
            try {
                val catalog = repository.getCatalog()
                val requestedName = when (category) {
                    "cellphones" -> "Celulares"
                    "audio" -> "Audio"
                    "computers" -> "Computadores"
                    else -> category
                }
                val roots = catalog.categories.filter {
                    it.name.equals(requestedName, true) || it.id.toString() == category
                }.map { it.id }.toSet()
                val parents = catalog.categories.associate { it.id to it.parentCategoryId }
                val filtered = catalog.products.filter { product ->
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
                _uiState.update { it.copy(results = filterProducts(it.searchText), isLoading = false) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, errorMessage = "No se pudieron cargar los productos. Intenta nuevamente.") }
            }
        }
    }

    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText, results = filterProducts(searchText)) }
    }

    private fun filterProducts(text: String): List<ProductInfo> = products.filter { product ->
        val query = text.trim()
        query.isEmpty() || listOfNotNull(product.name, product.model, product.description).any { it.contains(query, true) }
    }

    private fun average(product: ProductInfo): Double = product.reviews.filter { it.isActive }
        .map { it.rating }.average().takeIf { it.isFinite() } ?: 0.0
}
