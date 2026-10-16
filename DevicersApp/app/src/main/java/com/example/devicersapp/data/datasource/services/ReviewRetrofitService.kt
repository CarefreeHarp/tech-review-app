package com.example.devicersapp.data.datasource.services
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import retrofit2.http.*
/** Endpoints existentes de reseñas; los cuerpos y respuestas permanecen en la frontera HTTP. */
interface ReviewRetrofitService {
    @GET("reviews") suspend fun getReviews(): List<ReviewRetrofitDto>
    @GET("reviews/{id}") suspend fun getReviewById(@Path("id") id: Int): ReviewRetrofitDto
    @GET("users/{id}/reviews") suspend fun getReviewsByUser(@Path("id") id: Int): List<ReviewRetrofitDto>
    @GET("articles/{id}/reviews") suspend fun getReviewsByProduct(@Path("id") id: Int): List<ReviewRetrofitDto>
    @POST("reviews") suspend fun create(@Body request: CreateReviewRequestDto): ReviewRetrofitDto
    @PUT("reviews/{id}") suspend fun update(@Path("id") id: Int, @Body request: UpdateReviewRequestDto): ReviewRetrofitDto
}
