package com.example.devicersapp.datasourceintegrationtests

import com.example.devicersapp.data.datasource.FollowRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.firestore.FollowRemoteDataSourceFirestoreImplementation
import com.google.common.truth.Truth
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [FollowRemoteDataSource] contra Firestore del emulador local. */
class FollowRemoteDataSourceIntegrationTest {
    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private lateinit var followDataSource: FollowRemoteDataSourceFirestoreImplementation
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

        followDataSource = FollowRemoteDataSourceFirestoreImplementation(db)

        // Crea dos perfiles conocidos (1000 y 1001) para comprobar los dos sentidos del seguimiento.
        val batch = db.batch()
        batch.set(db.collection("users").document("1000"), generateUser(0))
        batch.set(db.collection("users").document("1001"), generateUser(1))
        batch.commit().await()
    }

    @Test
    fun setFollowing_true_addsFollowerAndFollowing() = runTest {
        // Preparación (Arrange): Selecciona al usuario 1000 como seguidor y al 1001 como destinatario.
        val followerId = 1000
        val followedId = 1001

        // Ejecución (Act): Crea el seguimiento mediante la fuente de datos.
        val result = followDataSource.setFollowing(followerId, followedId, true)

        // Verificación (Assert): Comprueba el resultado y los documentos de ambos sentidos de la relación.
        Truth.assertThat(result.isFollowing).isTrue()

        // Colección "follows" en el actor y "followers" en el objetivo
        val followDoc = db.collection("users").document(followerId.toString()).collection("follows").document(followedId.toString()).get().await()
        val followerDoc = db.collection("users").document(followedId.toString()).collection("followers").document(followerId.toString()).get().await()

        Truth.assertThat(followDoc.exists()).isTrue()
        Truth.assertThat(followerDoc.exists()).isTrue()
    }

    @Test
    fun setFollowing_false_removesFollowerAndFollowing() = runTest {
        // Preparación (Arrange): Crea primero la relación que luego se retirará.
        val followerId = 1000
        val followedId = 1001
        followDataSource.setFollowing(followerId, followedId, true) // Se siguen primero

        // Ejecución (Act): Solicita dejar de seguir al usuario 1001.
        val result = followDataSource.setFollowing(followerId, followedId, false)

        // Verificación (Assert): Comprueba que desaparezcan tanto follows como followers.
        Truth.assertThat(result.isFollowing).isFalse()

        val followDoc = db.collection("users").document(followerId.toString()).collection("follows").document(followedId.toString()).get().await()
        val followerDoc = db.collection("users").document(followedId.toString()).collection("followers").document(followerId.toString()).get().await()

        Truth.assertThat(followDoc.exists()).isFalse()
        Truth.assertThat(followerDoc.exists()).isFalse()
    }

    @Test
    fun getFollowers_userWithFollowers_returnsFollowInfoList() = runTest {
        // Preparación (Arrange): Crea un seguidor conocido para el usuario 1001.
        val followerId = 1000
        val followedId = 1001
        followDataSource.setFollowing(followerId, followedId, true)

        // Ejecución (Act): Consulta los seguidores del destinatario.
        val results = followDataSource.getFollowers(followedId)

        // Verificación (Assert): Verifica que la relación devuelta conserve los dos identificadores.
        Truth.assertThat(results).isNotEmpty()
        Truth.assertThat(results.first().followerId).isEqualTo(followerId)
        Truth.assertThat(results.first().followedId).isEqualTo(followedId)
    }

    @Test
    fun getFollowing_userFollowingOthers_returnsFollowInfoList() = runTest {
        // Preparación (Arrange): Hace que el usuario 1000 siga al usuario 1001.
        val followerId = 1000
        val followedId = 1001
        followDataSource.setFollowing(followerId, followedId, true)

        // Ejecución (Act): Consulta las cuentas seguidas por el actor.
        val results = followDataSource.getFollowing(followerId)

        // Verificación (Assert): Verifica que la lista incluya la relación preparada.
        Truth.assertThat(results).isNotEmpty()
        Truth.assertThat(results.first().followerId).isEqualTo(followerId)
        Truth.assertThat(results.first().followedId).isEqualTo(followedId)
    }

    /** Ejecuta la limpieza posterior a cada caso con el alcance definido por esta clase. */
    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        // La limpieza actual recorre todos los usuarios, no solo los IDs creados en setUp.
        val users = db.collection("users").get().await()
        for (userDoc in users) {
            // Firestore no elimina las subcolecciones al borrar su documento padre.
            val followers = userDoc.reference.collection("followers").get().await()
            for (follower in followers) {
                follower.reference.delete().await()
            }

            val follows = userDoc.reference.collection("follows").get().await()
            for (f in follows) {
                f.reference.delete().await()
            }

            userDoc.reference.delete().await()
        }

        // También se vacía toda la colección de notificaciones y se elimina el contador.
        val allNotifications = db.collection("notifications").get().await()
        for (notif in allNotifications) {
            notif.reference.delete().await()
        }

        sequenceRef.delete().await()
    }
}