package com.example.devicersapp.domain.usecase

import com.example.devicersapp.data.repository.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import com.example.devicersapp.data.dto.toProfileContent

/** Reúne el perfil y sus estadísticas con las relaciones consultadas en el repositorio de seguimiento. */
class ProfileContentUseCase @Inject constructor(
    private val users: UsersRepository,
    private val followers: FollowRepository
) {
    /** Construye el perfil y sus estadísticas a partir de consultas exitosas a la API. */
    suspend fun getProfileContent(userId: Int, reviewCount: Int) = coroutineScope {
        val user = async { users.getUserById(userId) }
        val follows = async { followers.getFollows() }
        val relations = follows.await()
        user.await().toProfileContent(
            reviewCount = reviewCount,
            followerCount = relations.count { it.followedId == userId },
            followingCount = relations.count { it.followerId == userId }
        )
    }}
