package com.example.devicersapp.ui.screens.remote_reviews

import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.data.dto.ReviewDto

/** Estado de consulta, edición y eliminación de reseñas remotas. */
data class RemoteReviewsState(
    val reviews: List<ReviewDto> = emptyList(),
    val products: List<ProductDto> = emptyList(),
    val loading: Boolean = false,
    val saving: Boolean = false,
    val error: String? = null,
    val editing: Boolean = false,
    val editingId: Int? = null,
    val articleId: Int? = null,
    val rating: Int = 0,
    val title: String = "",
    val body: String = "",
    val deleteId: Int? = null,
)
