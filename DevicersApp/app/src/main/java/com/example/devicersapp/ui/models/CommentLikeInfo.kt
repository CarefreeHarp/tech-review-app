package com.example.devicersapp.ui.models
/** Reacción a un comentario; reviewId conserva el contexto de la ruta documental. */
data class CommentLikeInfo(val id: Int?, val userId: Int, val commentId: Int, val createdAt: String?, val reviewId: Int? = null)
