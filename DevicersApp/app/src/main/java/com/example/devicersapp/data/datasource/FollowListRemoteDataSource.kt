package com.example.devicersapp.data.datasource

import com.example.devicersapp.ui.models.FollowListUser

/** Define las lecturas de seguidores y seguidos de un perfil concreto. */
interface FollowListRemoteDataSource {
    suspend fun getFollowers(profileId: Int): List<FollowListUser>
    suspend fun getFollowing(profileId: Int): List<FollowListUser>
}
