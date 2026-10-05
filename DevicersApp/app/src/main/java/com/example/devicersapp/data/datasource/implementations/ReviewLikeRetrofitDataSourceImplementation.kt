package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ReviewLikeRemoteDataSource
import com.example.devicersapp.data.datasource.services.ReviewLikeRetrofitService
import com.example.devicersapp.data.dto.ReviewLikeDto
import javax.inject.Inject

/** Implementa el acceso a likes de reseñas mediante su propio servicio Retrofit. */
class ReviewLikeRetrofitDataSourceImplementation @Inject constructor(
    private val service: ReviewLikeRetrofitService
) : ReviewLikeRemoteDataSource {
    override suspend fun getReviewLikes(): List<ReviewLikeDto> = service.getReviewLikes()
}
