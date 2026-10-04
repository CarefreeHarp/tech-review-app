package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.mapper.toFeedReviewContent
import com.example.devicersapp.ui.models.FeedReviewContent
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

/** Coordina las fuentes remotas de reseñas y las convierte en modelos de UI. */
class ReviewRepository @Inject constructor(
    private val reviewRemoteDataSource: ReviewRemoteDataSource,
    private val productRemoteDataSource: ProductRemoteDataSource
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
                val products = async { productRemoteDataSource.getProducts() }
                val brands = async { productRemoteDataSource.getBrands() }
                val categories = async { productRemoteDataSource.getCategories() }
                val comments = async { reviewRemoteDataSource.getComments() }
                val likes = async { reviewRemoteDataSource.getReviewLikes() }

                val brandNames = brands.await().associate { it.id to it.name }
                val categoryNames = categories.await().associate { it.id to it.name }
                val likeCounts = likes.await().groupingBy { it.reviewId }.eachCount()
                val commentCounts = comments.await()
                    .filter { it.isActive }
                    .groupingBy { it.reviewId }
                    .eachCount()

                // `articles` ya incluye sus reseñas y el autor de cada una.
                products.await()
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
