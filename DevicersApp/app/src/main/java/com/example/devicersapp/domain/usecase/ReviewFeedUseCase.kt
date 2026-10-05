package com.example.devicersapp.domain.usecase

import com.example.devicersapp.data.repository.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import com.example.devicersapp.data.dto.toFeedReviewContent
import com.example.devicersapp.ui.models.FeedReviewContent
import retrofit2.HttpException
import java.io.IOException

/** Combina las entidades del catálogo con reseñas y reacciones para construir el feed. */
class ReviewFeedUseCase @Inject constructor(
    private val products: ProductRepository,
    private val brands: BrandRepository,
    private val categories: CategoryRepository,
    private val comments: CommentRepository,
    private val reviewLikes: ReviewLikeRepository
) {
    /**
     * Obtiene las reseñas activas del feed, de la más reciente a la más antigua.
     *
     * Devuelve un fallo con la [HttpException] o [IOException] original para que la UI elija el mensaje.
     */
    suspend fun getFeedReviews(): Result<List<FeedReviewContent>> {
        return try {
            // Las consultas son independientes, así que se lanzan en paralelo.
            val feed = coroutineScope {
                val productsRecords = async { products.getProducts().getOrThrow() }
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

                // `articles` ya incluye sus reseñas y el autor de cada una.
                productsRecords.await()
                    .filter { it.isActive }
                    .flatMap { product ->
                        val reviews = product.reviews.orEmpty().filter { it.isActive }
                        val average = reviews.map { it.rating }.average().toFloat()

                        reviews.mapNotNull { review ->
                            val author = review.user ?: return@mapNotNull null

                            review.toFeedReviewContent(
                                product = product,
                                author = author,
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
