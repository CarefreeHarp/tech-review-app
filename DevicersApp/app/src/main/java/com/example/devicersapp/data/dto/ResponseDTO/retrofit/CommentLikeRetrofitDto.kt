package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para likes de comentarios; las asociaciones anidadas son opcionales según el endpoint. */
data class CommentLikeRetrofitDto(
    val user_id: Int,
    val created_at: String? = null,
    val id: Int,
    val comment_id: Int,
    val createdAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun CommentLikeRetrofitDto.toCommentLikeInfo(): CommentLikeInfo = CommentLikeInfo(
    userId = user_id,
    createdAt = created_at ?: createdAt,
    id = id,
    commentId = comment_id
)
