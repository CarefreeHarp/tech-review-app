package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.UserProfileRemoteDataSource
import com.example.devicersapp.data.datasource.ProfileImagesRemoteDataSource
import com.google.firebase.firestore.FieldPath
import com.example.devicersapp.ui.models.UserInfo
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Guarda perfiles planos en users y conserva los IDs numéricos de los datos migrados. */
class UserProfileFirestoreDataSourceImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserProfileRemoteDataSource, ProfileImagesRemoteDataSource {
    private val users get() = firestore.collection("users")

    /** Agrupa IDs únicos y lee solo los perfiles solicitados, nunca las copias del backend. */
    override suspend fun getImagesByIds(userIds: Set<Int>): Map<Int, String?> {
        require(userIds.all { it > 0 })
        val images = userIds.associateWith<Int, String?> { null }.toMutableMap()
        for (batch in userIds.chunked(10)) {
            val documents = users.whereIn(FieldPath.documentId(), batch.map(Int::toString))
                .get(Source.SERVER).await().documents
            for (document in documents) {
                images[document.id.toInt()] = document.getString("profile_image_url")
                    ?.trim()?.takeIf { it.isNotEmpty() }
            }
        }
        return images
    }

    override suspend fun getById(userId: Int): UserInfo {
        require(userId > 0)
        val document = users.document(userId.toString()).get(Source.SERVER).await()
        check(document.exists()) { "No existe el perfil solicitado." }
        return document.toUserInfo().also {
            check(it.id == userId) { "El ID del perfil no coincide con su documento." }
        }
    }

    override suspend fun findByFirebaseUid(firebaseUid: String): UserInfo? {
        val matches = users.whereEqualTo("firebase_uid", firebaseUid).limit(2)
            .get(Source.SERVER).await().documents
        check(matches.size <= 1) { "Hay varios perfiles asociados al mismo UID." }
        return matches.singleOrNull()?.toUserInfo()
    }

    override suspend fun createIfMissing(
        firebaseUid: String,
        email: String,
        username: String,
        profileImageUrl: String?
    ): UserInfo {
        require(firebaseUid.isNotBlank())
        val sequence = firestore.collection("metadata").document("user_ids")
        while (true) {
            // Lee la versión del contador antes de buscar el UID. Si otra creación
            // avanza la secuencia, repite también la búsqueda para no duplicar el perfil.
            val expectedLastId = sequence.get(Source.SERVER).await().getLong("last_id") ?: 0L
            findByFirebaseUid(firebaseUid)?.let { return it }
            val reference = firestore.runTransaction { transaction ->
                val lastId = transaction.get(sequence).getLong("last_id") ?: 0L
                if (lastId != expectedLastId) return@runTransaction null
                check(lastId in 0 until Int.MAX_VALUE.toLong()) { "Se agotaron los IDs de usuario." }
                var candidate = lastId + 1
                var ref = users.document(candidate.toString())
                // Al iniciar la secuencia, respeta los documentos previamente migrados.
                while (transaction.get(ref).exists()) {
                    check(candidate < Int.MAX_VALUE) { "Se agotaron los IDs de usuario." }
                    candidate++
                    ref = users.document(candidate.toString())
                }
                // El perfil y el contador se guardan juntos: un registro fallido no consume ID.
                val creationTime = FieldValue.serverTimestamp()
                transaction.set(ref, mapOf(
                    "id" to candidate,
                    "firebase_uid" to firebaseUid,
                    "email" to email,
                    "username" to username.trim(),
                    "biography" to "",
                    "profile_image_url" to profileImageUrl,
                    "notifications_last_viewed_at" to null,
                    "is_active" to true,
                    "createdAt" to creationTime,
                    "updatedAt" to creationTime
                ))
                transaction.set(sequence, mapOf("last_id" to candidate))
                ref
            }.await()
            if (reference != null) return reference.get(Source.SERVER).await().toUserInfo()
        }
    }

    override suspend fun updateProfile(
        userId: Int,
        firebaseUid: String,
        username: String?,
        profileImageUrl: String?
    ): UserInfo {
        val reference = users.document(userId.toString())
        firestore.runTransaction { transaction ->
            val existing = transaction.get(reference)
            check(existing.getString("firebase_uid") == firebaseUid) {
                "El perfil no pertenece al usuario autenticado."
            }
            val changes = mutableMapOf<String, Any>("updatedAt" to FieldValue.serverTimestamp())
            username?.let { changes["username"] = it.trim() }
            profileImageUrl?.let { changes["profile_image_url"] = it }
            transaction.update(reference, changes)
        }.await()
        return reference.get(Source.SERVER).await().toUserInfo()
    }

    /** Convierte los Timestamp de Firestore sin cambiar el modelo que ya consume la app. */
    private fun DocumentSnapshot.toUserInfo(): UserInfo {
        val numericId = checkNotNull(getLong("id")) { "El perfil no tiene ID numérico." }
        check(numericId in 1..Int.MAX_VALUE.toLong()) { "El ID del perfil no es válido." }
        return UserInfo(
            id = numericId.toInt(),
            firebaseUid = checkNotNull(getString("firebase_uid")),
            email = getString("email").orEmpty(),
            username = getString("username").orEmpty(),
            biography = getString("biography"),
            profileImageUrl = getString("profile_image_url"),
            notificationsLastViewedAt = timestampText("notifications_last_viewed_at"),
            isActive = getBoolean("is_active") ?: true,
            createdAt = timestampText("createdAt").orEmpty(),
            updatedAt = timestampText("updatedAt").orEmpty()
        )
    }

    private fun DocumentSnapshot.timestampText(field: String): String? =
        when (val value = get(field)) {
            is com.google.firebase.Timestamp -> value.toDate().toInstant().toString()
            is String -> value
            else -> null
        }
}
