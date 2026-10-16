package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de notificaciones en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class NotificationFirestoreDto(
    val id: Int,
    val user_id: Int,
    val actor_user_id: Int,
    val type: String,
    val review_id: Int?,
    val comment_id: Int?,
    val is_read: Boolean,
    val created_at: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toNotificationFirestoreDto(): NotificationFirestoreDto {
    check(integer("id") == numericDocumentId())
    return checkNotNull(data).toNotificationFirestoreDto()
}
internal fun Map<String, Any?>.toNotificationFirestoreDto(): NotificationFirestoreDto = NotificationFirestoreDto(
    id = integer("id"),
    user_id = integer("user_id"),
    actor_user_id = integer("actor_user_id"),
    type = text("type"),
    review_id = optionalInteger("review_id"),
    comment_id = optionalInteger("comment_id"),
    is_read = flag("is_read"),
    created_at = getTimestamp("created_at")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun NotificationFirestoreDto.toNotificationInfo(): NotificationInfo = NotificationInfo(
    id = id.toString(),
    userId = user_id,
    actorUserId = actor_user_id,
    type = type,
    reviewId = review_id,
    commentId = comment_id,
    isRead = is_read,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" }
)
