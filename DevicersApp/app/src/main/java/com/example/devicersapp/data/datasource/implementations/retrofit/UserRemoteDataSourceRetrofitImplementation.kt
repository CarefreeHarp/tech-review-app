package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

/** Implementa usuarios con Express y convierte sus respuestas a los modelos comunes. */
class UserRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: UserRetrofitService
) : UserRemoteDataSource {
    override suspend fun getUsers(excludeUserId: Int?): List<UserInfo> = service.getUsers(excludeUserId).map { it.toUserInfo().copy(reviews = emptyList()) }
    override suspend fun getById(userId: Int): UserInfo = service.getById(userId).toUserInfo().copy(reviews = emptyList())
    override suspend fun getByIds(userIds: Set<Int>): Map<Int, UserInfo> {
        require(userIds.all { it > 0 })
        // Express no tiene endpoint de lectura por lote; solo se consultan los IDs solicitados.
        return userIds.mapNotNull { id ->
            try { getById(id) } catch (error: retrofit2.HttpException) {
                if (error.code() == 404) null else throw error
            }
        }.associateBy { it.id }
    }
    override suspend fun findByFirebaseUid(firebaseUid: String): UserInfo? = try {
        service.findByUid(firebaseUid).toUserInfo().copy(reviews = emptyList())
    } catch (error: retrofit2.HttpException) {
        if (error.code() == 404) null else throw error
    }
    override suspend fun createIfMissing(draft: UserDraft): UserInfo {
        val created = service.create(draft.toCreateUserRequestDto())
        return getById(created.id)
    }
    override suspend fun updateProfile(userId: Int, changes: UserChanges): UserInfo {
        service.update(userId, changes.toUpdateUserRequestDto())
        return getById(userId)
    }
    /** Express no guarda tokens FCM; las notificaciones push dependen de Firestore y de las Cloud Functions. */
    override suspend fun updateFcmToken(userId: Int, token: String?) {
        throw UnsupportedOperationException("Express no guarda tokens de notificaciones push.")
    }
}
