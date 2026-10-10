package com.example.devicersapp.data.repository

import com.example.devicersapp.ui.models.ReviewInfo
import kotlinx.coroutines.CancellationException
import com.example.devicersapp.data.datasource.ReviewBookmarkRemoteDataSource
import com.example.devicersapp.data.dto.toReviewBookmarkInfo
import com.example.devicersapp.ui.models.ReviewBookmarkInfo
import javax.inject.Inject

/** Consulta reseñas guardadas y traduce sus DTOs al modelo del front. */
class ReviewBookmarkRepository @Inject constructor(private val source: ReviewBookmarkRemoteDataSource) {
    suspend fun getReviewBookmarks(): List<ReviewBookmarkInfo> = source.getReviewBookmarks().map { it.toReviewBookmarkInfo() }

    /** Obtiene solo los guardados del usuario, con sus productos, autores y reacciones remotas. */
    suspend fun getSavedReviews(
        userId: Int,
        reviews: ReviewRepository,
        products: ProductRepository,
        users: UsersRepository,
        comments: CommentRepository,
        reviewLikes: ReviewLikeRepository,
        commentLikes: CommentLikeRepository
    ): Result<List<ReviewInfo>> {
        return try {
            val ids = getReviewBookmarks()
                .filter { it.userId == userId }
                .map { it.reviewId }
                .distinct()
            if (ids.isEmpty()) return Result.success(emptyList())
            val records = reviews.getReviews().getOrThrow().associateBy { it.id }
            val saved = ids.map { id ->
                requireNotNull(records[id]) { "No se encontró la reseña guardada" }
            }.filter { it.isActive }
            Result.success(products.getReviewContents(saved, users, comments, reviewLikes, commentLikes))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }
}
