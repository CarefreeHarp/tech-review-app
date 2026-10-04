package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.datasource.services.ProductRetrofitService
import com.example.devicersapp.data.dto.ProductDto
import javax.inject.Inject

class ProductRetrofitDataSourceImplementation @Inject constructor(
    private val service: ProductRetrofitService
) : ProductRemoteDataSource {

    override suspend fun getProducts(): List<ProductDto> {
        return service.getProducts()
    }

    override suspend fun getProductById(productId: Int): ProductDto {
        return service.getProductById(productId)
    }
}