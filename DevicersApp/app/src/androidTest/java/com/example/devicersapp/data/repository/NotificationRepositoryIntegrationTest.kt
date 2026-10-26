package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.implementations.firestore.NotificationRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ProductRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewLikeRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.UserRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.ui.models.NotificationTypes
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [NotificationRepository] contra Firestore del emulador local. */
class NotificationRepositoryIntegrationTest {
    private val db = Firebase.firestore
    private val counters = db.collection("counters").document("next_id")
    private var originalCounters: Map<String, Any>? = null
    private lateinit var notificationRepository: NotificationRepository
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

        // El repositorio recibe las mismas dependencias que le entrega RepositoryModule en la app.
        val userDataSource = UserRemoteDataSourceFirestoreImplementation(db)
        val productRepository = ProductRepository(ProductRemoteDataSourceFirestoreImplementation(db))
        notificationRepository = NotificationRepository(
            NotificationRemoteDataSourceFirestoreImplementation(db), userDataSource,
            ReviewRemoteDataSourceFirestoreImplementation(db), productRepository
        )
        reviewLikeRepository = ReviewLikeRepository(ReviewLikeRemoteDataSourceFirestoreImplementation(db))

        // Se guarda el contador real para dejarlo igual en tearDown.
        originalCounters = counters.get().await().data

        // Datos replicables y autónomos: 4 usuarios; el usuario 1000 escribió la reseña 1000 del Producto 0.
        val batch = db.batch()
        repeat(4) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user["id"].toString()), user)
        }
        val product = generateProduct(0)
        batch.set(db.collection("articles").document(product["id"].toString()), product)
        val review = generateReview(0, userId = 1000, productId = 1000)
        batch.set(db.collection("reviews").document(review["id"].toString()), review)
        // El usuario 1000 tiene dos notificaciones: un like del usuario 1001 y un seguimiento del usuario 1002.
        val notifications = listOf(
            generateNotification(0, userId = 1000, actorUserId = 1001, type = NotificationTypes.REVIEW_LIKE, reviewId = 1000),
            generateNotification(1, userId = 1000, actorUserId = 1002, type = NotificationTypes.FOLLOW, reviewId = null)
        )
        notifications.forEach { notification ->
            batch.set(db.collection("notifications").document(notification["id"].toString()), notification)
        }
        // La notificación de un like nuevo toma su ID de este contador; 1100 no choca con las de faker.
        // merge cambia solo este campo y conserva los demás contadores del documento.
        batch.set(counters, mapOf("next_notification_id" to 1100), SetOptions.merge())
        batch.commit().await()
    }

    @Test
    fun getNotifications_afterReviewLike_authorNotified() = runTest {
        // Arrange
        val authorId = 1000
        val likerId = 1003
        val oldNotifications = notificationRepository.getNotifications(authorId)
        // Act
        reviewLikeRepository.sendReviewLike(reviewId = 1000, userId = likerId)
        // Assert
        val newNotifications = notificationRepository.getNotifications(authorId)
        // map obtiene el usuario que generó cada notificación.
        val actorIds = newNotifications.map { it.actorUserId }
        Truth.assertThat(newNotifications.size).isGreaterThan(oldNotifications.size)
        Truth.assertThat(actorIds).contains(likerId)
    }

    @Test
    fun getActivity_inactiveActor_notificationExcluded() = runTest {
        // Arrange
        val userId = 1000
        db.collection("users").document("1002").update("is_active", false).await()
        // Act
        val result = notificationRepository.getActivity(userId)
        // Assert
        val activity = result.getOrNull()
        Truth.assertThat(result.isSuccess).isTrue()
        // Solo queda el like del usuario 1001: se omite el seguimiento del usuario 1002, que está inactivo.
        Truth.assertThat(activity).hasSize(1)
        // first() toma la única notificación que quedó; el repositorio le agrega el actor y el artículo.
        Truth.assertThat(activity?.first()?.actor?.username).isEqualTo("username_1")
        Truth.assertThat(activity?.first()?.productName).isEqualTo("Producto 0")
    }

    @After
    fun tearDown() = runTest {
        val review = db.collection("reviews").document("1000")
        // Firestore no borra las subcolecciones junto con el documento, así que los likes se borran primero.
        val likes = review.collection("likes").get().await()
        likes.documents.forEach { doc ->
            review.collection("likes").document(doc.id).delete().await()
        }
        review.delete().await()
        db.collection("articles").document("1000").delete().await()
        // whereIn busca las notificaciones cuyo destinatario (user_id) es un usuario de prueba, incluidas las nuevas.
        val notifications = db.collection("notifications").whereIn("user_id", listOf(1000, 1001, 1002, 1003)).get().await()
        notifications.documents.forEach { doc ->
            db.collection("notifications").document(doc.id).delete().await()
        }
        repeat(4) { i ->
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
