package com.example.devicersapp.data.datasource

data class ProductSuggestion(val productName: String, val category: String, val brand: String, val releaseDate: String)

interface ProductRequestsRemoteDataSource {
    suspend fun submit(requestId: String, userId: Int, firebaseUid: String, suggestion: ProductSuggestion)
}
