package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ReviewBookmarkRemoteDataSource
import com.example.devicersapp.data.datasource.services.ReviewBookmarkRetrofitService
import com.example.devicersapp.data.dto.ReviewBookmarkDto
import javax.inject.Inject

/** Implementa el acceso a reseñas guardadas mediante su propio servicio Retrofit. */
class ReviewBookmarkRetrofitDataSourceImplementation @Inject constructor(
    private val service: ReviewBookmarkRetrofitService
) : ReviewBookmarkRemoteDataSource {
    override suspend fun getReviewBookmarks(): List<ReviewBookmarkDto> = service.getReviewBookmarks()
}
