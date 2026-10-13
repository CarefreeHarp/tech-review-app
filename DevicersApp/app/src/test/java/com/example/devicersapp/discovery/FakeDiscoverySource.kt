package com.example.devicersapp.discovery

import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

open class FakeDiscoverySource : DiscoveryRemoteDataSource {
    var products = emptyList<ProductInfo>()
    var categories = emptyList<CategoryInfo>()
    var reviews = emptyList<ReviewInfo>()
    var users = emptyList<UserInfo>()
    var followed = emptySet<Int>()
    var notifications = emptyList<DiscoveryNotification>()
    override suspend fun getProducts() = products
    override suspend fun getCategories() = categories
    override suspend fun getActiveReviews() = reviews
    override suspend fun getUsers() = users
    override suspend fun getFollowedIds(userId: Int) = followed
    override suspend fun getNotifications(userId: Int) = notifications
}
