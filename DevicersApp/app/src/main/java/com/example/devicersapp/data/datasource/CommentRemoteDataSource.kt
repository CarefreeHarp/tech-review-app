package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.CommentDto

/** Contrato remoto exclusivo de comentarios; propaga los errores de consulta. */
interface CommentRemoteDataSource {
    suspend fun getComments(): List<CommentDto>
}
