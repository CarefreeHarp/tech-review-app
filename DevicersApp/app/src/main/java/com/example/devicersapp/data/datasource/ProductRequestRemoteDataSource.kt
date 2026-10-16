package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de solicitudes de productos; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface ProductRequestRemoteDataSource {
    suspend fun getRequests(userId: Int): List<ProductRequestInfo>
    suspend fun submit(requestId: String, userId: Int, suggestion: ProductSuggestion): ProductRequestInfo
}
