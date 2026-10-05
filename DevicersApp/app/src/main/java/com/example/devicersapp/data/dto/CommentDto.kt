package com.example.devicersapp.data.dto

import com.example.devicersapp.R
import java.time.Instant
import com.example.devicersapp.ui.models.UserInfo
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.CommentInfo

/** Comentario publicado en una reseña; `parent_comment_id` es nulo si responde directamente a ella. */
data class CommentDto(
    val id: Int,
    val review_id: Int,
    val user_id: Int,
    val parent_comment_id: Int?,
    val body: String,
    val is_active: Boolean,
    val createdAt: String,
    val updatedAt: String,
)

/** Traduce los campos de la API al modelo de comentario del front. */
fun CommentDto.toCommentInfo(): CommentInfo = CommentInfo(
    id = id,
    reviewId = review_id,
    userId = user_id,
    parentCommentId = parent_comment_id,
    body = body,
    isActive = is_active,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

/** Adapta un comentario real a la respuesta visible, conservando su autor y su nivel en el hilo. */
fun CommentInfo.toReplyContent(author: UserInfo, depth: Int, likes: Int) =
    ReplyContent(
        authorId = userId.toString(),
        timeAgoResId = R.string.review_reply_time,
        textResId = R.string.review_reply_text_two,
        depth = depth,
        likes = likes,
        authorName = author.username,
        authorImageUrl = author.profileImageUrl,
        body = body,
        createdAtMillis = runCatching { Instant.parse(createdAt).toEpochMilli() }.getOrNull()
    )
