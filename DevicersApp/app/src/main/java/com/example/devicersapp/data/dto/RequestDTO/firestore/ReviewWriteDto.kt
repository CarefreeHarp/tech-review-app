package com.example.devicersapp.data.dto.RequestDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.firestore.FieldValue
/** Reseña plana; solo guarda referencias al usuario y al artículo. */
data class CreateReviewFirestoreDto(val user_id: Int, val article_id: Int, val rating: Int,
    val title: String?, val body: String) {
    fun fields(id: Int): Map<String, Any?> = mapOf(
        "id" to id, "user_id" to user_id, "article_id" to article_id,
        "rating" to rating, "title" to title, "body" to body,
        "likes_count" to 0, "comments_count" to 0, "is_active" to true,
        "created_at" to FieldValue.serverTimestamp(), "updated_at" to FieldValue.serverTimestamp()
    )
}
/** Convierte el borrador sin copiar los datos del artículo dentro de la reseña. */
fun ReviewDraft.toCreateReviewFirestoreDto() = CreateReviewFirestoreDto(userId, articleId, rating, title, body)
/** Campos editables de una reseña; null significa conservar el valor existente. */
data class UpdateReviewFirestoreDto(val rating: Int?, val title: String?, val body: String?,
    val is_active: Boolean?, val user_id: Int?, val article_id: Int?) {
    fun fields(): Map<String, Any> = buildMap {
        rating?.let { put("rating", it) }
        title?.let { put("title", it) }
        body?.let { put("body", it) }
        is_active?.let { put("is_active", it) }
        user_id?.let { put("user_id", it) }
        article_id?.let { put("article_id", it) }
        put("updated_at", FieldValue.serverTimestamp())
    }
}
/** Mantiene los mismos campos editables que la implementación HTTP. */
fun ReviewChanges.toUpdateReviewFirestoreDto() = UpdateReviewFirestoreDto(rating, title, body, isActive, userId, articleId)
