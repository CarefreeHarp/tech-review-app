package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.CommentLikeInfo

/** Campos de una reacción tal como llegan de `comment-likes`. */
data class CommentLikeDto(val id: Int, val user_id: Int, val comment_id: Int)

/** Traduce la reacción remota al modelo utilizado por el front. */
fun CommentLikeDto.toCommentLikeInfo() = CommentLikeInfo(id, user_id, comment_id)
