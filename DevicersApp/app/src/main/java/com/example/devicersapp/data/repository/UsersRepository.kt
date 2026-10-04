package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.UsersRemoteDataSource
import com.example.devicersapp.data.dto.*
import javax.inject.Inject

class UsersRepository @Inject constructor(private val source: UsersRemoteDataSource) {
    suspend fun getUsers(): List<UserDto> = source.getUsers()

    suspend fun getUserById(userId: Int): UserDto = source.getUserById(userId)
}
