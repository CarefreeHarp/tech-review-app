package com.example.devicersapp.data.dto

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.UserInfo

/**
 * Comentario del hilo de una reseña.
 *
 * @param userUsername Nombre copiado al comentar; solo se usa si el perfil vigente no está disponible.
 * @param parentCommentId Comentario al que responde, o `null` si responde directamente a la reseña.
 */
data class CommentDocumentDto(
    val id: Int,
    val userId: Int,
    val userUsername: String,
    val parentCommentId: Int?,
    val body: String,
    val isActive: Boolean,
    val createdAt: String
)

/** Adapta el comentario a la respuesta visible, conservando su autor y su nivel en el hilo. */
fun CommentDocumentDto.toReplyContent(author: UserInfo, depth: Int, likes: Int) =
    ReplyContent(
        authorId = userId.toString(),
        timeAgoResId = R.string.review_reply_time,
        textResId = R.string.review_reply_text_two,
        depth = depth,
        likes = likes,
        authorName = author.username,
        authorImageUrl = author.profileImageUrl,
        body = body,
        createdAtMillis = createdAt.toEpochMillisOrNull()
    )
