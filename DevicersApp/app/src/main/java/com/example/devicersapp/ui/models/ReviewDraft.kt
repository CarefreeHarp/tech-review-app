package com.example.devicersapp.ui.models

/** Borrador completo que el front utiliza para publicar una reseña. */
data class ReviewDraft(
    val userId: Int,
    val articleId: Int,
    val rating: Int,
    val body: String,
    val title: String? = null,
)

/** Cambios opcionales que el front solicita para una reseña existente. */
data class ReviewChanges(
    val rating: Int? = null,
    val title: String? = null,
    val body: String? = null,
    val isActive: Boolean? = null,
    val userId: Int? = null,
    val articleId: Int? = null,
)
