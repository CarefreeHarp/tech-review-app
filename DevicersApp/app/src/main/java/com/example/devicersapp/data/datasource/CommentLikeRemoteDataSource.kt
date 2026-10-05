package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.CommentLikeDto

/** Contrato remoto exclusivo de likes de comentarios; propaga los errores de consulta. */
interface CommentLikeRemoteDataSource {
    suspend fun getCommentLikes(): List<CommentLikeDto>
}
