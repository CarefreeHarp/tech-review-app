package com.example.devicersapp.data.datasource.services
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import retrofit2.http.*
/** Endpoints existentes de seguimientos; los cuerpos y respuestas permanecen en la frontera HTTP. */
interface FollowRetrofitService {
    @GET("follows") suspend fun getFollows(@Query("followerId") followerId: Int? = null,
        @Query("followedId") followedId: Int? = null): List<FollowRetrofitDto>
    @PUT("users/{id}/follow") suspend fun setFollowing(@Path("id") id: Int,
        @Body request: SetFollowingRequestDto): FollowStatusRetrofitDto
}
