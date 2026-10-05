package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.CategoryRemoteDataSource
import com.example.devicersapp.data.datasource.services.CategoryRetrofitService
import com.example.devicersapp.data.dto.CategoryDto
import javax.inject.Inject

/** Implementa el acceso a categorías mediante su propio servicio Retrofit. */
class CategoryRetrofitDataSourceImplementation @Inject constructor(
    private val service: CategoryRetrofitService
) : CategoryRemoteDataSource {
    override suspend fun getCategories(): List<CategoryDto> = service.getCategories()
}
