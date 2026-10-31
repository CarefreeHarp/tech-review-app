package com.example.devicersapp.ui.screens.product

import com.example.devicersapp.R
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.UserRepository
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.models.UserInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ProductViewModelUnitTest {

    private lateinit var viewModel: ProductViewModel
    private lateinit var productRepository: ProductRepository
    private lateinit var reviewRepository: ReviewRepository
    private lateinit var userRepository: UserRepository

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        productRepository = mockk()
        reviewRepository = mockk()
        userRepository = mockk()

        viewModel = ProductViewModel(
            productRepository,
            reviewRepository,
            userRepository
        )
    }

    @Test
    fun loadProduct_productFailure_showsErrorMessage() = runTest {

        // arrange
        coEvery {
            productRepository.getProductById(1)
        } returns Result.failure(Exception("Error al cargar producto"))

        // act
        viewModel.loadProduct(1)
        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.product).isNull()
        assertThat(state.reviews).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessageResId)
            .isEqualTo(R.string.remote_product_load_error)
    }

    @Test
    fun loadProduct_reviewsFailure_showsErrorMessage() = runTest {

        // arrange
        val product = ProductInfo(
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

        coEvery {
            productRepository.getProductById(1)
        } returns Result.success(product)

        coEvery {
            reviewRepository.getReviewsByProduct(1)
        } returns Result.failure(Exception("Error al cargar reseñas"))

        // act
        viewModel.loadProduct(1)
        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.product).isNull()
        assertThat(state.reviews).isEmpty()
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessageResId)
            .isEqualTo(R.string.remote_product_load_error)
    }

    @Test
    fun loadProduct_success_loadsProductAndActiveReviews() = runTest {

        // arrange
        val product = ProductInfo(
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

        val olderReview = ReviewInfo(
            id = 101,
            userId = 10,
            articleId = 1,
            rating = 4,
            title = "Buen producto",
            body = "Funciona correctamente",
            isActive = true,
            createdAt = "2025-01-02T10:00:00Z",
            updatedAt = "2025-01-02T10:00:00Z"
        )

        val newerReview = ReviewInfo(
            id = 102,
            userId = 20,
            articleId = 1,
            rating = 5,
            title = "Excelente producto",
            body = "Muy buena experiencia",
            isActive = true,
            createdAt = "2025-01-03T10:00:00Z",
            updatedAt = "2025-01-03T10:00:00Z"
        )

        val inactiveReview = ReviewInfo(
            id = 103,
            userId = 30,
            articleId = 1,
            rating = 1,
            title = "Reseña inactiva",
            body = "Esta reseña no debe aparecer",
            isActive = false,
            createdAt = "2025-01-04T10:00:00Z",
            updatedAt = "2025-01-04T10:00:00Z"
        )

        val user1 = UserInfo(
            id = 10,
            email = "juan@test.com",
            username = "juanito",
            firebaseUid = null,
            biography = null,
            profileImageUrl = null,
            notificationsLastViewedAt = null,
            isActive = true,
            createdAt = "2025-01-01T10:00:00Z",
            updatedAt = "2025-01-01T10:00:00Z"
        )

        val user2 = UserInfo(
            id = 20,
            email = "maria@test.com",
            username = "maria",
            firebaseUid = null,
            biography = null,
            profileImageUrl = null,
            notificationsLastViewedAt = null,
            isActive = true,
            createdAt = "2025-01-01T10:00:00Z",
            updatedAt = "2025-01-01T10:00:00Z"
        )

        coEvery {
            productRepository.getProductById(1)
        } returns Result.success(product)

        coEvery {
            reviewRepository.getReviewsByProduct(1)
        } returns Result.success(
            listOf(olderReview, inactiveReview, newerReview)
        )

        coEvery {
            userRepository.getByIds(setOf(10, 20))
        } returns mapOf(
            10 to user1,
            20 to user2
        )

        // act
        viewModel.loadProduct(1)
        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.product).isEqualTo(product)
        assertThat(state.isLoading).isFalse()
        assertThat(state.errorMessageResId).isNull()

        assertThat(state.reviews).hasSize(2)

        assertThat(state.reviews.map { it.id })
            .containsExactly(102, 101)
            .inOrder()

        assertThat(state.reviews[0].user).isEqualTo(user2)
        assertThat(state.reviews[1].user).isEqualTo(user1)

        assertThat(state.reviews[0].article).isEqualTo(product)
        assertThat(state.reviews[1].article).isEqualTo(product)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
