package com.example.devicersapp.repositoryintegrationtests

import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.datasource.implementations.firestore.ProductRemoteDataSourceFirestoreImplementation
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [ProductRepository] contra Firestore del emulador local. */
class ProductRepositoryIntegrationTest {
    private val db = Firebase.firestore
    private lateinit var productRepository: ProductRepository

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {
            // BaseApplication ya conectó Firestore al emulador; repetirlo después de usarlo lanza una excepción.
        }
        // Si Firestore no apunta al emulador local, la prueba se detiene aquí, antes de escribir o borrar datos.
        Truth.assertThat(db.firestoreSettings.host).isEqualTo("10.0.2.2:8080")

        productRepository = ProductRepository(ProductRemoteDataSourceFirestoreImplementation(db))

        // Datos replicables y autónomos: cada prueba empieza con los mismos 5 artículos (IDs 1000 a 1004).
        val batch = db.batch()
        repeat(5) { i ->
            val product = generateProduct(i)
            // El ID del documento es el mismo valor de su campo id.
            batch.set(db.collection("articles").document(product["id"].toString()), product)
        }
        batch.commit().await()
    }

    @Test
    fun getProductById_validId_correctProduct() = runTest {
        // Arrange
        val id = 1003
        val expectedName = "Producto 3"
        // Act
        val result = productRepository.getProductById(id)
        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.name).isEqualTo(expectedName)
        Truth.assertThat(result.getOrNull()?.id).isEqualTo(id)
    }

    @Test
    fun getProductById_nonexistentId_failure() = runTest {
        // Arrange
        val id = 1999
        // Act
        val result = productRepository.getProductById(id)
        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun getProducts_inactiveProduct_productExcluded() = runTest {
        // Arrange
        db.collection("articles").document("1004").update("is_active", false).await()
        // Act
        val result = productRepository.getProducts()
        // Assert
        // map obtiene solo los IDs de los artículos devueltos.
        val resultIds = result.getOrNull()?.map { it.id }
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(resultIds).containsAtLeast(1000, 1001, 1002, 1003)
        Truth.assertThat(resultIds).doesNotContain(1004)
    }

    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        // Solo se borran los artículos de prueba; los datos cargados con faker se conservan.
        repeat(5) { i ->
            db.collection("articles").document((1000 + i).toString()).delete().await()
        }
    }
}
