package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

import java.time.Instant
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
/** Centraliza reseñas, feed y detalle; las estrategias de transporte quedan dentro de los DataSources. */
class ReviewRepository(
    private val source: ReviewRemoteDataSource,
    private val products: ProductRepository,
    private val users: UserRemoteDataSource,
    private val comments: CommentRepository,
    private val likes: ReviewLikeRemoteDataSource
) {
    suspend fun getReviews(): Result<List<ReviewInfo>> = repositoryResult { source.getReviews() }
    suspend fun getReviewById(reviewId: Int): Result<ReviewInfo> = repositoryResult { source.getReviewById(reviewId) }
    suspend fun getReviewsByUser(userId: Int): Result<List<ReviewInfo>> = repositoryResult { source.getReviewsByUser(userId) }
    suspend fun getReviewsByProduct(productId: Int): Result<List<ReviewInfo>> = repositoryResult { source.getReviewsByProduct(productId) }
    suspend fun createReview(draft: ReviewDraft): Result<ReviewInfo> = repositoryResult { source.createReview(draft) }
    suspend fun updateReview(reviewId: Int, changes: ReviewChanges): Result<ReviewInfo> = repositoryResult { source.updateReview(reviewId, changes) }
    suspend fun deleteReview(reviewId: Int): Result<Unit> = repositoryResult { source.deleteReview(reviewId) }
    /** Reúne artículo y autor para las tarjetas de un perfil con lecturas agrupadas. */
    suspend fun getReviewSummariesByUser(userId: Int): List<ReviewInfo> = coroutineScope {
        val records = source.getReviewsByUser(userId).filter { it.isActive }
        if (records.isEmpty()) return@coroutineScope emptyList()
        val articles = async { products.getProducts(includeInactive = true).getOrThrow().associateBy { it.id } }
        val author = async { users.getById(userId) }
        val catalog = articles.await()
        val profile = author.await()
        records.map { it.copy(article = checkNotNull(catalog[it.articleId]), user = profile) }
            .sortedByDescending { Instant.parse(it.createdAt) }
    }

    /**
     * Resuelve las referencias requeridas por el detalle y conserva los contadores persistidos.
     * Con [currentUserId] también indica si ese usuario ya dio like a la reseña.
     */
    suspend fun getReviewDetail(reviewId: Int, currentUserId: Int? = null): Result<ReviewDetailInfo> = repositoryResult {
        val review = source.getReviewById(reviewId)
        if (!review.isActive) throw NoSuchElementException("La reseña ya no está disponible.")
        coroutineScope {
            val article = async { products.getProductById(review.articleId).getOrThrow() }
            val author = async { users.getById(review.userId) }
            val thread = async { comments.getThread(review.id, currentUserId) }
            val liked = async { currentUserId != null && review.id in likes.getLikedReviewIds(currentUserId, setOf(review.id)) }
            ReviewDetailInfo(review.copy(isLiked = liked.await()), article.await(), author.await(), thread.await())
        }
    }
    /**
     * Obtiene tarjetas del feed con consultas agrupadas y promedios de reseñas activas.
     * Con [currentUserId] también marca las reseñas a las que ese usuario ya dio like.
     * [authorIds] restringe la consulta a cuentas seguidas; vacío devuelve un feed vacío.
     */
    suspend fun getFeedReviews(currentUserId: Int? = null, authorIds: Set<Int>? = null): Result<List<FeedReviewContent>> = repositoryResult {
        val records = if (authorIds == null) source.getReviews() else source.getReviewsByUsers(authorIds)
        buildFeedReviews(records, currentUserId, authorIds != null)
    }

    /** Mantiene las tarjetas de Home al día utilizando la misma proyección que la consulta puntual. */
    fun listenFeedReviews(currentUserId: Int, authorIds: Set<Int>? = null): Flow<List<FeedReviewContent>> =
        source.listenReviews(authorIds).map { buildFeedReviews(it, currentUserId, authorIds != null) }

    /** Resuelve productos, autores y likes para cada versión de las reseñas activas. */
    private suspend fun buildFeedReviews(
        records: List<ReviewInfo>, currentUserId: Int?, filteredByAuthor: Boolean
    ): List<FeedReviewContent> {
        val reviews = records.filter { it.isActive }
        if (reviews.isEmpty()) return emptyList()
        return coroutineScope {
            val articles = async { products.getProducts().getOrThrow().associateBy { it.id } }
            val authors = async { users.getByIds(reviews.map { it.userId }.toSet()) }
            val liked = async {
                currentUserId?.let { userId ->
                    likes.getLikedReviewIds(userId, reviews.map { it.id }.toSet())
                }.orEmpty()
            }
            val catalog = articles.await()
            val profiles = authors.await()
            val likedIds = liked.await()
            // En Siguiendo, el promedio incluye también reseñas de autores que no se siguen.
            val ratingRecords = if (!filteredByAuthor) {
                reviews
            } else {
                reviews.map { it.articleId }.toSet()
                    .flatMap { source.getReviewsByProduct(it) }
                    .filter { it.isActive }
            }
            val averageRatings = ratingRecords.groupBy { it.articleId }
                .mapValues { (_, productReviews) -> productReviews.map { it.rating }.average().toFloat() }
            reviews.mapNotNull { review ->
                val article = catalog[review.articleId] ?: return@mapNotNull null
                val author = profiles[review.userId] ?: return@mapNotNull null
                FeedReviewContent(
                    reviewId = review.id,
                    productId = article.id,
                    productName = article.name,
                    productBrand = article.brandName,
                    productCategory = article.categoryName,
                    productImage = article.imageUrl,
                    productAverage = averageRatings.getValue(article.id),
                    authorId = author.id,
                    authorUsername = author.username,
                    authorImage = author.profileImageUrl,
                    rating = review.rating,
                    title = review.title,
                    body = review.body,
                    likes = checkNotNull(review.likes),
                    comments = review.commentsCount,
                    createdAtMillis = Instant.parse(review.createdAt).toEpochMilli(),
                    isLiked = review.id in likedIds
                )
            }.sortedByDescending { it.createdAtMillis }
        }
    }
}
