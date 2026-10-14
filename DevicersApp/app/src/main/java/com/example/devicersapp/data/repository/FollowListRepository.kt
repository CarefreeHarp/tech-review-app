package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.FollowListRemoteDataSource
import com.example.devicersapp.ui.models.FollowListSection
import com.example.devicersapp.ui.models.FollowListUser
import javax.inject.Inject

/** Selecciona la consulta de Firestore según la sección del perfil solicitado. */
class FollowListRepository @Inject constructor(private val source: FollowListRemoteDataSource) {
    suspend fun getUsers(profileId: Int, section: FollowListSection): List<FollowListUser> = when (section) {
        FollowListSection.FOLLOWERS -> source.getFollowers(profileId)
        FollowListSection.FOLLOWING -> source.getFollowing(profileId)
    }
}
