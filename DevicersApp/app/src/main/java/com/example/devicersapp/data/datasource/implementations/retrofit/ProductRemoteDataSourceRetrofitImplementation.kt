package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa artículos con Express y convierte sus respuestas a los modelos comunes. */
class ProductRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: ProductRetrofitService
) : ProductRemoteDataSource {
    override suspend fun getProducts(): List<ProductInfo> = service.getProducts().map { it.toProductInfo().copy(reviews = emptyList(), stores = emptyList()) }
    override suspend fun getProductById(productId: Int): ProductInfo = service.getProductById(productId).toProductInfo().copy(reviews = emptyList())
    override suspend fun getStores(productId: Int): List<ArticleStoreInfo> = getProductById(productId).stores
}
