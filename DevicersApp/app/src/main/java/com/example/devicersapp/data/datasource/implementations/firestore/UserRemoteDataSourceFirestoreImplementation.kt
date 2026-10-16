package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de usuarios con las rutas y campos de Firestore v9. */
class UserRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {
    private val users get() = firestore.collection("users")
    override suspend fun getUsers(excludeUserId: Int?): List<UserInfo> =
        users.get(Source.SERVER).await().documents.map { it.toUserFirestoreDto().toUserInfo() }.filter { it.id != excludeUserId }

    override suspend fun getById(userId: Int): UserInfo {
        require(userId > 0)
        return users.document(userId.toString()).get(Source.SERVER).await().also { snapshot ->
            if (!snapshot.exists()) throw NoSuchElementException("No existe ${snapshot.reference.path}")
        }.toUserFirestoreDto().toUserInfo()
    }

    override suspend fun getByIds(userIds: Set<Int>): Map<Int, UserInfo> {
        require(userIds.all { it > 0 })
        return userIds.chunked(10).flatMap { batch ->
            users.whereIn(FieldPath.documentId(), batch.map(Int::toString)).get(Source.SERVER).await().documents
                .map { it.toUserFirestoreDto().toUserInfo() }
        }.associateBy { it.id }
    }

    override suspend fun findByFirebaseUid(firebaseUid: String): UserInfo? {
        require(firebaseUid.isNotBlank())
        val found = users.whereEqualTo("firebase_uid", firebaseUid).limit(2).get(Source.SERVER).await().documents
        check(found.size <= 1) { "Hay varios perfiles asociados al mismo UID." }
        return found.singleOrNull()?.toUserFirestoreDto()?.toUserInfo()
    }

    override suspend fun createIfMissing(draft: UserDraft): UserInfo {
        require(draft.firebaseUid.isNotBlank() && draft.username.isNotBlank())
        val sequence = firestore.collection("counters").document("next_id")
        while (true) {
            // La búsqueda por UID debe repetirse si otra creación avanzó el contador compartido.
            val counter = sequence.get(Source.SERVER).await()
            if (!counter.exists()) throw NoSuchElementException("No existe ${sequence.path}")
            val expected = counter.integer("next_user_id")
            findByFirebaseUid(draft.firebaseUid)?.let { return it }
            val id = firestore.runTransaction { transaction ->
                val next = transaction.get(sequence).integer("next_user_id")
                if (next != expected) return@runTransaction null
                check(next > 0)
                var candidate = next
                while (transaction.get(users.document(candidate.toString())).exists()) {
                    check(candidate < Int.MAX_VALUE) { "Se agotaron los IDs de usuario." }
                    candidate++
                }
                transaction.set(users.document(candidate.toString()), draft.toCreateUserFirestoreDto().fields(candidate))
                transaction.set(sequence, mapOf("next_user_id" to candidate.toLong() + 1), SetOptions.merge())
                candidate
            }.await()
            if (id != null) return getById(id)
        }
    }

    override suspend fun updateProfile(userId: Int, changes: UserChanges): UserInfo {
        require(userId > 0)
        val reference = users.document(userId.toString())
        firestore.runTransaction { transaction ->
            val existing = transaction.get(reference)
            check(existing.exists()) { "No existe el perfil solicitado." }
            transaction.update(reference, changes.toUpdateUserFirestoreDto().fields())
        }.await()
        return getById(userId)
    }
}
