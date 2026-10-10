package com.example.devicersapp.data.repository

import com.example.devicersapp.data.dto.toProfileContent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import com.example.devicersapp.data.datasource.UsersRemoteDataSource
import com.example.devicersapp.data.datasource.ProfileImagesRemoteDataSource
import com.example.devicersapp.data.dto.toUserInfo
import com.example.devicersapp.ui.models.UserInfo
import javax.inject.Inject

/** Consulta usuarios y construye sus perfiles con las estadísticas de seguimiento. */
class UsersRepository @Inject constructor(
    private val source: UsersRemoteDataSource,
    private val profileImages: ProfileImagesRemoteDataSource
) {
    /** Conserva los datos de la API y reemplaza sus fotos por las vigentes en Firestore. */
    suspend fun getUsers(excludeUserId: Int? = null): List<UserInfo> {
        val users = source.getUsers(excludeUserId).map { it.toUserInfo() }
        val images = getProfileImages(users.map { it.id }.toSet())
        return users.map { it.copy(profileImageUrl = images[it.id]) }
    }

    suspend fun getUserById(userId: Int): UserInfo {
        val user = source.getUserById(userId).toUserInfo()
        return user.copy(profileImageUrl = getProfileImages(setOf(userId))[userId])
    }

    /** Permite cargar fotos de autores sin consultar otra vez sus datos personales en la API. */
    suspend fun getProfileImages(userIds: Set<Int>): Map<Int, String?> =
        if (userIds.isEmpty()) emptyMap() else profileImages.getImagesByIds(userIds)

    /** Construye el perfil y sus estadísticas a partir de consultas exitosas a la API. */
    suspend fun getProfileContent(
        userId: Int,
        reviewCount: Int,
        followers: FollowRepository
    ) = coroutineScope {
        val user = async { getUserById(userId) }
        val follows = async { followers.getFollows() }
        val relations = follows.await()
        user.await().toProfileContent(
            reviewCount = reviewCount,
            followerCount = relations.count { it.followedId == userId },
            followingCount = relations.count { it.followerId == userId }
        )
    }
}
