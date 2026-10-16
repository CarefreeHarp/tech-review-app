package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa likes de comentarios con Express y convierte sus respuestas a los modelos comunes. */
class CommentLikeRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: CommentLikeRetrofitService,
    private val commentsService: CommentRetrofitService
) : CommentLikeRemoteDataSource {
    override suspend fun getCommentLikes(reviewId: Int?, commentId: Int?): List<CommentLikeInfo> {
        val comments = commentsService.getComments().associate { it.id to it.review_id }
        return service.getCommentLikes().filter {
            (commentId == null || it.comment_id == commentId) && (reviewId == null || comments[it.comment_id] == reviewId)
        }.map { it.toCommentLikeInfo().copy(reviewId = checkNotNull(comments[it.comment_id])) }
    }
}
