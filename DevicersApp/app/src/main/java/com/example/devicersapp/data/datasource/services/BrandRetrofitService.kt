package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.BrandDto
import retrofit2.http.GET

/** Declara únicamente los endpoints de marcas. */
interface BrandRetrofitService {
    @GET("brands")
    suspend fun getBrands(): List<BrandDto>
}
