package com.example.devicersapp.repositoryintegrationtests

import com.example.devicersapp.data.repository.ReviewLikeRepository
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewLikeRemoteDataSourceFirestoreImplementation
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [ReviewLikeRepository] contra Firestore del emulador local. */
class ReviewLikeRepositoryIntegrationTest {
    private val db = Firebase.firestore
    private val counters = db.collection("counters").document("next_id")
    private var originalCounters: Map<String, Any>? = null
    private lateinit var reviewLikeRepository: ReviewLikeRepository

    @Before
    fun setUp() = runTest {
        try {
            db.useEmulator("10.0.2.2", 8080)
        } catch (e: Exception) {
            // BaseApplication ya conectó Firestore al emulador; repetirlo después de usarlo lanza una excepción.
        }
        // Si Firestore no apunta al emulador local, la prueba se detiene aquí, antes de escribir o borrar datos.
        Truth.assertThat(db.firestoreSettings.host).isEqualTo("10.0.2.2:8080")

        reviewLikeRepository = ReviewLikeRepository(ReviewLikeRemoteDataSourceFirestoreImplementation(db))

        // Se guarda el contador real para dejarlo igual en tearDown.
        originalCounters = counters.get().await().data

        // Datos replicables y autónomos: el usuario 1000 escribió la reseña 1000, que todavía no tiene likes.
        val batch = db.batch()
        repeat(2) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user["id"].toString()), user)
        }
        val product = generateProduct(0)
        batch.set(db.collection("articles").document(product["id"].toString()), product)
        val review = generateReview(0, userId = 1000, productId = 1000)
        batch.set(db.collection("reviews").document(review["id"].toString()), review)
        // La notificación del like toma su ID de este contador; 1100 no choca con las de faker.
        // merge cambia solo este campo y conserva los demás contadores del documento.
        batch.set(counters, mapOf("next_notification_id" to 1100), SetOptions.merge())
        batch.commit().await()
    }

    @Test
    fun sendReviewLike_firstLike_reviewLiked() = runTest {
        // Arrange
        val reviewId = 1000
        val userId = 1001
        // Act
        val result = reviewLikeRepository.sendReviewLike(reviewId, userId)
        // Assert
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.isLiked).isTrue()
        Truth.assertThat(result.getOrNull()?.likes).isEqualTo(1)
    }

    @Test
    fun sendReviewLike_secondLike_likeRemoved() = runTest {
        // Arrange
        val reviewId = 1000
        val userId = 1001
        reviewLikeRepository.sendReviewLike(reviewId, userId)
        // Act
        val result = reviewLikeRepository.sendReviewLike(reviewId, userId)
        // Assert
        val likes = reviewLikeRepository.getReviewLikes(reviewId)
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.isLiked).isFalse()
        Truth.assertThat(likes).isEmpty()
    }

    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        val review = db.collection("reviews").document("1000")
        // Firestore no borra las subcolecciones junto con el documento, así que los likes se borran primero.
        val likes = review.collection("likes").get().await()
        likes.documents.forEach { doc ->
            review.collection("likes").document(doc.id).delete().await()
        }
        review.delete().await()
        db.collection("articles").document("1000").delete().await()
        // whereIn busca las notificaciones cuyo destinatario (user_id) es un usuario de prueba.
        val notifications = db.collection("notifications").whereIn("user_id", listOf(1000, 1001)).get().await()
        notifications.documents.forEach { doc ->
            db.collection("notifications").document(doc.id).delete().await()
        }
        repeat(2) { i ->
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
