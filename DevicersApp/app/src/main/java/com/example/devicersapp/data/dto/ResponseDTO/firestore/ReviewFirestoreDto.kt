package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de reseñas en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class ReviewFirestoreDto(
    val id: Int,
    val user_id: Int,
    val article_id: Int,
    val rating: Int,
    val title: String?,
    val body: String,
    val likes_count: Int,
    val comments_count: Int,
    val is_active: Boolean,
    val created_at: Timestamp?,
    val updated_at: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toReviewFirestoreDto(): ReviewFirestoreDto {
    check(integer("id") == numericDocumentId())
    return checkNotNull(data).toReviewFirestoreDto()
}
internal fun Map<String, Any?>.toReviewFirestoreDto(): ReviewFirestoreDto = ReviewFirestoreDto(
    id = integer("id"),
    user_id = integer("user_id"),
    article_id = integer("article_id"),
    rating = integer("rating"),
    title = getString("title"),
    body = text("body"),
    likes_count = integer("likes_count"),
    comments_count = integer("comments_count"),
    is_active = flag("is_active"),
    created_at = getTimestamp("created_at"),
    updated_at = getTimestamp("updated_at")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun ReviewFirestoreDto.toReviewInfo(): ReviewInfo = ReviewInfo(
    id = id,
    userId = user_id,
    articleId = article_id,
    rating = rating,
    title = title,
    body = body,
    likes = likes_count,
    commentsCount = comments_count,
    isActive = is_active,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    updatedAt = checkNotNull(updated_at.isoText()) { "Fecha updated_at ausente" }
)
