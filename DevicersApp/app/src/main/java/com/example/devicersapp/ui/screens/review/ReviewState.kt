package com.example.devicersapp.ui.screens.review

import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.ui.models.ReplyContent

/**
 * Representa el estado visible del detalle de una reseña.
 */
data class ReviewState(
    val product: ProductDto? = null,
    val review: ReviewDto? = null,
    val replies: List<ReplyContent> = emptyList(),
    val replyText: String = "",
    val expandedReplies: Map<Int, Boolean> = emptyMap(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)