package com.example.devicersapp.ui.models

/** Reacciones y comentarios activos asociados al detalle de una reseña. */
data class ReviewInteractions(
    val likes: Int,
    val comments: List<CommentInfo>,
    val commentLikes: Map<Int, Int>
)
