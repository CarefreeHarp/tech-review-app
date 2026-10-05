package com.example.devicersapp.ui.models

/** Datos de comentario independientes del contrato JSON del backend. */
data class CommentInfo(
    val id: Int,
    val reviewId: Int,
    val userId: Int,
    val parentCommentId: Int?,
    val body: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
)
