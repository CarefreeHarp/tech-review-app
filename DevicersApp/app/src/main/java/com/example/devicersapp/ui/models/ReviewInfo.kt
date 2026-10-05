package com.example.devicersapp.ui.models

/** Datos de reseña independientes del contrato JSON del backend. */
data class ReviewInfo(
    val id: Int,
    val userId: Int,
    val articleId: Int,
    val rating: Int,
    val title: String?,
    val body: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val user: UserInfo? = null,
    val article: ProductInfo? = null,
    val likes: Int? = null,
    val comments: List<ReplyContent>? = null,
)
