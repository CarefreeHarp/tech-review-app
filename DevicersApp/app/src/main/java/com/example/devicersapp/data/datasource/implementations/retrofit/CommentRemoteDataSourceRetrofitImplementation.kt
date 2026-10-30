package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa comentarios con Express y convierte sus respuestas a los modelos comunes. */
class CommentRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: CommentRetrofitService
) : CommentRemoteDataSource {
    override suspend fun getComments(reviewId: Int?): List<CommentInfo> = service.getComments()
        .filter { reviewId == null || it.review_id == reviewId }.map { it.toCommentInfo() }
    /** Express valida la reseña y el autor, y guarda el comentario con las copias de su perfil. */
    override suspend fun createComment(draft: CommentDraft): CommentInfo {
        require(draft.reviewId > 0 && draft.userId > 0 && draft.body.isNotBlank())
        draft.parentCommentId?.let { require(it > 0) }
        return service.create(draft.reviewId, draft.toCreateCommentRequestDto()).toCommentInfo()
    }
}
