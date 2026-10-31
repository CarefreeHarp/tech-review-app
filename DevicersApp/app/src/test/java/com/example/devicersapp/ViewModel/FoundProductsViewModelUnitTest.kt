package com.example.devicersapp.ui.screens.found_products

import com.example.devicersapp.R
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.CategoryRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.ui.models.CategoryInfo
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ReviewInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FoundProductsViewModelUnitTest {

    private lateinit var viewModel: FoundProductsViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var reviewRepository: ReviewRepository

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        productRepository = mockk()
        categoryRepository = mockk()
        reviewRepository = mockk()

        viewModel = FoundProductsViewModel(
            productRepository,
            categoryRepository,
            reviewRepository
        )
    }

    //every -> no son fun suspend
    //coEvery -> son fun suspend
    //flowOf -> emite una lista una vez (datos en vivo)

    @Test
    fun loadProducts_reviewsFailure_showsErrorMessage() = runTest {

        // arrange
        coEvery {
            categoryRepository.getCategories()
        } returns listOf(
            CategoryInfo(1, null, "Celulares", null)
        )

        coEvery {
            reviewRepository.getReviews()
        } returns Result.failure(Exception("Error al cargar reseñas"))

        // act
        viewModel.loadProducts(
            productName = "Samsung",
            category = "cellphones",
            minimumRating = 0f,
            sortBy = ""
        )

        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.results).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.searchText).isEqualTo("Samsung")
        assertThat(state.errorMessage).isEqualTo(R.string.catalog_load_error)
    }

    @Test
    fun loadProducts_success_filtersProductsCorrectly() = runTest {

        // arrange
        val categories = listOf(
            CategoryInfo(1, null, "Celulares", null),
            CategoryInfo(2, null, "Computadores", null)
        )

        val samsung = ProductInfo(
            id = 1,
            categoryId = 1,
            brandId = null,
            name = "Samsung Galaxy S25",
            model = "S25",
            description = "Smartphone Samsung",
            imageUrl = null,
            releaseDate = "2025-01-22",
            specifications = null,
            isActive = true,
            createdAt = "2025-01-01T10:00:00Z",
            updatedAt = "2025-01-01T10:00:00Z"
        )

        val sony = ProductInfo(
            id = 2,
            categoryId = 1,
            brandId = null,
            name = "Sony Xperia",
            model = "Xperia 1",
            description = "Smartphone Sony",
            imageUrl = null,
            releaseDate = "2025-01-22",
            specifications = null,
            isActive = true,
            createdAt = "2025-01-02T10:00:00Z",
            updatedAt = "2025-01-02T10:00:00Z"
        )

        val lenovo = ProductInfo(
            id = 3,
            categoryId = 2,
            brandId = null,
            name = "Lenovo ThinkPad",
            model = "T14",
            description = "Computador Lenovo",
            imageUrl = null,
            releaseDate = "2025-01-22",
            specifications = null,
            isActive = true,
            createdAt = "2025-01-03T10:00:00Z",
            updatedAt = "2025-01-03T10:00:00Z"
        )

        val reviews = listOf(
            ReviewInfo(
                id = 101,
                userId = 10,
                articleId = 1,
                rating = 5,
                title = "Excelente",
                body = "Muy buen celular",
                isActive = true,
                createdAt = "2025-01-02T10:00:00Z",
                updatedAt = "2025-01-02T10:00:00Z"
            ),
            ReviewInfo(
                id = 102,
                userId = 20,
                articleId = 2,
                rating = 4,
                title = "Bueno",
                body = "Buen celular",
                isActive = true,
                createdAt = "2025-01-03T10:00:00Z",
                updatedAt = "2025-01-03T10:00:00Z"
            ),
            ReviewInfo(
                id = 103,
                userId = 30,
                articleId = 3,
                rating = 5,
                title = "Excelente",
                body = "Buen computador",
                isActive = true,
                createdAt = "2025-01-04T10:00:00Z",
                updatedAt = "2025-01-04T10:00:00Z"
            )
        )

        coEvery {
            categoryRepository.getCategories()
        } returns categories

        coEvery {
            reviewRepository.getReviews()
        } returns Result.success(reviews)

        every {
            productRepository.getProductsLive()
        } returns flowOf(listOf(samsung, sony, lenovo))

        // act
        viewModel.loadProducts(
            productName = "",
            category = "cellphones",
            minimumRating = 4f,
            sortBy = ""
        )

        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessage).isNull()

        assertThat(state.results).hasSize(2)
        assertThat(state.results.map { it.id })
            .containsExactly(1, 2)
            .inOrder()

        // Comprobamos la búsqueda por texto.
        viewModel.onSearchTextChange("Sony")

        val filteredState = viewModel.uiState.value

        assertThat(filteredState.searchText).isEqualTo("Sony")
        assertThat(filteredState.results).hasSize(1)
        assertThat(filteredState.results[0].id).isEqualTo(2)
        assertThat(filteredState.results[0].name).isEqualTo("Sony Xperia")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
