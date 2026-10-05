package com.example.devicersapp.ui.models

/** Datos de reacción independientes del contrato JSON del backend. */
data class ReviewLikeInfo(
    val id: Int,
    val userId: Int,
    val reviewId: Int,
)
