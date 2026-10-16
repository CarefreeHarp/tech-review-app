package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para comentarios; las asociaciones anidadas son opcionales según el endpoint. */
data class CommentRetrofitDto(
    val id: Int,
    val user_id: Int,
    val user_username: String? = null,
    val user_profile_image_url: String? = null,
    val parent_comment_id: Int? = null,
    val body: String,
    val is_active: Boolean,
    val created_at: String? = null,
    val updated_at: String? = null,
    val review_id: Int,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun CommentRetrofitDto.toCommentInfo(): CommentInfo = CommentInfo(
    id = id,
    userId = user_id,
    userUsername = user_username,
    userProfileImageUrl = user_profile_image_url,
    parentCommentId = parent_comment_id,
    body = body,
    isActive = is_active,
    createdAt = checkNotNull(created_at ?: createdAt) { "Fecha created_at ausente" },
    updatedAt = checkNotNull(updated_at ?: updatedAt) { "Fecha updated_at ausente" },
    reviewId = review_id
)
