package com.example.devicersapp.ui.models

/**
 * Borrador que el front utiliza para publicar un comentario en una reseña.
 *
 * @param userId ID numérico del autor; su nombre y su foto los copia la fuente de datos al guardarlo.
 * @param parentCommentId Comentario al que responde, o null cuando responde directamente a la reseña.
 */
data class CommentDraft(
    val reviewId: Int,
    val userId: Int,
    val body: String,
    val parentCommentId: Int? = null,
)
