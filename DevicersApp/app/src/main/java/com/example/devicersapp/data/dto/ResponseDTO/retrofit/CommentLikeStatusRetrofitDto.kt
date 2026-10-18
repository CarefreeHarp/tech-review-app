package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.CommentLikeStatus
/** Estado final de la transacción SQL del like de un comentario. */
data class CommentLikeStatusRetrofitDto(val review_id: Int, val comment_id: Int, val user_id: Int,
    val is_liked: Boolean, val likes_count: Int)
/** Proyecta el contrato HTTP al mismo modelo que devuelve Firestore. */
fun CommentLikeStatusRetrofitDto.toCommentLikeStatus() = CommentLikeStatus(is_liked, likes_count)
