package com.example.devicersapp.data.dto

import com.google.gson.annotations.SerializedName

/** Datos de la API; las relaciones anidadas solo están presentes en algunas consultas. */
data class UserDto(
    val id: Int,
    val email: String,
    val username: String,
    @SerializedName("firebase_uid") val firebaseUid: String?,
    val biography: String?,
    @SerializedName("profile_image_url") val profileImageUrl: String?,
    @SerializedName("notifications_last_viewed_at") val notificationsLastViewedAt: String?,
    @SerializedName("is_active") val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val reviews: List<ReviewDto>? = null,
)
