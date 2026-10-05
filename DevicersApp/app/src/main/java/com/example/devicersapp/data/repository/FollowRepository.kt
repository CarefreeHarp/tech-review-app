package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.FollowRemoteDataSource
import com.example.devicersapp.data.dto.toFollowInfo
import com.example.devicersapp.ui.models.FollowInfo
import javax.inject.Inject

/** Consulta seguimientos y traduce sus DTOs al modelo del front. */
class FollowRepository @Inject constructor(private val source: FollowRemoteDataSource) {
    suspend fun getFollows(): List<FollowInfo> = source.getFollows().map { it.toFollowInfo() }
}
