package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para usuarios; las asociaciones anidadas son opcionales según el endpoint. */
data class UserRetrofitDto(
    val id: Int,
    val firebase_uid: String? = null,
    val email: String,
    val username: String,
    val biography: String? = null,
    val profile_image_url: String? = null,
    val notifications_last_viewed_at: String? = null,
    val is_active: Boolean,
    val created_at: String? = null,
    val updated_at: String? = null,
    val reviews: List<ReviewRetrofitDto>? = null,
    val followers_count: Int = 0,
    val follows_count: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun UserRetrofitDto.toUserInfo(): UserInfo = UserInfo(
    id = id,
    firebaseUid = firebase_uid,
    email = email,
    username = username,
    biography = biography,
    profileImageUrl = profile_image_url,
    notificationsLastViewedAt = notifications_last_viewed_at,
    isActive = is_active,
    createdAt = checkNotNull(created_at ?: createdAt) { "Fecha created_at ausente" },
    updatedAt = checkNotNull(updated_at ?: updatedAt) { "Fecha updated_at ausente" },
    reviews = reviews.orEmpty().map { it.toReviewInfo() },
    followersCount = followers_count,
    followsCount = follows_count
)

/** Proyección pública del mismo DTO de usuario. */
fun UserRetrofitDto.toUserSummary() = UserSummary(id, username, biography, profile_image_url)
