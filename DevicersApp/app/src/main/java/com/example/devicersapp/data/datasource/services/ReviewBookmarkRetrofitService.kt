package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.ReviewBookmarkDto
import retrofit2.http.GET

/** Declara únicamente los endpoints de reseñas guardadas. */
interface ReviewBookmarkRetrofitService {
    @GET("review-bookmarks")
    suspend fun getReviewBookmarks(): List<ReviewBookmarkDto>
}
