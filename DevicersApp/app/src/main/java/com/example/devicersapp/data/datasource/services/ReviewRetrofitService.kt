package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.CommentDto
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.data.dto.ReviewLikeDto
import com.example.devicersapp.data.dto.CreateReviewRequestDto
import com.example.devicersapp.data.dto.UpdateReviewRequestDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/** Endpoints de review del backend; las respuestas HTTP fallidas lanzan HttpException. */
interface ReviewRetrofitService {
    @GET("reviews")
    suspend fun getReviews(): List<ReviewDto>

    @GET("reviews/{reviewId}")
    suspend fun getReviewById(@Path("reviewId") reviewId: Int): ReviewDto

    @GET("users/{userId}/reviews")
    suspend fun getReviewsByUser(@Path("userId") userId: Int): List<ReviewDto>

    @GET("articles/{productId}/reviews")
    suspend fun getReviewsByProduct(@Path("productId") productId: Int): List<ReviewDto>

    @POST("reviews")
    suspend fun createReview(@Body request: CreateReviewRequestDto): ReviewDto

    @PUT("reviews/{reviewId}")
    suspend fun updateReview(@Path("reviewId") reviewId: Int, @Body request: UpdateReviewRequestDto): ReviewDto

    @DELETE("reviews/{reviewId}")
    suspend fun deleteReview(@Path("reviewId") reviewId: Int): Unit

    @GET("comments")
    suspend fun getComments(): List<CommentDto>

    @GET("review-likes")
    suspend fun getReviewLikes(): List<ReviewLikeDto>
}
