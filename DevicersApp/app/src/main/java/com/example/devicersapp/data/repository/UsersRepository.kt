package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.UsersRemoteDataSource
import com.example.devicersapp.data.dto.toUserInfo
import com.example.devicersapp.ui.models.UserInfo
import javax.inject.Inject

/** Consulta usuarios y traduce únicamente los DTOs de esta entidad. */
class UsersRepository @Inject constructor(private val source: UsersRemoteDataSource) {
    suspend fun getUsers(excludeUserId: Int? = null): List<UserInfo> = source.getUsers(excludeUserId).map { it.toUserInfo() }
    suspend fun getUserById(userId: Int): UserInfo = source.getUserById(userId).toUserInfo()
}
