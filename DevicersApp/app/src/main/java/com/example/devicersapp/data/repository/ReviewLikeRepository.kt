package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ReviewLikeRemoteDataSource
import com.example.devicersapp.data.dto.toReviewLikeInfo
import com.example.devicersapp.ui.models.ReviewLikeInfo
import javax.inject.Inject

/** Consulta likes de reseñas y traduce sus DTOs al modelo del front. */
class ReviewLikeRepository @Inject constructor(private val source: ReviewLikeRemoteDataSource) {
    suspend fun getReviewLikes(): List<ReviewLikeInfo> = source.getReviewLikes().map { it.toReviewLikeInfo() }
}
