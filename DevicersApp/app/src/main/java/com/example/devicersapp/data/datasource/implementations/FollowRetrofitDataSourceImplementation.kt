package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.FollowRemoteDataSource
import com.example.devicersapp.data.datasource.services.FollowRetrofitService
import com.example.devicersapp.data.dto.FollowDto
import javax.inject.Inject

/** Implementa el acceso a seguimientos mediante su propio servicio Retrofit. */
class FollowRetrofitDataSourceImplementation @Inject constructor(
    private val service: FollowRetrofitService
) : FollowRemoteDataSource {
    override suspend fun getFollows(): List<FollowDto> = service.getFollows()
}
