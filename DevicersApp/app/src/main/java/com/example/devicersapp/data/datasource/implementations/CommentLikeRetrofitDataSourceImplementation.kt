package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.CommentLikeRemoteDataSource
import com.example.devicersapp.data.datasource.services.CommentLikeRetrofitService
import com.example.devicersapp.data.dto.CommentLikeDto
import javax.inject.Inject

/** Implementa el acceso a likes de comentarios mediante su propio servicio Retrofit. */
class CommentLikeRetrofitDataSourceImplementation @Inject constructor(
    private val service: CommentLikeRetrofitService
) : CommentLikeRemoteDataSource {
    override suspend fun getCommentLikes(): List<CommentLikeDto> = service.getCommentLikes()
}
