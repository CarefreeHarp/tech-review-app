package com.example.devicersapp.data.datasource.services
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import retrofit2.http.*
/** Endpoints existentes de artículos; los cuerpos y respuestas permanecen en la frontera HTTP. */
interface ProductRetrofitService {
    @GET("articles") suspend fun getProducts(): List<ProductRetrofitDto>
    @GET("articles/{id}") suspend fun getProductById(@Path("id") id: Int): ProductRetrofitDto
}
