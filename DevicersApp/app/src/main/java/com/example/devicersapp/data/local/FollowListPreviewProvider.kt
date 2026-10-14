package com.example.devicersapp.data.local

import com.example.devicersapp.data.datasource.FollowListRemoteDataSource
import com.example.devicersapp.data.repository.FollowListRepository
import com.example.devicersapp.ui.models.FollowListUser

/** Ofrece datos separados para previsualizar la lista sin consultar Firebase. */
class FollowListPreviewProvider(username: String, biography: String) {
    val user = FollowListUser(1, username, biography, null)
    val repository = FollowListRepository(object : FollowListRemoteDataSource {
        override suspend fun getFollowers(profileId: Int) = listOf(user)
        override suspend fun getFollowing(profileId: Int) = listOf(user)
    })
}
