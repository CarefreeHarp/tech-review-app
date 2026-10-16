package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Consulta y crea solicitudes con los campos exactos del esquema común. */
class ProductRequestRepository(private val source: ProductRequestRemoteDataSource) {
    suspend fun getRequests(userId: Int): List<ProductRequestInfo> = source.getRequests(userId)
    suspend fun submit(id: String, userId: Int, suggestion: ProductSuggestion): ProductRequestInfo {
        require(suggestion.productName.isNotBlank())
        return source.submit(id, userId, suggestion)
    }
}
