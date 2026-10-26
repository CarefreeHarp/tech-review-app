package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.MessagingRemoteDataSource
import com.example.devicersapp.data.datasource.StorageRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.firestore.CommentLikeRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.CommentRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.FollowRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ProductRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewLikeRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.UserRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.ui.models.UserChanges
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.messaging
import com.google.firebase.storage.storage
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [UserRepository] contra Firestore del emulador local. */
class UserRepositoryIntegrationTest {
    private val db = Firebase.firestore
    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {
            // BaseApplication ya conectó Firestore al emulador; repetirlo después de usarlo lanza una excepción.
        }
        // Si Firestore no apunta al emulador local, la prueba se detiene aquí, antes de escribir o borrar datos.
        Truth.assertThat(db.firestoreSettings.host).isEqualTo("10.0.2.2:8080")

        // El repositorio recibe las mismas dependencias que le entrega RepositoryModule en la app.
        val userDataSource = UserRemoteDataSourceFirestoreImplementation(db)
        val productRepository = ProductRepository(ProductRemoteDataSourceFirestoreImplementation(db))
        val commentRepository = CommentRepository(
            CommentRemoteDataSourceFirestoreImplementation(db), userDataSource,
            CommentLikeRemoteDataSourceFirestoreImplementation(db)
        )
        val reviewRepository = ReviewRepository(
            ReviewRemoteDataSourceFirestoreImplementation(db), productRepository, userDataSource,
            commentRepository, ReviewLikeRemoteDataSourceFirestoreImplementation(db)
        )
        val followRepository = FollowRepository(FollowRemoteDataSourceFirestoreImplementation(db), userDataSource)
        userRepository = UserRepository(
            userDataSource, reviewRepository, StorageRemoteDataSource(Firebase.storage),
            MessagingRemoteDataSource(Firebase.messaging), followRepository
        )

        // Datos replicables y autónomos: cada prueba empieza con los mismos 10 usuarios (IDs 1000 a 1009).
        val batch = db.batch()
        repeat(10) { i ->
            val user = generateUser(i)
            // El ID del documento es el mismo valor de su campo id.
            batch.set(db.collection("users").document(user["id"].toString()), user)
        }
        batch.commit().await()
    }

    @Test
    fun getById_validId_correctUser() = runTest {
        // Arrange
        val id = 1009
        val expectedUsername = "username_9"
        // Act
        val result = userRepository.getById(id)
        // Assert
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result.username).isEqualTo(expectedUsername)
        Truth.assertThat(result.id).isEqualTo(id)
    }

    @Test
    fun updateProfile_newBiography_biographySaved() = runTest {
        // Arrange
        val id = 1001
        val newBiography = "Biografía actualizada desde la prueba"
        // Act
        userRepository.updateProfile(id, UserChanges(biography = newBiography))
        // Assert
        val result = userRepository.getById(id)
        Truth.assertThat(result.biography).isEqualTo(newBiography)
    }

    @Test
    fun getSummaries_inactiveUser_onlyActiveUsers() = runTest {
        // Arrange
        db.collection("users").document("1002").update("is_active", false).await()
        val ids = setOf(1000, 1001, 1002)
        // Act
        val result = userRepository.getSummaries(ids)
        // Assert
        // map obtiene solo los IDs de los perfiles devueltos.
        val resultIds = result.map { it.id }
        Truth.assertThat(resultIds).containsExactly(1000, 1001)
    }

    @After
    fun tearDown() = runTest {
        // Solo se borran los usuarios de prueba; los datos cargados con faker se conservan.
        repeat(10) { i ->
            db.collection("users").document((1000 + i).toString()).delete().await()
        }
    }
}
