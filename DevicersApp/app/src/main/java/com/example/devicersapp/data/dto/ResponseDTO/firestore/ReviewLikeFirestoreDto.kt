package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de likes de reseñas en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class ReviewLikeFirestoreDto(
    val user_id: Int,
    val created_at: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toReviewLikeFirestoreDto(): ReviewLikeFirestoreDto {
    return checkNotNull(data).toReviewLikeFirestoreDto()
}
internal fun Map<String, Any?>.toReviewLikeFirestoreDto(): ReviewLikeFirestoreDto = ReviewLikeFirestoreDto(
    user_id = integer("user_id"),
    created_at = getTimestamp("created_at")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun ReviewLikeFirestoreDto.toReviewLikeInfo(reviewId: Int): ReviewLikeInfo = ReviewLikeInfo(
    userId = user_id,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    id = null, reviewId = reviewId
)
