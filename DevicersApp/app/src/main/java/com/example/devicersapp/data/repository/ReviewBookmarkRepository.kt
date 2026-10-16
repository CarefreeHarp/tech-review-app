package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Operaciones de reseñas guardadas independientes de la tecnología de persistencia. */
class ReviewBookmarkRepository(private val source: ReviewBookmarkRemoteDataSource) {
    suspend fun getReviewBookmarks(userId: Int): List<ReviewBookmarkInfo> = source.getReviewBookmarks(userId)
}
