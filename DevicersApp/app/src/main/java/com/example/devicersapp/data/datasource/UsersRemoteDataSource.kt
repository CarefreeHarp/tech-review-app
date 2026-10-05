package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.UserDto

/** Contrato remoto exclusivo de la entidad Users; propaga errores de consulta. */
interface UsersRemoteDataSource {
    suspend fun getUsers(excludeUserId: Int? = null): List<UserDto>
    suspend fun getUserById(userId: Int): UserDto
}
