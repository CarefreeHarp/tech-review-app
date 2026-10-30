package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

import java.time.Instant
/** Arma los hilos y resuelve las fotos ausentes sin modificar la copia persistida del comentario. */
class CommentRepository(
    private val source: CommentRemoteDataSource,
    private val users: UserRemoteDataSource,
    private val likes: CommentLikeRemoteDataSource
) {
    suspend fun getComments(reviewId: Int? = null): List<CommentInfo> = source.getComments(reviewId)
    /** Publica el texto sin espacios sobrantes o entrega el fallo para conservar el borrador en pantalla. */
    suspend fun createComment(draft: CommentDraft): Result<CommentInfo> = repositoryResult {
        require(draft.body.isNotBlank()) { "El comentario no puede estar vacío." }
        source.createComment(draft.copy(body = draft.body.trim()))
    }
    /** Las respuestas huérfanas quedan en la raíz y los ciclos no repiten comentarios. */
    suspend fun getThread(reviewId: Int, currentUserId: Int? = null): List<CommentThreadItem> {
        val likedIds = if (currentUserId == null) emptySet() else likes.getCommentLikes(reviewId)
            .filter { it.userId == currentUserId }.map { it.commentId }.toSet()
        val comments = getComments(reviewId).filter { it.isActive }
            .sortedWith(compareBy({ Instant.parse(it.createdAt) }, { it.id }))
        // Un mismo autor puede comentar varias veces: se resuelve cada ID solo una vez por carga.
        val missingPhotoUserIds = comments.filter { it.userProfileImageUrl.isNullOrBlank() }
            .map { it.userId }.toSet()
        val authors = if (missingPhotoUserIds.isEmpty()) emptyMap() else users.getByIds(missingPhotoUserIds)
        val commentsByParentId = comments.groupBy { it.parentCommentId }
        val activeCommentIds = comments.map { it.id }.toSet()
        val visitedCommentIds = mutableSetOf<Int>()
        val thread = mutableListOf<CommentThreadItem>()
        fun appendComment(comment: CommentInfo, depth: Int) {
            if (!visitedCommentIds.add(comment.id)) return
            thread += CommentThreadItem(
                comment = comment,
                depth = depth,
                likes = comment.likesCount,
                isLiked = comment.id in likedIds,
                authorImageUrl = comment.userProfileImageUrl?.takeIf { it.isNotBlank() }
                    ?: authors[comment.userId]?.profileImageUrl
            )
            commentsByParentId[comment.id].orEmpty().forEach { appendComment(it, depth + 1) }
        }
        comments.filter { it.parentCommentId !in activeCommentIds }.forEach { appendComment(it, 0) }
        // Recupera también ramas sin raíz por ciclos; visitedCommentIds evita repetir sus comentarios.
        comments.forEach { appendComment(it, 0) }
        return thread
    }
}
