package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.BrandDto
import com.example.devicersapp.data.dto.CategoryDto
import com.example.devicersapp.data.dto.ProductDto
import retrofit2.http.GET
import retrofit2.http.Path

/** Endpoints de product del backend; las respuestas HTTP fallidas lanzan HttpException. */
interface ProductRetrofitService {
    @GET("articles")
    suspend fun getProducts(): List<ProductDto>

    @GET("articles/{productId}")
    suspend fun getProductById(@Path("productId") productId: Int): ProductDto

    @GET("brands")
    suspend fun getBrands(): List<BrandDto>

    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}
