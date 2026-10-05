package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.CommentLikeDto
import retrofit2.http.GET

/** Declara únicamente los endpoints de likes de comentarios. */
interface CommentLikeRetrofitService {
    @GET("comment-likes")
    suspend fun getCommentLikes(): List<CommentLikeDto>
}
