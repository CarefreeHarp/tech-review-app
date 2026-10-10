package com.example.devicersapp.data.datasource

import com.example.devicersapp.ui.models.UserInfo

/** Define la persistencia del perfil que vincula una cuenta de Auth con su ID de aplicación. */
interface UserProfileRemoteDataSource {
    /** Recupera desde el servidor el documento correspondiente al ID de aplicación. */
    suspend fun getById(userId: Int): UserInfo

    /** Busca por UID sin asociar cuentas mediante su correo ni modificar perfiles existentes. */
    suspend fun findByFirebaseUid(firebaseUid: String): UserInfo?

    /** Crea el perfil de forma repetible; conserva el mismo documento al reintentar. */
    suspend fun createIfMissing(
        firebaseUid: String,
        email: String,
        username: String,
        profileImageUrl: String?
    ): UserInfo

    /** Actualiza únicamente el nombre o la imagen del propietario indicado. */
    suspend fun updateProfile(
        userId: Int,
        firebaseUid: String,
        username: String? = null,
        profileImageUrl: String? = null
    ): UserInfo
}
