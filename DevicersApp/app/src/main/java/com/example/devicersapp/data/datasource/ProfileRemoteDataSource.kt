package com.example.devicersapp.data.datasource

import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.models.UserInfo

/** Define las lecturas del perfil público sin depender de la API REST. */
interface ProfileRemoteDataSource {
    suspend fun getUser(userId: Int): UserInfo
    suspend fun getReviews(userId: Int): List<ReviewInfo>
    suspend fun getFollowCounts(userId: Int): Pair<Int, Int>
    suspend fun isFollowing(followerId: Int, followedId: Int): Boolean
}
