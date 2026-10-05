package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.ReviewDraft

/** Cuerpo de creación: el backend recibe userId y articleId en camelCase. */
data class CreateReviewRequestDto(
    val userId: Int,
    val articleId: Int,
    val rating: Int,
    val body: String,
    val title: String? = null,
)

/** Convierte los datos del formulario al cuerpo esperado por el backend. */
fun ReviewDraft.toCreateReviewRequestDto(): CreateReviewRequestDto = CreateReviewRequestDto(
    userId = userId,
    articleId = articleId,
    rating = rating,
    body = body,
    title = title,
)

/** Traduce el cuerpo de la petición al modelo de formulario del front. */
fun CreateReviewRequestDto.toReviewDraft(): ReviewDraft = ReviewDraft(
    userId = userId,
    articleId = articleId,
    rating = rating,
    body = body,
    title = title,
)
