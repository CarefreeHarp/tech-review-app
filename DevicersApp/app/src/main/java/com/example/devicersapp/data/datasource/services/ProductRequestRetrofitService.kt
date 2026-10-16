package com.example.devicersapp.data.datasource.services
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import retrofit2.http.*
/** Endpoints existentes de solicitudes de productos; los cuerpos y respuestas permanecen en la frontera HTTP. */
interface ProductRequestRetrofitService {
    @GET("product-requests") suspend fun getRequests(@Query("user_id") userId: Int): List<ProductRequestRetrofitDto>
    @GET("product-requests/{id}") suspend fun getById(@Path("id") id: String): ProductRequestRetrofitDto
    @POST("product-requests") suspend fun submit(@Body request: CreateProductRequestDto): ProductRequestRetrofitDto
}
