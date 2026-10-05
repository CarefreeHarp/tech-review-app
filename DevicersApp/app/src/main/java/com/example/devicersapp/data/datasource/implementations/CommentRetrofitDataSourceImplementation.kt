package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.CommentRemoteDataSource
import com.example.devicersapp.data.datasource.services.CommentRetrofitService
import com.example.devicersapp.data.dto.CommentDto
import javax.inject.Inject

/** Implementa el acceso a comentarios mediante su propio servicio Retrofit. */
class CommentRetrofitDataSourceImplementation @Inject constructor(
    private val service: CommentRetrofitService
) : CommentRemoteDataSource {
    override suspend fun getComments(): List<CommentDto> = service.getComments()
}
