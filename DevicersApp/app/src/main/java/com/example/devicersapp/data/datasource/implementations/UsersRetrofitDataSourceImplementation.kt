package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.UsersRemoteDataSource
import com.example.devicersapp.data.datasource.services.UsersRetrofitService
import com.example.devicersapp.data.dto.*
import javax.inject.Inject

class UsersRetrofitDataSourceImplementation
@Inject
constructor(private val service: UsersRetrofitService) : UsersRemoteDataSource {
    override suspend fun getUsers(): List<UserDto> = service.getUsers()

    override suspend fun getUserById(userId: Int): UserDto = service.getUserById(userId)
}
