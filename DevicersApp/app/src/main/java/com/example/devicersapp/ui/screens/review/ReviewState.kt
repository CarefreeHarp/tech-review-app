package com.example.devicersapp.ui.screens.review

import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewContent

/**
 * Representa el estado visible del detalle de una reseña.
 *
 * @param likeInProgress Indica que el like de la reseña se está guardando; ignora toques repetidos.
 * @param replyInProgress Indica que el comentario escrito se está publicando; bloquea el compositor.
 * @param replyError Mensaje que se muestra cuando no se pudo publicar el comentario.
 */
data class ReviewState(
    val loading: Boolean = false,
    val error: Int? = null,
    val product: ProductContent? = null,
    val review: ReviewContent? = null,
    val isLocal: Boolean = false,
    val canManage: Boolean = false,
    val actionsMenuExpanded: Boolean = false,
    val deletionRequested: Boolean = false,
    val replies: List<ReplyContent> = emptyList(),
    val replyText: String = "",
    val expandedReplies: Map<Int, Boolean> = emptyMap(),
    val likeInProgress: Boolean = false,
    val pendingCommentLikeIds: Set<Int> = emptySet(),
    val commentLikeError: Int? = null,
    val replyInProgress: Boolean = false,
    val replyError: Int? = null
)
