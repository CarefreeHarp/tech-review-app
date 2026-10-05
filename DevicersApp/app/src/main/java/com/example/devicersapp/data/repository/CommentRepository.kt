package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.CommentRemoteDataSource
import com.example.devicersapp.data.dto.toCommentInfo
import com.example.devicersapp.ui.models.CommentInfo
import javax.inject.Inject

/** Consulta comentarios y traduce sus DTOs al modelo del front. */
class CommentRepository @Inject constructor(private val source: CommentRemoteDataSource) {
    suspend fun getComments(): List<CommentInfo> = source.getComments().map { it.toCommentInfo() }
}
