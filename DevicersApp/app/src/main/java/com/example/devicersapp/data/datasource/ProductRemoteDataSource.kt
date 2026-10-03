package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.ProductDto

/** Contrato de acceso remoto de product; los errores se propagan al consumidor. */
interface ProductRemoteDataSource {
    suspend fun getProducts(): List<ProductDto>

    suspend fun getProductById(productId: Int): ProductDto
}
