package com.example.devicersapp.domain.usecase

import com.example.devicersapp.data.repository.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import com.example.devicersapp.data.dto.toReplyContent
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.CommentInfo

/** Coordina productos, usuarios, comentarios y likes para presentar el detalle de reseñas. */
class ReviewContentUseCase @Inject constructor(
    private val products: ProductRepository,
    private val users: UsersRepository,
    private val comments: CommentRepository,
    private val reviewLikes: ReviewLikeRepository,
    private val commentLikes: CommentLikeRepository
) {
    /** Reúne los datos remotos necesarios para presentar reseñas, sin sustituir relaciones por muestras. */
    suspend fun getReviewContents(records: List<ReviewInfo>): List<ReviewInfo> = coroutineScope {
        if (records.isEmpty()) return@coroutineScope emptyList()
        val selectedIds = records.map { it.id }.toSet()
        val catalog = async { products.getProducts(includeInactive = true).getOrThrow().associateBy { it.id } }
        val authorRecords = async { users.getUsers().associateBy { it.id } }
        val commentRecords = async { comments.getComments().filter { it.isActive && it.reviewId in selectedIds } }
        val likes = async { reviewLikes.getReviewLikes().groupingBy { it.reviewId }.eachCount() }
        val commentLikeRecords = async { commentLikes.getCommentLikes().groupingBy { it.commentId }.eachCount() }
        val articles = catalog.await()
        val authors = authorRecords.await().toMutableMap()
        val activeComments = commentRecords.await().groupBy { it.reviewId }
        val likeCounts = likes.await()
        val replyLikeCounts = commentLikeRecords.await()
        for (id in (records.map { it.userId } + activeComments.values.flatten().map { it.userId }).distinct()) {
            if (id !in authors) authors[id] = users.getUserById(id)
        }
        records.map { review ->
            val replies = mutableListOf<ReplyContent>()
            val thread = activeComments[review.id].orEmpty().sortedWith(compareBy({ it.createdAt }, { it.id }))
            val children = thread.groupBy { it.parentCommentId }
            val ids = thread.map { it.id }.toSet()
            val visited = mutableSetOf<Int>()
            fun append(comment: CommentInfo, depth: Int) {
                // El orden padre-hijos sirve para plegar respuestas; la guarda evita ciclos inválidos.
                if (!visited.add(comment.id)) return
                replies.add(comment.toReplyContent(authors.getValue(comment.userId), depth, replyLikeCounts[comment.id] ?: 0))
                children[comment.id].orEmpty().forEach { append(it, depth + 1) }
            }
            thread.filter { it.parentCommentId !in ids }.forEach { append(it, 0) }
            thread.forEach { append(it, 0) }
            review.copy(
                article = requireNotNull(articles[review.articleId]) { "No se encontró el producto de la reseña" },
                user = authors.getValue(review.userId),
                likes = likeCounts[review.id] ?: 0,
                comments = replies
            )
        }
    }

}
