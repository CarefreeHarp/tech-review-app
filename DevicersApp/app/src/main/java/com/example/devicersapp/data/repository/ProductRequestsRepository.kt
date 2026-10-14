package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ProductRequestsRemoteDataSource
import com.example.devicersapp.data.datasource.ProductSuggestion
import com.example.devicersapp.ui.utils.search.isValidLaunchDate
import javax.inject.Inject

class ProductRequestsRepository @Inject constructor(
    private val source: ProductRequestsRemoteDataSource,
    private val session: SessionRepository
) {
    suspend fun submit(id: String, suggestion: ProductSuggestion) {
        val clean = suggestion.copy(productName = suggestion.productName.trim(), category = suggestion.category.trim(),
            brand = suggestion.brand.trim(), releaseDate = suggestion.releaseDate.trim())
        require(clean.productName.isNotEmpty())
        require(clean.releaseDate.isEmpty() || isValidLaunchDate(clean.releaseDate))
        val profile = session.requireCurrentProfile()
        source.submit(id, profile.id, checkNotNull(profile.firebaseUid), clean)
    }
}
