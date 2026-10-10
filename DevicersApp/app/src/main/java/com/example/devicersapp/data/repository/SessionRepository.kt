package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.UserProfileRemoteDataSource
import com.example.devicersapp.ui.models.UserInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Comparte el perfil de Firestore y su relación UID/ID entre los ViewModels de la sesión. */
@Singleton
class SessionRepository @Inject constructor(
    private val profiles: UserProfileRemoteDataSource
) {
    private val _currentProfile = MutableStateFlow<UserInfo?>(null)
    val currentProfile = _currentProfile.asStateFlow()
    private var revision = 0L

    /** Exige la identidad real antes de realizar una operación que pertenece a un usuario. */
    fun requireCurrentProfile(): UserInfo = checkNotNull(currentProfile.value) {
        "No se ha recuperado el perfil de la sesión actual."
    }

    /** Recupera el perfil o completa el registro de una cuenta que todavía solo existe en Auth. */
    suspend fun loadProfile(
        firebaseUid: String,
        email: String,
        username: String,
        profileImageUrl: String?
    ): UserInfo {
        val expectedRevision = revision
        val profile = profiles.findByFirebaseUid(firebaseUid)
            ?: profiles.createIfMissing(firebaseUid, email, username, profileImageUrl)
        publish(profile, firebaseUid, expectedRevision)
        return profile
    }

    /** Vuelve a consultar Firestore por el ID de sesión y renueva la identidad compartida. */
    suspend fun refreshCurrentProfile(): UserInfo {
        val current = requireCurrentProfile()
        val uid = checkNotNull(current.firebaseUid)
        val expectedRevision = revision
        val refreshed = profiles.getById(current.id)
        check(refreshed.id == current.id) { "La consulta cambió el ID del usuario." }
        publish(refreshed, uid, expectedRevision)
        return refreshed
    }

    /** Mantiene el perfil compartido al cambiar el nombre o la foto de la cuenta. */
    suspend fun updateProfile(username: String? = null, profileImageUrl: String? = null) {
        val profile = checkNotNull(_currentProfile.value) { "No se ha recuperado el perfil." }
        val uid = checkNotNull(profile.firebaseUid)
        val expectedRevision = revision
        val updated = profiles.updateProfile(profile.id, uid, username, profileImageUrl)
        check(updated.id == profile.id) { "La actualización cambió el ID del usuario." }
        publish(updated, uid, expectedRevision)
    }

    /** Invalida las cargas pendientes para que no restauren una sesión cerrada o anterior. */
    fun clear() {
        revision++
        _currentProfile.value = null
    }

    private fun publish(profile: UserInfo, firebaseUid: String, expectedRevision: Long) {
        if (expectedRevision != revision) throw CancellationException("La sesión cambió durante la consulta.")
        check(profile.firebaseUid == firebaseUid && profile.id > 0 && profile.isActive) {
            "El perfil recuperado no corresponde a una cuenta activa del usuario."
        }
        _currentProfile.value = profile
    }
}
