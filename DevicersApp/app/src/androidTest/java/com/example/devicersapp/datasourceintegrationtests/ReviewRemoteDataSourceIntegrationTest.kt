package com.example.devicersapp.datasourceintegrationtests

import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.firestore.ReviewRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.ui.models.ReviewChanges
import com.example.devicersapp.ui.models.ReviewDraft
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [ReviewRemoteDataSource] contra Firestore del emulador local. */
class ReviewRemoteDataSourceIntegrationTest {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var reviewDataSource: ReviewRemoteDataSourceFirestoreImplementation
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

        reviewDataSource = ReviewRemoteDataSourceFirestoreImplementation(db)

        // Crea el autor, el artículo y tres reseñas relacionadas para cada prueba.
        val batch = db.batch()
        val user = generateUser(0)
        val product = generateProduct(0)
        batch.set(db.collection("users").document("1000"), user)
        batch.set(db.collection("articles").document("1000"), product)

        repeat(3) { i ->
            val review = generateReview(i, userId = 1000, productId = 1000)
            batch.set(db.collection("reviews").document(review["id"].toString()), review)
        }

        // La siguiente reseña creada usará un ID desde 1100; merge conserva los demás contadores.
        batch.set(sequenceRef, mapOf("next_review_id" to 1100), SetOptions.merge())
        batch.commit().await()
    }

    @Test
    fun getReviewById_existingId_returnsCorrectReview() = runTest {
        // Preparación (Arrange): Selecciona una reseña y el título conocido de sus datos de prueba.
        val targetId = 1000
        val expectedTitle = "Título de la reseña 0"

        // Ejecución (Act): Consulta la reseña por su ID.
        val result = reviewDataSource.getReviewById(targetId)

        // Verificación (Assert): Comprueba que los datos correspondan al documento solicitado.
        Truth.assertThat(result.id).isEqualTo(targetId)
        Truth.assertThat(result.title).isEqualTo(expectedTitle)
    }

    @Test
    fun createReview_validDraft_reviewIsPersisted() = runTest {
        // Preparación (Arrange): Construye una reseña para el usuario y el artículo creados en setUp.
        val draft = ReviewDraft(
            userId = 1000,
            articleId = 1000,
            rating = 5,
            title = "Nueva reseña Test",
            body = "Cuerpo de prueba"
        )

        // Ejecución (Act): Publica el borrador mediante la fuente de datos.
        val result = reviewDataSource.createReview(draft)

        // Verificación (Assert): Verifica el ID asignado, el contenido y la existencia del documento guardado.
        Truth.assertThat(result.id).isAtLeast(1100)
        Truth.assertThat(result.body).isEqualTo("Cuerpo de prueba")

        val doc = db.collection("reviews").document(result.id.toString()).get().await()
        Truth.assertThat(doc.exists()).isTrue()
    }

    @Test
    fun updateReview_changesApplied_returnsUpdatedReview() = runTest {
        // Preparación (Arrange): Selecciona una reseña existente y define los cambios de calificación y texto.
        val targetId = 1001
        val changes = ReviewChanges(rating = 2, body = "Cuerpo editado")

        // Ejecución (Act): Actualiza la reseña seleccionada.
        val result = reviewDataSource.updateReview(targetId, changes)

        // Verificación (Assert): Comprueba que el resultado refleje ambos cambios.
        Truth.assertThat(result.rating).isEqualTo(2)
        Truth.assertThat(result.body).isEqualTo("Cuerpo editado")
    }

    @Test
    fun deleteReview_existingReview_setsIsActiveToFalse() = runTest {
        // Preparación (Arrange): Selecciona una reseña existente.
        val targetId = 1002

        // Ejecución (Act): Solicita su eliminación lógica.
        reviewDataSource.deleteReview(targetId)

        // Verificación (Assert): Vuelve a leer el documento y comprueba que permanezca guardado como inactivo.
        val deletedReview = reviewDataSource.getReviewById(targetId)
        Truth.assertThat(deletedReview.isActive).isFalse()
    }

    @Test
    fun getReviewsByProduct_existingReviews_returnsReviewList() = runTest {
        // Preparación (Arrange): Selecciona el artículo al que pertenecen las tres reseñas de setUp.
        val targetProductId = 1000

        // Ejecución (Act): Consulta las reseñas de ese artículo.
        val results = reviewDataSource.getReviewsByProduct(targetProductId)

        // Verificación (Assert): Verifica la cantidad y la referencia al artículo del resultado.
        Truth.assertThat(results).isNotEmpty()
        Truth.assertThat(results).hasSize(3)
        Truth.assertThat(results.first().articleId).isEqualTo(targetProductId)
    }

    /** Ejecuta la limpieza posterior a cada caso con el alcance definido por esta clase. */
    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        db.collection("users").document("1000").delete().await()
        db.collection("articles").document("1000").delete().await()

        // El filtro elimina las reseñas con ID desde 1000, no únicamente las tres de setUp.
        val testReviews = db.collection("reviews").whereGreaterThanOrEqualTo("id", 1000).get().await()
        testReviews.documents.forEach { doc ->
            db.collection("reviews").document(doc.id).delete().await()
        }

        // La limpieza actual elimina el documento de contadores; no restaura su contenido previo.
        sequenceRef.delete().await()
    }
}