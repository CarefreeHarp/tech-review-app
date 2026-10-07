package com.example.devicersapp.remote

import com.example.devicersapp.data.datasource.CategoryRemoteDataSource
import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.dto.CategoryDto
import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.data.repository.CategoryRepository
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.ui.screens.create_review.CreateReviewViewModel
import com.example.devicersapp.ui.screens.found_products.FoundProductsViewModel
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

/** Evita que la respuesta remota borre una búsqueda escrita con el skeleton visible. */
@OptIn(ExperimentalCoroutinesApi::class)
class LoadingSearchTest {
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() { Dispatchers.setMain(dispatcher) }

    @After
    fun cleanup() { Dispatchers.resetMain() }

    @Test
    fun suggestedProductsApplyQueryEnteredWhileLoading() = runTest(dispatcher) {
        val pending = CompletableDeferred<List<ProductDto>>()
        val viewModel = CreateReviewViewModel(products(pending), categories())
        runCurrent()
        assertTrue(viewModel.uiState.value.isLoading)
        viewModel.onSearchTextChange("Tablet")
        pending.complete(catalog())
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Tablet", state.searchText)
        assertEquals(listOf("Tablet"), state.filteredProducts.map { it.name })
    }

    @Test
    fun foundProductsKeepLatestQueryAfterPendingResponse() = runTest(dispatcher) {
        val pending = CompletableDeferred<List<ProductDto>>()
        val viewModel = FoundProductsViewModel(products(pending), categories())
        viewModel.loadProducts("", "all", 0f, "recent")
        runCurrent()
        assertTrue(viewModel.uiState.value.isLoading)
        viewModel.onSearchTextChange("Tablet")
        pending.complete(catalog())
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertFalse(state.isLoading)
        assertEquals("Tablet", state.searchText)
        assertEquals(listOf("Tablet"), state.results.map { it.name })
        viewModel.onSearchTextChange("")
        assertEquals(2, viewModel.uiState.value.results.size)
    }

    private fun products(pending: CompletableDeferred<List<ProductDto>>) = ProductRepository(
        object : ProductRemoteDataSource {
            override suspend fun getProducts() = pending.await()
            override suspend fun getProductById(productId: Int) = pending.await().first { it.id == productId }
        }
    )

    private fun categories() = CategoryRepository(object : CategoryRemoteDataSource {
        override suspend fun getCategories() = emptyList<CategoryDto>()
    })

    private fun catalog() = listOf("Phone", "Tablet").mapIndexed { index, name ->
        ProductDto(id = index + 1, category_id = 1, brand_id = 1, name = name, model = null,
            description = null, image_url = null, release_date = null, specifications = null,
            is_active = true, createdAt = "", updatedAt = "")
    }
}
