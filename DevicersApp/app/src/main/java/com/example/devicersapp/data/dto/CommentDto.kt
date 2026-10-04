package com.example.devicersapp.data.dto

import com.google.gson.annotations.SerializedName

/** Comentario publicado en una reseña; `parentCommentId` es nulo si responde directamente a ella. */
data class CommentDto(
    val id: Int,
    @SerializedName("review_id") val reviewId: Int,
    @SerializedName("user_id") val userId: Int,
    @SerializedName("parent_comment_id") val parentCommentId: Int?,
    val body: String,
    @SerializedName("is_active") val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
)
