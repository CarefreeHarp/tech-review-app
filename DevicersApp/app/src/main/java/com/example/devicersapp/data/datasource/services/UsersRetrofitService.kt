package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.UserDto
import retrofit2.http.GET
import retrofit2.http.Path

/** Endpoints de users del backend; las respuestas HTTP fallidas lanzan HttpException. */
interface UsersRetrofitService {
    @GET("users")
    suspend fun getUsers(): List<UserDto>

    @GET("users/{userId}")
    suspend fun getUserById(@Path("userId") userId: Int): UserDto
}
