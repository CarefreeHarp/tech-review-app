package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de usuarios; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface UserRemoteDataSource {
    suspend fun getUsers(excludeUserId: Int? = null): List<UserInfo>
    suspend fun getById(userId: Int): UserInfo
    suspend fun getByIds(userIds: Set<Int>): Map<Int, UserInfo>
    suspend fun findByFirebaseUid(firebaseUid: String): UserInfo?
    suspend fun createIfMissing(draft: UserDraft): UserInfo
    suspend fun updateProfile(userId: Int, changes: UserChanges): UserInfo
}
