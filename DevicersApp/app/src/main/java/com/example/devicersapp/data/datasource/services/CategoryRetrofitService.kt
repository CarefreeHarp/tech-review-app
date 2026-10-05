package com.example.devicersapp.data.datasource.services

import com.example.devicersapp.data.dto.CategoryDto
import retrofit2.http.GET

/** Declara únicamente los endpoints de categorías. */
interface CategoryRetrofitService {
    @GET("categories")
    suspend fun getCategories(): List<CategoryDto>
}
