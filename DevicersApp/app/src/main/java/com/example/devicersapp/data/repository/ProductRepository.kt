package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.dto.*
import javax.inject.Inject

class ProductRepository @Inject constructor(private val source: ProductRemoteDataSource) {
    suspend fun getProducts(): List<ProductDto> = source.getProducts()

    suspend fun getProductById(productId: Int): ProductDto = source.getProductById(productId)
}
