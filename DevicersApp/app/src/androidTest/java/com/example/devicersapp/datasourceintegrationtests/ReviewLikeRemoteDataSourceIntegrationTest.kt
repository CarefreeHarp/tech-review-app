package com.example.devicersapp.datasourceintegrationtests

import com.example.devicersapp.data.datasource.ReviewLikeRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewLikeRemoteDataSourceFirestoreImplementation
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import com.google.firebase.auth.auth

/** Pruebas de integración de [ReviewLikeRemoteDataSource] contra Firestore del emulador local. */
class ReviewLikeRemoteDataSourceIntegrationTest {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var likeDataSource: ReviewLikeRemoteDataSourceFirestoreImplementation
    private val sequenceRef = db.collection("counters").document("next_id")

    /** Configura el destino local, prepara la sesión y carga los datos previos a cada prueba. */
    @Before
    fun setUp() = runTest {
        // En el emulador de Android, 10.0.2.2 permite acceder a Firebase en el computador anfitrión.
        try {
            db.useEmulator("10.0.2.2", 8080)
            auth.useEmulator("10.0.2.2", 9099)
        } catch (e: Exception) {
            // La comprobación siguiente verifica el destino efectivo aunque falle la reconfiguración.
        }
        // Esta validación detiene la preparación si Firestore no quedó conectado al emulador local.
        Truth.assertThat(db.firestoreSettings.host).isEqualTo("10.0.2.2:8080")

        // Crea una sesión anónima solo si Firebase Auth no conserva una sesión previa.
        if (auth.currentUser == null) {
            auth.signInAnonymously().await()
        }

        likeDataSource = ReviewLikeRemoteDataSourceFirestoreImplementation(db)

        // Prepara al autor, al usuario que dará like y al artículo de la reseña.
        val batch = db.batch()
        // Usuario 1000 será el autor, usuario 1001 dará los likes
        batch.set(db.collection("users").document("1000"), generateUser(0))
        batch.set(db.collection("users").document("1001"), generateUser(1))
        batch.set(db.collection("articles").document("1000"), generateProduct(0))

        // La reseña 1000 pertenece al usuario 1000 y empieza sin likes.
        batch.set(db.collection("reviews").document("1000"), generateReview(0, 1000, 1000))
        batch.commit().await()
    }

    @Test
    fun sendReviewLike_newLike_addsLikeAndIncrementsCount() = runTest {
        // Preparación (Arrange): Selecciona una reseña sin likes y un usuario distinto de su autor.
        val reviewId = 1000
        val actingUserId = 1001 // Usuario distinto al autor

        // Ejecución (Act): Da el primer like a la reseña.
        val result = likeDataSource.sendReviewLike(reviewId, actingUserId)

        // Verificación (Assert): Verifica el estado, el contador y el documento de la relación.
        Truth.assertThat(result.isLiked).isTrue()
        Truth.assertThat(result.likes).isEqualTo(1)

        // Verifica si el documento de like existe físicamente en la subcolección
        val likeDoc = db.collection("reviews").document("1000").collection("likes").document("1001").get().await()
        Truth.assertThat(likeDoc.exists()).isTrue()
    }

    @Test
    fun sendReviewLike_existingLike_removesLikeAndDecrementsCount() = runTest {
        // Preparación (Arrange): Crea un like previo del mismo usuario sobre la misma reseña.
        val reviewId = 1000
        val actingUserId = 1001
        likeDataSource.sendReviewLike(reviewId, actingUserId) // Se aplica el primer like

        // Ejecución (Act): Repite la acción para retirar el like.
        val result = likeDataSource.sendReviewLike(reviewId, actingUserId) // La segunda pulsación retira el like previo.

        // Verificación (Assert): Comprueba que el contador vuelva a cero y desaparezca el documento.
        Truth.assertThat(result.isLiked).isFalse()
        Truth.assertThat(result.likes).isEqualTo(0)

        val likeDoc = db.collection("reviews").document("1000").collection("likes").document("1001").get().await()
        Truth.assertThat(likeDoc.exists()).isFalse()
    }

    @Test
    fun getLikedReviewIds_userWithLikes_returnsSetOfLikedIds() = runTest {
        // Preparación (Arrange): Da like a la reseña 1000 antes de consultar.
        val reviewId = 1000
        val actingUserId = 1001
        likeDataSource.sendReviewLike(reviewId, actingUserId)

        val idsToFilter = setOf(1000, 1002, 1003) // 1002 y 1003 no existen
        // Ejecución (Act): Filtra entre una reseña existente y dos IDs que no se crean en setUp.
        val result = likeDataSource.getLikedReviewIds(actingUserId, idsToFilter)

        // Verificación (Assert): Comprueba que solo se devuelva el ID de la reseña con like.
        Truth.assertThat(result).containsExactly(1000)
    }

    @Test
    fun getReviewLikes_byReviewId_returnsLikeObjects() = runTest {
        // Preparación (Arrange): Agrega un like conocido a la reseña de prueba.
        val reviewId = 1000
        likeDataSource.sendReviewLike(reviewId, 1001)

        // Ejecución (Act): Consulta los likes de esa reseña.
        val results = likeDataSource.getReviewLikes(reviewId)

        // Verificación (Assert): Verifica que exista una sola relación y pertenezca al usuario 1001.
        Truth.assertThat(results).hasSize(1)
        Truth.assertThat(results.first().userId).isEqualTo(1001)
    }

    /** Ejecuta la limpieza posterior a cada caso con el alcance definido por esta clase. */
    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        // 1. Borrar usuarios y artículos creados
        db.collection("users").document("1000").delete().await()
        db.collection("users").document("1001").delete().await()
        db.collection("articles").document("1000").delete().await()

        // La limpieza actual elimina todas las reseñas y sus likes, no solo la reseña de prueba.
        val reviews = db.collection("reviews").get().await()
        for (reviewDoc in reviews) {
            val likes = reviewDoc.reference.collection("likes").get().await()
            for (like in likes) {
                like.reference.delete().await()
            }
            reviewDoc.reference.delete().await()
        }

        // También se elimina toda la colección de notificaciones, incluidas las previas a setUp.
        val allNotifications = db.collection("notifications").get().await()
        for (notif in allNotifications) {
            notif.reference.delete().await()
        }

        // Se elimina el contador completo, sin restaurar su contenido previo.
        sequenceRef.delete().await()
    }
}