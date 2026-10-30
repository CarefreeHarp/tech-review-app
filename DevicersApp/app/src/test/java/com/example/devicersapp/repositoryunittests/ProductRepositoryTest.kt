package com.example.devicersapp.repositoryunittests

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.ui.models.ProductInfo
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/** Pruebas unitarias de [ProductRepository] con la fuente de artículos reemplazada por un mock. */
class ProductRepositoryTest {
    private val mockDataSource = mockk<ProductRemoteDataSource>()
    private val repository = ProductRepository(mockDataSource)

    /** Genera un artículo con los campos obligatorios de [ProductInfo]. */
    private fun generateProduct(id: Int, isActive: Boolean) = ProductInfo(
        id = id,
        categoryId = 1,
        brandId = null,
        name = "Producto $id",
        model = null,
        description = null,
        imageUrl = null,
        releaseDate = null,
        specifications = null,
        isActive = isActive,
        createdAt = "2026-10-01T10:00:00Z",
        updatedAt = "2026-10-01T10:00:00Z"
    )

    @Test
    fun `when getProducts is called it leaves out inactive products`() = runTest {
        // Arrange
        coEvery { mockDataSource.getProducts() } returns listOf(
            generateProduct(id = 1, isActive = true),
            generateProduct(id = 2, isActive = false)
        )
        // Act
        val result = repository.getProducts()
        // Assert
        // map obtiene solo los IDs de los artículos devueltos.
        val resultIds = result.getOrNull()?.map { it.id }
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(resultIds).containsExactly(1)
    }
}
