package com.example.devicersapp.ui.models
/** Reacción identificada por reseña y usuario; el ID independiente solo existe en SQL. */
data class ReviewLikeInfo(val id: Int?, val userId: Int, val reviewId: Int, val createdAt: String?)
