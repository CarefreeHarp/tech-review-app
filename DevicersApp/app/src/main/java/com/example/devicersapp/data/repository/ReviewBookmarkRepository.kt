package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ReviewBookmarkRemoteDataSource
import com.example.devicersapp.data.dto.toReviewBookmarkInfo
import com.example.devicersapp.ui.models.ReviewBookmarkInfo
import javax.inject.Inject

/** Consulta reseñas guardadas y traduce sus DTOs al modelo del front. */
class ReviewBookmarkRepository @Inject constructor(private val source: ReviewBookmarkRemoteDataSource) {
    suspend fun getReviewBookmarks(): List<ReviewBookmarkInfo> = source.getReviewBookmarks().map { it.toReviewBookmarkInfo() }
}
