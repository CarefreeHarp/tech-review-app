package com.example.devicersapp.ui.models

/**
 * Estado confirmado por la fuente de datos después de dar o quitar un like a una reseña.
 *
 * @param isLiked Indica si el usuario quedó con like sobre la reseña.
 * @param likes Cantidad de likes de la reseña después de la operación.
 */
data class ReviewLikeStatus(val isLiked: Boolean, val likes: Int)
