package com.example.devicersapp.data.datasource.services
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import retrofit2.http.*
/** Endpoints existentes de likes de reseñas; los cuerpos y respuestas permanecen en la frontera HTTP. */
interface ReviewLikeRetrofitService {
    @GET("review-likes") suspend fun getReviewLikes(): List<ReviewLikeRetrofitDto>
}
