package com.example.devicersapp.domain.usecase

import com.example.devicersapp.data.repository.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import com.example.devicersapp.ui.models.ReviewInfo

/** Combina las relaciones de guardado con las reseñas y su contenido remoto. */
class SavedReviewsUseCase @Inject constructor(
    private val bookmarks: ReviewBookmarkRepository,
    private val reviews: ReviewRepository,
    private val content: ReviewContentUseCase
) {
    /** Obtiene solo los guardados del usuario, con sus productos, autores y reacciones remotas. */
    suspend fun getSavedReviews(userId: Int): Result<List<ReviewInfo>> {
        return try {
            val ids = bookmarks.getReviewBookmarks()
                .filter { it.userId == userId }
                .map { it.reviewId }
                .distinct()
            if (ids.isEmpty()) return Result.success(emptyList())
            val records = reviews.getReviews().getOrThrow().associateBy { it.id }
            val saved = ids.map { id ->
                requireNotNull(records[id]) { "No se encontró la reseña guardada" }
            }.filter { it.isActive }
            Result.success(content.getReviewContents(saved))
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

}
