package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Consulta la relación en ambos sentidos y resuelve las identidades públicas de las listas. */
class FollowRepository(private val source: FollowRemoteDataSource, private val users: UserRemoteDataSource) {
    /** Confirma el seguimiento o propaga el fallo para revertir la presentación optimista. */
    suspend fun setFollowing(followerId: Int, followedId: Int, following: Boolean): Result<FollowStatus> =
        repositoryResult { source.setFollowing(followerId, followedId, following) }
    suspend fun getFollowers(userId: Int): List<FollowInfo> = source.getFollowers(userId)
    suspend fun getFollowing(userId: Int): List<FollowInfo> = source.getFollowing(userId)
    suspend fun getFollowedUserIds(userId: Int): Set<Int> = getFollowing(userId).map { it.followedId }.toSet()
    suspend fun getFollowerUsers(userId: Int): List<UserSummary> =
        publicUsers(getFollowers(userId).map { it.followerId }.toSet())
    suspend fun getFollowingUsers(userId: Int): List<UserSummary> = publicUsers(getFollowedUserIds(userId))
    private suspend fun publicUsers(ids: Set<Int>): List<UserSummary> = users.getByIds(ids).values
        .filter { it.isActive }.map { it.toUserSummary() }.sortedBy { it.username.lowercase() }
}
