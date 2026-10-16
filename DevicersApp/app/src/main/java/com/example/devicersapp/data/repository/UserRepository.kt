package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

import com.example.devicersapp.data.utils.Utils
import android.net.Uri
import java.util.UUID
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
/** Centraliza perfiles, proyecciones públicas y edición, usando la fuente seleccionada. */
class UserRepository(
    private val source: UserRemoteDataSource,
    private val reviews: ReviewRepository,
    private val storage: StorageRemoteDataSource
) {
    /** Resuelve el perfil por el UID actual de Auth sin conservar una copia global. */
    suspend fun getAuthenticatedUser(): UserInfo {
        val uid = Utils.getCurrentUserId()
        check(uid.isNotBlank()) { "No hay un usuario autenticado." }
        return checkNotNull(source.findByFirebaseUid(uid)) { "No existe el perfil del usuario autenticado." }
    }
    suspend fun getUsers(excludeUserId: Int? = null): List<UserInfo> = source.getUsers(excludeUserId)
    suspend fun getById(userId: Int): UserInfo = source.getById(userId)
    suspend fun getByIds(userIds: Set<Int>): Map<Int, UserInfo> = source.getByIds(userIds)
    suspend fun getSummaries(userIds: Set<Int>): List<UserSummary> =
        source.getByIds(userIds).values.filter { it.isActive }.map { it.toUserSummary() }
    suspend fun findByFirebaseUid(firebaseUid: String): UserInfo? = source.findByFirebaseUid(firebaseUid)
    suspend fun createIfMissing(draft: UserDraft): UserInfo = source.createIfMissing(draft)
    suspend fun updateProfile(userId: Int, changes: UserChanges): UserInfo {
        changes.username?.let { require(it.isNotBlank()) }
        return source.updateProfile(userId, changes)
    }
    /** Publica la foto en una ruta nueva y actualiza el perfil mediante la implementación elegida. */
    suspend fun saveProfile(userId: Int, firebaseUid: String, changes: UserChanges, photo: Uri?): UserInfo {
        val image = photo?.let { storage.uploadImage("profileImages/$firebaseUid/${UUID.randomUUID()}.jpg", it) }
        return updateProfile(userId, changes.copy(profileImageUrl = image ?: changes.profileImageUrl))
    }
    /** Agrega datos de usuario, reseñas y relaciones sin crear un repositorio por pantalla. */
    suspend fun getProfile(userId: Int): UserProfileInfo = coroutineScope {
        val user = async { getById(userId) }
        val records = async { reviews.getReviewSummariesByUser(userId) }
        UserProfileInfo(user.await(), records.await(), 0, 0, false)
    }
}
