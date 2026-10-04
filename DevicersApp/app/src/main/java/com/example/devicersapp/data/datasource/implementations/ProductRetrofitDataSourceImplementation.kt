package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.datasource.services.ProductRetrofitService
import com.example.devicersapp.data.dto.BrandDto
import com.example.devicersapp.data.dto.CategoryDto
import com.example.devicersapp.data.dto.ProductDto
import javax.inject.Inject

/** Implementa el acceso remoto a artículos delegando cada operación en Retrofit. */
class ProductRetrofitDataSourceImplementation @Inject constructor(
    private val service: ProductRetrofitService
) : ProductRemoteDataSource {

    override suspend fun getProducts(): List<ProductDto> = service.getProducts()

    override suspend fun getProductById(productId: Int): ProductDto =
        service.getProductById(productId)

    override suspend fun getBrands(): List<BrandDto> = service.getBrands()

    override suspend fun getCategories(): List<CategoryDto> = service.getCategories()
}
