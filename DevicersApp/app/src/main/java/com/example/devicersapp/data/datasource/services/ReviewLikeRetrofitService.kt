package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.ReviewLikeDto
import retrofit2.http.GET

/** Declara únicamente los endpoints de likes de reseñas. */
interface ReviewLikeRetrofitService {
    @GET("review-likes")
    suspend fun getReviewLikes(): List<ReviewLikeDto>
}
