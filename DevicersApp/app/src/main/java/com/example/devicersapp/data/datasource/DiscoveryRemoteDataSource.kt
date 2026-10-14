package com.example.devicersapp.data.datasource

import com.example.devicersapp.ui.models.CategoryInfo
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.models.UserInfo

/** Contrato de lectura exclusivo de las tareas 12, 13 y 14. */
interface DiscoveryRemoteDataSource {
    suspend fun getProducts(): List<ProductInfo>
    suspend fun getCategories(): List<CategoryInfo>
    suspend fun getActiveReviews(): List<ReviewInfo>
    suspend fun getUsers(): List<UserInfo>
    suspend fun getFollowedIds(userId: Int): Set<Int>
    suspend fun getNotifications(userId: Int): List<DiscoveryNotification>
}

data class DiscoveryNotification(
    val id: String,
    val recipientId: Int,
    val actorId: Int,
    val type: String,
    val reviewId: Int?,
    val time: Long
)
