package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.FollowDto

/** Contrato remoto exclusivo de seguimientos; propaga los errores de consulta. */
interface FollowRemoteDataSource {
    suspend fun getFollows(): List<FollowDto>
}
