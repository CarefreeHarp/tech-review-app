package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.dto.ProductDto
import javax.inject.Inject

class ProductRepository @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource
) {

    suspend fun getProducts(): Result<List<ProductDto>> {
        return try {
            val products = productRemoteDataSource.getProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(productId: Int): Result<ProductDto> {
        return try {
            val product = productRemoteDataSource.getProductById(productId)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}