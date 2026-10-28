package com.example.devicersapp.datasourceintegrationtests

import com.example.devicersapp.data.datasource.UserRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.firestore.UserRemoteDataSourceFirestoreImplementation
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [UserRemoteDataSource] contra Firestore del emulador local. */
class UserRemoteDataSourceIntegrationTest {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var userDataSource: UserRemoteDataSourceFirestoreImplementation

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

        userDataSource = UserRemoteDataSourceFirestoreImplementation(db)

        // Cada prueba empieza con los mismos tres perfiles (1000 a 1002).
        val batch = db.batch()
        repeat(3) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user["id"].toString()), user)
        }
        batch.commit().await()
    }

    @Test
    fun getById_existingUser_returnsUser() = runTest {
        // Preparación (Arrange): Selecciona al usuario 1001 creado en setUp.
        val targetId = 1001
        // Ejecución (Act): Consulta el perfil por su ID numérico.
        val result = userDataSource.getById(targetId)
        // Verificación (Assert): Verifica el identificador y el nombre del usuario devuelto.
        Truth.assertThat(result.id).isEqualTo(targetId)
        Truth.assertThat(result.username).isEqualTo("username_1")
    }

    @Test
    fun getById_nonExistingUser_throwsNoSuchElementException() = runTest {
        // Preparación (Arrange): Usa un ID que no se inserta durante la preparación.
        val invalidId = 9999
        // Ejecución (Act): Captura el resultado fallido de la consulta.
        val result = runCatching { userDataSource.getById(invalidId) }
        // Verificación (Assert): Comprueba que la ausencia produzca NoSuchElementException.
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    fun findByFirebaseUid_existingUid_returnsCorrectUser() = runTest {
        // Preparación (Arrange): Usa el UID ficticio asociado al perfil 1002.
        val targetUid = "uid_2"
        // Ejecución (Act): Busca el perfil por su UID de Firebase.
        val result = userDataSource.findByFirebaseUid(targetUid)
        // Verificación (Assert): Comprueba que la búsqueda encuentre el perfil numérico esperado.
        Truth.assertThat(result).isNotNull()
        Truth.assertThat(result?.id).isEqualTo(1002)
    }

    @Test
    fun updateFcmToken_validToken_updatesDatabase() = runTest {
        // Preparación (Arrange): Selecciona el perfil 1000 y un token FCM de prueba.
        val targetId = 1000
        val newToken = "test_fcm_token_123"
        // Ejecución (Act): Actualiza el token mediante la fuente de datos.
        userDataSource.updateFcmToken(targetId, newToken)
        // Verificación (Assert): Lee directamente Firestore para comprobar que el cambio quedó persistido.
        val updatedDoc = db.collection("users").document(targetId.toString()).get().await()
        Truth.assertThat(updatedDoc.getString("fcm_token")).isEqualTo(newToken)
    }

    /** Ejecuta la limpieza posterior a cada caso con el alcance definido por esta clase. */
    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        // Esta clase borra únicamente los tres perfiles que crea durante la preparación.
        val usersToDelete = listOf(1000, 1001, 1002)
        usersToDelete.forEach { id ->
            db.collection("users").document(id.toString()).delete().await()
        }
    }
}