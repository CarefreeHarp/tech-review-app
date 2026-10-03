package com.example.devicersapp.data.dto

/** Cuerpo de creación: el backend recibe userId y articleId en camelCase. */
data class CreateReviewRequestDto(
    val userId: Int,
    val articleId: Int,
    val rating: Int,
    val body: String,
    val title: String? = null,
)
