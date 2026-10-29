package com.example.devicersapp.repositoryintegrationtests

import com.example.devicersapp.data.repository.CommentRepository
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.datasource.implementations.firestore.CommentLikeRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.CommentRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ProductRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewLikeRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.UserRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.ui.models.ReviewDraft
import com.google.common.truth.Truth
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [ReviewRepository] contra Firestore del emulador local. */
class ReviewRepositoryIntegrationTest {
    private val db = getRepositoryTestFirestore()
    private val counters = db.collection("counters").document("next_id")
    private var originalCounters: Map<String, Any>? = null
    private lateinit var reviewRepository: ReviewRepository

    @Before
    fun setUp() = runTest {
        // Si Firestore no apunta al emulador local, la prueba se detiene aquí, antes de escribir o borrar datos.
        Truth.assertThat(db.firestoreSettings.host).isEqualTo("10.0.2.2:8080")
        signInToRepositoryTestAuth(db)

        // El repositorio recibe las mismas dependencias que le entrega RepositoryModule en la app.
        val userDataSource = UserRemoteDataSourceFirestoreImplementation(db)
        val productRepository = ProductRepository(ProductRemoteDataSourceFirestoreImplementation(db))
        val commentRepository = CommentRepository(
            CommentRemoteDataSourceFirestoreImplementation(db), userDataSource,
            CommentLikeRemoteDataSourceFirestoreImplementation(db)
        )
        reviewRepository = ReviewRepository(
            ReviewRemoteDataSourceFirestoreImplementation(db), productRepository, userDataSource,
            commentRepository, ReviewLikeRemoteDataSourceFirestoreImplementation(db)
        )

        // Conserva el contador local para restaurarlo en tearDown.
        originalCounters = counters.get().await().data

        // Datos replicables y autónomos: 3 usuarios, 2 artículos y 4 reseñas.
        val batch = db.batch()
        repeat(3) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user["id"].toString()), user)
        }
        repeat(2) { i ->
            val product = generateProduct(i)
            batch.set(db.collection("articles").document(product["id"].toString()), product)
        }
        // El usuario 1001 escribió dos reseñas; los usuarios 1000 y 1002, una cada uno.
        val reviews = listOf(
            generateReview(0, userId = 1000, productId = 1000),
            generateReview(1, userId = 1001, productId = 1000),
            generateReview(2, userId = 1001, productId = 1001),
            generateReview(3, userId = 1002, productId = 1001)
        )
        reviews.forEach { review ->
            batch.set(db.collection("reviews").document(review["id"].toString()), review)
        }
        // createReview toma su ID de este contador; 1100 no choca con las reseñas de faker.
        // merge cambia solo este campo y conserva los demás contadores del documento.
        batch.set(counters, mapOf("next_review_id" to 1100), SetOptions.merge())
        batch.commit().await()
    }

    @Test
    fun getReviewById_validId_correctReview() = runTest {
        // Arrange
        val id = 1002
        val expectedTitle = "Título de la reseña 2"
        // Act
        val result = reviewRepository.getReviewById(id)
        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.title).isEqualTo(expectedTitle)
        Truth.assertThat(result.getOrNull()?.userId).isEqualTo(1001)
    }

    @Test
    fun createReview_validDraft_reviewCreated() = runTest {
        // Arrange
        val userId = 1000
        val draft = ReviewDraft(userId = userId, articleId = 1001, rating = 5, body = "Excelente producto", title = "Nueva reseña")
        val oldReviews = reviewRepository.getReviewsByUser(userId).getOrNull()
        // Act
        val result = reviewRepository.createReview(draft)
        // Assert
        val newReviews = reviewRepository.getReviewsByUser(userId).getOrNull()
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.body).isEqualTo("Excelente producto")
        Truth.assertThat(result.getOrNull()?.isActive).isTrue()
        Truth.assertThat(newReviews?.size).isGreaterThan(oldReviews?.size)
    }

    @Test
    fun deleteReview_existingReview_reviewInactive() = runTest {
        // Arrange
        val id = 1003
        // Act
        val result = reviewRepository.deleteReview(id)
        // Assert
        val deletedReview = reviewRepository.getReviewById(id).getOrNull()
        Truth.assertThat(result.isSuccess).isTrue()
        // La eliminación es lógica: la reseña sigue guardada, pero queda inactiva.
        Truth.assertThat(deletedReview?.isActive).isFalse()
    }

    @Test
    fun getFeedReviews_followedAuthor_onlyAuthorReviews() = runTest {
        // Arrange
        val currentUserId = 1000
        val followedAuthorId = 1001
        // Act
        val result = reviewRepository.getFeedReviews(currentUserId, authorIds = setOf(followedAuthorId))
        // Assert
        val feed = result.getOrNull()
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(feed).hasSize(2)
        // forEach revisa cada tarjeta del feed: todas deben ser del autor seguido, con su nombre resuelto.
        feed?.forEach { review ->
            Truth.assertThat(review.authorId).isEqualTo(followedAuthorId)
            Truth.assertThat(review.authorUsername).isEqualTo("username_1")
        }
    }

    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        // whereIn busca las reseñas de los usuarios de prueba, incluida la que crea createReview.
        val reviews = db.collection("reviews").whereIn("user_id", listOf(1000, 1001, 1002)).get().await()
        reviews.documents.forEach { doc ->
            db.collection("reviews").document(doc.id).delete().await()
        }
        repeat(2) { i ->
            db.collection("articles").document((1000 + i).toString()).delete().await()
        }
        repeat(3) { i ->
            db.collection("users").document((1000 + i).toString()).delete().await()
        }
        // Deja el contador como estaba: lo restaura, o lo borra si no existía antes de la prueba.
        val original = originalCounters
        if (original == null) {
            counters.delete().await()
        } else {
            counters.set(original).await()
        }
    }
}
