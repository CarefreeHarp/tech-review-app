package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de reseñas guardadas en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class ReviewBookmarkFirestoreDto(
    val review_id: Int,
    val created_at: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toReviewBookmarkFirestoreDto(): ReviewBookmarkFirestoreDto {
    return checkNotNull(data).toReviewBookmarkFirestoreDto()
}
internal fun Map<String, Any?>.toReviewBookmarkFirestoreDto(): ReviewBookmarkFirestoreDto = ReviewBookmarkFirestoreDto(
    review_id = integer("review_id"),
    created_at = getTimestamp("created_at")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun ReviewBookmarkFirestoreDto.toReviewBookmarkInfo(userId: Int): ReviewBookmarkInfo = ReviewBookmarkInfo(
    reviewId = review_id,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    userId = userId
)
