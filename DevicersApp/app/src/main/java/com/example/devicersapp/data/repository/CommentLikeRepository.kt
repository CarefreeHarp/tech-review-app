package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.CommentLikeRemoteDataSource
import com.example.devicersapp.data.dto.toCommentLikeInfo
import com.example.devicersapp.ui.models.CommentLikeInfo
import javax.inject.Inject

/** Consulta likes de comentarios y traduce sus DTOs al modelo del front. */
class CommentLikeRepository @Inject constructor(private val source: CommentLikeRemoteDataSource) {
    suspend fun getCommentLikes(): List<CommentLikeInfo> = source.getCommentLikes().map { it.toCommentLikeInfo() }
}
