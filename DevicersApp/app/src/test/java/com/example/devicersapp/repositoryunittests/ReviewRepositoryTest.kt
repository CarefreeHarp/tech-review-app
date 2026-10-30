package com.example.devicersapp.repositoryunittests

import com.example.devicersapp.data.datasource.ReviewLikeRemoteDataSource
import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.datasource.UserRemoteDataSource
import com.example.devicersapp.data.repository.CommentRepository
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.ui.models.ReviewInfo
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/** Pruebas unitarias de [ReviewRepository]: las fuentes de datos y los repositorios que usa son mocks. */
class ReviewRepositoryTest {
    private val mockDataSource = mockk<ReviewRemoteDataSource>()
    private val productRepository = mockk<ProductRepository>()
    private val userDataSource = mockk<UserRemoteDataSource>()
    private val commentRepository = mockk<CommentRepository>()
    private val reviewLikeDataSource = mockk<ReviewLikeRemoteDataSource>()
    private val repository = ReviewRepository(
        mockDataSource, productRepository, userDataSource, commentRepository, reviewLikeDataSource
    )

    /** Genera una reseña del usuario 7 sobre el artículo 3 con los campos obligatorios de [ReviewInfo]. */
    private fun generateReview(id: Int, isActive: Boolean = true) = ReviewInfo(
        id = id,
        userId = 7,
        articleId = 3,
        rating = 4,
        title = "Buen celular",
        body = "La batería dura todo el día",
        isActive = isActive,
        createdAt = "2026-10-01T10:00:00Z",
        updatedAt = "2026-10-01T10:00:00Z",
        likes = 0
    )

    @Test
    fun `when getReviewById is called with a valid id it returns a Result with the review`() = runTest {
        // Arrange
        coEvery { mockDataSource.getReviewById(123) } returns generateReview(id = 123)
        // Act
        val result = repository.getReviewById(123)
        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.id).isEqualTo(123)
        Truth.assertThat(result.getOrNull()?.title).isEqualTo("Buen celular")
    }

    @Test
    fun `when getReviewById is called with an id that does not exist it returns a Result with failure`() = runTest {
        // Arrange
        coEvery { mockDataSource.getReviewById(999) } throws NoSuchElementException("No existe reviews/999")
        // Act
        val result = repository.getReviewById(999)
        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("No existe reviews/999")
    }

    @Test
    fun `when getReviewDetail receives an inactive review it returns a failure without loading the product`() = runTest {
        // Arrange
        coEvery { mockDataSource.getReviewById(123) } returns generateReview(id = 123, isActive = false)
        // Act
        val result = repository.getReviewDetail(123)
        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()?.message).isEqualTo("La reseña ya no está disponible.")
        // coVerify revisa cuántas veces se llamó a un mock: el artículo de una reseña retirada no se consulta.
        coVerify(exactly = 0) { productRepository.getProductById(any()) }
    }
}
