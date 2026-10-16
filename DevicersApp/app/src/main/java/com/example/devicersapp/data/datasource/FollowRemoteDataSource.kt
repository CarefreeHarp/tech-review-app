package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de seguimientos; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface FollowRemoteDataSource {
    suspend fun getFollowers(userId: Int): List<FollowInfo>
    suspend fun getFollowing(userId: Int): List<FollowInfo>
}
