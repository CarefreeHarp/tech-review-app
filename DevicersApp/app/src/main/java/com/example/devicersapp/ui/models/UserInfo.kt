package com.example.devicersapp.ui.models

/** Datos comunes de usuario; las asociaciones se resuelven mediante los repositorios. */
data class UserInfo(
    val id: Int,
    val email: String,
    val username: String,
    val firebaseUid: String?,
    val biography: String?,
    val profileImageUrl: String?,
    val notificationsLastViewedAt: String?,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val reviews: List<ReviewInfo> = emptyList(),
    val followersCount: Int = 0,
    val followsCount: Int = 0,
    val reviewBookmarksCount: Int = 0,
)
