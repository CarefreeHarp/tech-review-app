package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Operaciones de likes de comentarios independientes de la tecnología de persistencia. */
class CommentLikeRepository(private val source: CommentLikeRemoteDataSource) {
    /** Confirma el like o entrega el fallo para restaurar el corazón y su conteo. */
    suspend fun sendCommentLike(reviewId: Int, commentId: Int, userId: Int): Result<CommentLikeStatus> =
        repositoryResult { source.sendCommentLike(reviewId, commentId, userId) }
    suspend fun getCommentLikes(reviewId: Int? = null, commentId: Int? = null): List<CommentLikeInfo> = source.getCommentLikes(reviewId, commentId)
}
