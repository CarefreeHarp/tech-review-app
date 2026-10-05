package com.example.devicersapp.ui.models

/** Datos de usuario independientes del contrato JSON del backend. */
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
)
