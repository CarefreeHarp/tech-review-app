package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa solicitudes de productos con Express y convierte sus respuestas a los modelos comunes. */
class ProductRequestRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: ProductRequestRetrofitService
) : ProductRequestRemoteDataSource {
    override suspend fun getRequests(userId: Int): List<ProductRequestInfo> = service.getRequests(userId).map { it.toProductRequestInfo() }
    override suspend fun submit(requestId: String, userId: Int, suggestion: ProductSuggestion): ProductRequestInfo {
        require(requestId.isNotBlank() && '/' !in requestId && userId > 0 && suggestion.productName.isNotBlank())
        service.submit(suggestion.toCreateProductRequestDto(requestId, userId))
        return service.getById(requestId).toProductRequestInfo()
    }
}
