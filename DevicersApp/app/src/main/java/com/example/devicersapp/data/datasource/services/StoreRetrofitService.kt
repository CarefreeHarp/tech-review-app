package com.example.devicersapp.data.datasource.services
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import retrofit2.http.*
/** Endpoints existentes de tiendas; los cuerpos y respuestas permanecen en la frontera HTTP. */
interface StoreRetrofitService {
    @GET("stores") suspend fun getStores(): List<StoreDto>
}
