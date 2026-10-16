package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de seguimientos en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class FollowFirestoreDto(
    val followed_id: Int,
    val created_at: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toFollowFirestoreDto(): FollowFirestoreDto {
    return checkNotNull(data).toFollowFirestoreDto()
}
internal fun Map<String, Any?>.toFollowFirestoreDto(): FollowFirestoreDto = FollowFirestoreDto(
    followed_id = integer("followed_id"),
    created_at = getTimestamp("created_at")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun FollowFirestoreDto.toFollowInfo(followerId: Int): FollowInfo = FollowInfo(
    followedId = followed_id,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    followerId = followerId
)
