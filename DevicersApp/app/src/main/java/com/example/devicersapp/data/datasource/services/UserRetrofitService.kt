package com.example.devicersapp.data.datasource.services
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import retrofit2.http.*
/** Endpoints existentes de usuarios; los cuerpos y respuestas permanecen en la frontera HTTP. */
interface UserRetrofitService {
    @GET("users") suspend fun getUsers(@Query("excludeUserId") excludeUserId: Int? = null): List<UserRetrofitDto>
    @GET("users/{id}") suspend fun getById(@Path("id") id: Int): UserRetrofitDto
    @GET("users/by-firebase-uid/{uid}") suspend fun findByUid(@Path("uid") uid: String): UserRetrofitDto
    @POST("users") suspend fun create(@Body request: CreateUserRequestDto): UserRetrofitDto
    @PUT("users/{id}") suspend fun update(@Path("id") id: Int, @Body request: UpdateUserRequestDto): UserRetrofitDto
}
