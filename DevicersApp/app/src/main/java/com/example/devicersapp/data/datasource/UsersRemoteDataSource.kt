package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.UserDto

/** Contrato de acceso remoto de users; los errores se propagan al consumidor. */
interface UsersRemoteDataSource {
    suspend fun getUsers(): List<UserDto>

    suspend fun getUserById(userId: Int): UserDto
}
