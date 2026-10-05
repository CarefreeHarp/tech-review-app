package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.FollowDto
import retrofit2.http.GET

/** Declara únicamente los endpoints de seguimientos. */
interface FollowRetrofitService {
    @GET("follows")
    suspend fun getFollows(): List<FollowDto>
}
