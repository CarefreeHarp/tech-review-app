package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de seguimientos; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface FollowRemoteDataSource {
    /** Establece el seguimiento deseado sin alternarlo de nuevo ante un reintento. */
    suspend fun setFollowing(followerId: Int, followedId: Int, following: Boolean): FollowStatus
    suspend fun getFollowers(userId: Int): List<FollowInfo>
    suspend fun getFollowing(userId: Int): List<FollowInfo>
}
