package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de likes de comentarios en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class CommentLikeFirestoreDto(
    val user_id: Int,
    val created_at: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toCommentLikeFirestoreDto(): CommentLikeFirestoreDto {
    return checkNotNull(data).toCommentLikeFirestoreDto()
}
internal fun Map<String, Any?>.toCommentLikeFirestoreDto(): CommentLikeFirestoreDto = CommentLikeFirestoreDto(
    user_id = integer("user_id"),
    created_at = getTimestamp("created_at")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun CommentLikeFirestoreDto.toCommentLikeInfo(reviewId: Int, commentId: Int): CommentLikeInfo = CommentLikeInfo(
    userId = user_id,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    id = null, reviewId = reviewId, commentId = commentId
)
