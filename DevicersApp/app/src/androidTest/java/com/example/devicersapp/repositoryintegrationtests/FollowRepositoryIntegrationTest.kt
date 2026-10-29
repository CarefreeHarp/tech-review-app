package com.example.devicersapp.repositoryintegrationtests

import com.example.devicersapp.data.repository.FollowRepository
import com.example.devicersapp.data.datasource.implementations.firestore.FollowRemoteDataSourceFirestoreImplementation
import com.example.devicersapp.data.datasource.implementations.firestore.UserRemoteDataSourceFirestoreImplementation
import com.google.common.truth.Truth
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

/** Pruebas de integración de [FollowRepository] contra Firestore del emulador local. */
class FollowRepositoryIntegrationTest {
    private val db = getRepositoryTestFirestore()
    private val counters = db.collection("counters").document("next_id")
    private var originalCounters: Map<String, Any>? = null
    private lateinit var userDataSource: UserRemoteDataSourceFirestoreImplementation
    private lateinit var followRepository: FollowRepository

    @Before
    fun setUp() = runTest {
        // Si Firestore no apunta al emulador local, la prueba se detiene aquí, antes de escribir o borrar datos.
        Truth.assertThat(db.firestoreSettings.host).isEqualTo("10.0.2.2:8080")
        signInToRepositoryTestAuth(db)

        userDataSource = UserRemoteDataSourceFirestoreImplementation(db)
        followRepository = FollowRepository(FollowRemoteDataSourceFirestoreImplementation(db), userDataSource)

        // Conserva el contador local para restaurarlo en tearDown.
        originalCounters = counters.get().await().data

        // Datos replicables y autónomos: 3 usuarios que todavía no siguen a nadie.
        val batch = db.batch()
        repeat(3) { i ->
            val user = generateUser(i)
            batch.set(db.collection("users").document(user["id"].toString()), user)
        }
        // La notificación del seguimiento toma su ID de este contador; 1100 no choca con las de faker.
        // merge cambia solo este campo y conserva los demás contadores del documento.
        batch.set(counters, mapOf("next_notification_id" to 1100), SetOptions.merge())
        batch.commit().await()
    }

    @Test
    fun setFollowing_followUser_followersCountIncrement() = runTest {
        // Arrange
        val currentUserId = 1000
        val targetUserId = 1001
        val oldData = userDataSource.getById(targetUserId)
        // Act
        val result = followRepository.setFollowing(currentUserId, targetUserId, following = true)
        // Assert
        val targetUserResult = userDataSource.getById(targetUserId)
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(targetUserResult.followersCount).isGreaterThan(oldData.followersCount)
    }

    @Test
    fun setFollowing_unfollowUser_relationRemoved() = runTest {
        // Arrange
        val currentUserId = 1000
        val targetUserId = 1001
        followRepository.setFollowing(currentUserId, targetUserId, following = true)
        // Act
        val result = followRepository.setFollowing(currentUserId, targetUserId, following = false)
        // Assert
        val followedIds = followRepository.getFollowedUserIds(currentUserId)
        Truth.assertThat(result.isSuccess).isTrue()
        Truth.assertThat(result.getOrNull()?.isFollowing).isFalse()
        Truth.assertThat(followedIds).doesNotContain(targetUserId)
    }

    @After
    fun tearDown() = runTest {
        // JUnit ejecuta @After aunque falle @Before; nunca limpiar un destino distinto al emulador.
        if (db.firestoreSettings.host != "10.0.2.2:8080") {
            return@runTest
        }

        repeat(3) { i ->
            val user = db.collection("users").document((1000 + i).toString())
            // Firestore no borra las subcolecciones junto con el documento, así que se borran primero.
            val follows = user.collection("follows").get().await()
            follows.documents.forEach { doc ->
                user.collection("follows").document(doc.id).delete().await()
            }
            val followers = user.collection("followers").get().await()
            followers.documents.forEach { doc ->
                user.collection("followers").document(doc.id).delete().await()
            }
            user.delete().await()
        }
        // whereIn busca las notificaciones cuyo destinatario (user_id) es un usuario de prueba.
        val notifications = db.collection("notifications").whereIn("user_id", listOf(1000, 1001, 1002)).get().await()
        notifications.documents.forEach { doc ->
            db.collection("notifications").document(doc.id).delete().await()
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
