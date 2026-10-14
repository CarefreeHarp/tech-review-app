package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ArticleReviewsRemoteDataSource
import com.example.devicersapp.data.dto.CommentDocumentDto
import com.example.devicersapp.data.dto.UserSummaryDto
import com.example.devicersapp.data.dto.toEpochMillisOrNull
import com.example.devicersapp.data.dto.toFeedReviewContent
import com.example.devicersapp.data.dto.toProductInfo
import com.example.devicersapp.data.dto.toReplyContent
import com.example.devicersapp.data.dto.toReviewInfo
import com.example.devicersapp.data.dto.toUserInfo
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.models.UserInfo
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/**
 * Reúne los artículos y reseñas que muestran Home, Product y Review y los traduce a modelos de UI.
 *
 * Los nombres y fotos de autores se leen de sus perfiles vigentes; la copia guardada en la reseña solo
 * se usa si el perfil ya no existe. Cada operación devuelve el error original para que la UI elija el mensaje.
 */
class ArticleReviewsRepository @Inject constructor(
    private val source: ArticleReviewsRemoteDataSource
) {

    /** Obtiene las reseñas activas del feed con su artículo, autor y conteos, de la más reciente a la más antigua. */
    suspend fun getFeedReviews(): Result<List<FeedReviewContent>> = load {
        val documents = source.getActiveReviews().filter { it.isActive && it.article.isActive }
        coroutineScope {
            val likes = async { source.getReviewLikeCounts(documents.map { it.id }.toSet()) }
            val authors = async { source.getUsers(documents.map { it.userId }.toSet()) }
            // El feed contiene todas las reseñas activas, así que el promedio de cada artículo sale de ellas.
            val averages = documents.groupBy { it.article.id }
                .mapValues { (_, reviews) -> reviews.map { it.rating }.average().toFloat() }
            val likeCounts = likes.await()
            val authorRecords = authors.await()

            documents.map { review ->
                review.toFeedReviewContent(
                    author = authorRecords.author(review.userId, review.userUsername),
                    productAverage = averages.getValue(review.article.id),
                    likes = likeCounts[review.id] ?: 0,
                    comments = review.comments.count { it.isActive }
                )
            }.sortedByDescending { it.createdAtMillis }
        }
    }

    /** Obtiene los IDs de los usuarios que sigue la cuenta indicada, para la pestaña Siguiendo. */
    suspend fun getFollowedUserIds(followerId: Int): Result<Set<Int>> = load {
        source.getFollowedUserIds(followerId)
    }

    /** Obtiene el artículo y sus reseñas activas, de la más reciente a la más antigua. */
    suspend fun getProductDetail(productId: Int): Result<Pair<ProductInfo, List<ReviewInfo>>> = load {
        coroutineScope {
            val article = async { source.getArticleById(productId) }
            val documents = async { source.getActiveReviewsByArticle(productId) }
            val product = article.await().toProductInfo()
            val reviews = documents.await().filter { it.isActive && it.article.id == productId }
            val authors = source.getUsers(reviews.map { it.userId }.toSet())

            product to reviews
                .sortedByDescending { it.createdAt.toEpochMillisOrNull() ?: 0L }
                .map { it.toReviewInfo(author = authors.author(it.userId, it.userUsername), product = product) }
        }
    }

    /** Obtiene una reseña activa con su artículo, autor, likes y el hilo completo de comentarios activos. */
    suspend fun getReviewDetail(reviewId: Int): Result<ReviewInfo> = load {
        val review = source.getReviewById(reviewId)
        // Una reseña retirada desde el perfil no se vuelve a mostrar, aunque su documento se conserve.
        if (!review.isActive) throw NoSuchElementException("La reseña $reviewId ya no está disponible.")
        val comments = review.comments.filter { it.isActive }

        coroutineScope {
            val likes = async { source.getReviewLikeCounts(setOf(review.id)) }
            val commentLikes = async { source.getCommentLikeCounts(comments.map { it.id }.toSet()) }
            val authors = async { source.getUsers(comments.map { it.userId }.toSet() + review.userId) }
            val authorRecords = authors.await()

            review.toReviewInfo(
                author = authorRecords.author(review.userId, review.userUsername),
                product = review.article.toProductInfo(),
                likes = likes.await()[review.id] ?: 0,
                comments = buildThread(comments, authorRecords, commentLikes.await())
            )
        }
    }

    /**
     * Ordena el hilo poniendo cada comentario antes de sus respuestas, con su nivel de anidación.
     *
     * Las respuestas cuyo comentario padre fue retirado se muestran en la raíz, y la guarda de visitados
     * evita repetir comentarios si los datos contienen un ciclo inválido.
     */
    private fun buildThread(
        comments: List<CommentDocumentDto>,
        authors: Map<Int, UserSummaryDto>,
        likes: Map<Int, Int>
    ): List<ReplyContent> {
        val thread = comments.sortedWith(compareBy({ it.createdAt.toEpochMillisOrNull() ?: 0L }, { it.id }))
        val children = thread.groupBy { it.parentCommentId }
        val ids = thread.map { it.id }.toSet()
        val visited = mutableSetOf<Int>()
        val replies = mutableListOf<ReplyContent>()

        fun append(comment: CommentDocumentDto, depth: Int) {
            if (!visited.add(comment.id)) return
            replies.add(comment.toReplyContent(authors.author(comment.userId, comment.userUsername), depth, likes[comment.id] ?: 0))
            children[comment.id].orEmpty().forEach { append(it, depth + 1) }
        }

        thread.filter { it.parentCommentId !in ids }.forEach { append(it, 0) }
        // Los comentarios que solo forman un ciclo no tienen raíz; se agregan al final sin repetirse.
        thread.forEach { append(it, 0) }
        return replies
    }

    /** Usa el perfil vigente y, si ya no existe, el nombre copiado sin foto. */
    private fun Map<Int, UserSummaryDto>.author(userId: Int, copiedUsername: String): UserInfo =
        (this[userId] ?: UserSummaryDto(userId, copiedUsername, null)).toUserInfo()

    private suspend fun <T> load(block: suspend () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
}
