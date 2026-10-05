package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.CommentDto
import retrofit2.http.GET

/** Declara únicamente los endpoints de comentarios. */
interface CommentRetrofitService {
    @GET("comments")
    suspend fun getComments(): List<CommentDto>
}
