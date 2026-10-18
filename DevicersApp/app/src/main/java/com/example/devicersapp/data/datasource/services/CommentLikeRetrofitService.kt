package com.example.devicersapp.data.datasource.services
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import retrofit2.http.*
/** Endpoints existentes de likes de comentarios; los cuerpos y respuestas permanecen en la frontera HTTP. */
interface CommentLikeRetrofitService {
    @POST("reviews/{reviewId}/comments/{commentId}/likes/toggle") suspend fun toggleCommentLike(
        @Path("reviewId") reviewId: Int, @Path("commentId") commentId: Int,
        @Body request: ToggleReviewLikeRequestDto): CommentLikeStatusRetrofitDto
    @GET("comment-likes") suspend fun getCommentLikes(): List<CommentLikeRetrofitDto>
}
