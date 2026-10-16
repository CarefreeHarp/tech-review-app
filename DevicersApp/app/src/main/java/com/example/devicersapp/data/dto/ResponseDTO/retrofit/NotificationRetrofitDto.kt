package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para notificaciones; las asociaciones anidadas son opcionales según el endpoint. */
data class NotificationRetrofitDto(
    val id: Int,
    val user_id: Int,
    val actor_user_id: Int,
    val type: String,
    val review_id: Int? = null,
    val comment_id: Int? = null,
    val is_read: Boolean,
    val created_at: String? = null,
    val createdAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun NotificationRetrofitDto.toNotificationInfo(): NotificationInfo = NotificationInfo(
    id = id.toString(),
    userId = user_id,
    actorUserId = actor_user_id,
    type = type,
    reviewId = review_id,
    commentId = comment_id,
    isRead = is_read,
    createdAt = checkNotNull(created_at ?: createdAt) { "Fecha created_at ausente" }
)
