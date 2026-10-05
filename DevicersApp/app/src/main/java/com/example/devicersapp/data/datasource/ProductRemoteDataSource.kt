package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.ProductDto

/** Contrato remoto exclusivo de la entidad Product; propaga errores de consulta. */
interface ProductRemoteDataSource {
    suspend fun getProducts(): List<ProductDto>
    suspend fun getProductById(productId: Int): ProductDto
}
