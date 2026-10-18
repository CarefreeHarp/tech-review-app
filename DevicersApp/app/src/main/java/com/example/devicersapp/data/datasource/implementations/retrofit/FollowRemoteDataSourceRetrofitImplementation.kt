package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa seguimientos con Express y convierte sus respuestas a los modelos comunes. */
class FollowRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: FollowRetrofitService
) : FollowRemoteDataSource {
    /** Express guarda la relación y su notificación de forma transaccional. */
    override suspend fun setFollowing(followerId: Int, followedId: Int, following: Boolean): FollowStatus =
        service.setFollowing(followedId, SetFollowingRequestDto(followerId, following)).toFollowStatus()
    override suspend fun getFollowers(userId: Int): List<FollowInfo> = service.getFollows(followedId = userId).map { it.toFollowInfo() }
    override suspend fun getFollowing(userId: Int): List<FollowInfo> = service.getFollows(followerId = userId).map { it.toFollowInfo() }
}
