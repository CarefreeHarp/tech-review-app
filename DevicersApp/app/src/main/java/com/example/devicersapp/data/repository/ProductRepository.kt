package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.dto.toProductInfo
import com.example.devicersapp.ui.models.ProductInfo
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

class ProductRepository @Inject constructor(
    private val productRemoteDataSource: ProductRemoteDataSource
) {

    suspend fun getProducts(includeInactive: Boolean = false): Result<List<ProductInfo>> {
        return try {
            val products = productRemoteDataSource.getProducts().map { it.toProductInfo() }.filter { includeInactive || it.isActive }
            Result.success(products)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProductById(productId: Int): Result<ProductInfo> {
        return try {
            val product = productRemoteDataSource.getProductById(productId).toProductInfo()
            Result.success(product)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}