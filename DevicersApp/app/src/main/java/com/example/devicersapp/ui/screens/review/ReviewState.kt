package com.example.devicersapp.ui.screens.review

import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa el estado visible del detalle de una reseña. */
data class ReviewState(
    val product: ProductContent? = null,
    val review: ReviewContent? = null,
    val replies: List<ReplyContent> = emptyList(),
    val replyText: String = "",
    val expandedReplies: Map<Int, Boolean> = emptyMap()
)