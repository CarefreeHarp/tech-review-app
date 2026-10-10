package com.example.devicersapp.data.repository

import java.io.IOException
import retrofit2.HttpException
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.models.CommentInfo
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.data.dto.toFeedReviewContent
import com.example.devicersapp.data.dto.toReplyContent
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.async
import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.dto.toProductInfo
import com.example.devicersapp.ui.models.ProductInfo
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

/** Consulta productos y reúne los datos asociados para presentar reseñas y el feed. */
class ProductRepository @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource
) {

    suspend fun getProducts(includeInactive: Boolean = false): Result<List<ProductInfo>> {
        return try {
            val products = productRemoteDataSource.getProducts().map { it.toProductInfo() }.filter { includeInactive || it.isActive }
            Result.success(products)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(productId: Int): Result<ProductInfo> {
        return try {
            val product = productRemoteDataSource.getProductById(productId).toProductInfo()
            Result.success(product)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Reúne los datos remotos necesarios para presentar reseñas, sin sustituir relaciones por muestras. */
    suspend fun getReviewContents(
        records: List<ReviewInfo>,
        users: UsersRepository,
        comments: CommentRepository,
        reviewLikes: ReviewLikeRepository,
        commentLikes: CommentLikeRepository
    ): List<ReviewInfo> = coroutineScope {
        if (records.isEmpty()) return@coroutineScope emptyList()
        val selectedIds = records.map { it.id }.toSet()
        val catalog = async { getProducts(includeInactive = true).getOrThrow().associateBy { it.id } }
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

    /**
     * Obtiene las reseñas activas del feed, de la más reciente a la más antigua.
     *
     * Devuelve un fallo con la [HttpException] o [IOException] original para que la UI elija el mensaje.
     */
    suspend fun getFeedReviews(
        brands: BrandRepository,
        categories: CategoryRepository,
        comments: CommentRepository,
        reviewLikes: ReviewLikeRepository,
        users: UsersRepository
    ): Result<List<FeedReviewContent>> {
        return try {
            // Las consultas son independientes, así que se lanzan en paralelo.
            val feed = coroutineScope {
                val productsRecords = async { getProducts().getOrThrow() }
                val brandsRecords = async { brands.getBrands() }
                val categoriesRecords = async { categories.getCategories().getOrThrow() }
                val commentsRecords = async { comments.getComments() }
                val likes = async { reviewLikes.getReviewLikes() }

                val brandNames = brandsRecords.await().associate { it.id to it.name }
                val categoryNames = categoriesRecords.await().associate { it.id to it.name }
                val likeCounts = likes.await().groupingBy { it.reviewId }.eachCount()
                val commentCounts = commentsRecords.await()
                    .filter { it.isActive }
                    .groupingBy { it.reviewId }
                    .eachCount()

                // Los artículos incluyen autores, pero la foto vigente se obtiene de Firestore.
                val products = productsRecords.await()
                val images = users.getProfileImages(products.flatMap { product ->
                    product.reviews.orEmpty().filter { it.isActive }.mapNotNull { it.user?.id }
                }.toSet())
                products
                    .filter { it.isActive }
                    .flatMap { product ->
                        val reviews = product.reviews.orEmpty().filter { it.isActive }
                        val average = reviews.map { it.rating }.average().toFloat()

                        reviews.mapNotNull { review ->
                            val author = review.user ?: return@mapNotNull null

                            review.toFeedReviewContent(
                                product = product,
                                author = author.copy(profileImageUrl = images[author.id]),
                                brandName = brandNames[product.brandId].orEmpty(),
                                categoryName = categoryNames[product.categoryId].orEmpty(),
                                productAverage = average,
                                likes = likeCounts[review.id] ?: 0,
                                comments = commentCounts[review.id] ?: 0
                            )
                        }
                    }
                    .sortedByDescending { it.createdAtMillis }
            }

            Result.success(feed)
        } catch (e: HttpException) {
            // El servidor respondió con un código 4xx o 5xx.
            Result.failure(e)
        } catch (e: IOException) {
            // No hubo conexión con el servidor o la respuesta se interrumpió.
            Result.failure(e)
        }
    }
}
