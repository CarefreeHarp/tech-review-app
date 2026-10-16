package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa reseñas guardadas con Express y convierte sus respuestas a los modelos comunes. */
class ReviewBookmarkRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: ReviewBookmarkRetrofitService
) : ReviewBookmarkRemoteDataSource {
    override suspend fun getReviewBookmarks(userId: Int): List<ReviewBookmarkInfo> = service.getReviewBookmarks()
        .filter { it.user_id == userId }.map { it.toReviewBookmarkInfo() }
}
