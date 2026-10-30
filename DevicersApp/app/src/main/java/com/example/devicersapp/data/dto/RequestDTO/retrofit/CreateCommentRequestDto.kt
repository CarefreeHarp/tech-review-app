package com.example.devicersapp.data.dto.RequestDTO.retrofit

import com.example.devicersapp.ui.models.CommentDraft

/** Cuerpo de POST reviews/{id}/comments: Express copia el nombre y la foto del autor al guardarlo. */
data class CreateCommentRequestDto(
    val user_id: Int,
    val body: String,
    val parent_comment_id: Int? = null,
)

/** Convierte el borrador al cuerpo esperado por el backend; la reseña viaja en la ruta. */
fun CommentDraft.toCreateCommentRequestDto(): CreateCommentRequestDto = CreateCommentRequestDto(
    user_id = userId,
    body = body,
    parent_comment_id = parentCommentId,
)
