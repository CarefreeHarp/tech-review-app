package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.ReviewChanges

/** Cuerpo de actualización parcial; Gson omite los campos nulos. */
data class UpdateReviewRequestDto(
    val rating: Int? = null,
    val title: String? = null,
    val body: String? = null,
    val is_active: Boolean? = null,
    val user_id: Int? = null,
    val article_id: Int? = null,
)

/** Convierte los datos del formulario al cuerpo esperado por el backend. */
fun ReviewChanges.toUpdateReviewRequestDto(): UpdateReviewRequestDto = UpdateReviewRequestDto(
    rating = rating,
    title = title,
    body = body,
    is_active = isActive,
    user_id = userId,
    article_id = articleId,
)

/** Traduce el cuerpo de la petición al modelo de formulario del front. */
fun UpdateReviewRequestDto.toReviewChanges(): ReviewChanges = ReviewChanges(
    rating = rating,
    title = title,
    body = body,
    isActive = is_active,
    userId = user_id,
    articleId = article_id,
)
