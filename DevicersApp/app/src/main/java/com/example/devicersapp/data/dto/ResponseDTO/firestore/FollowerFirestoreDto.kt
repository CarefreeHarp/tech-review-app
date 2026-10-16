package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de seguidores en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class FollowerFirestoreDto(
    val follower_id: Int,
    val created_at: Timestamp?
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toFollowerFirestoreDto(): FollowerFirestoreDto {
    return checkNotNull(data).toFollowerFirestoreDto()
}
internal fun Map<String, Any?>.toFollowerFirestoreDto(): FollowerFirestoreDto = FollowerFirestoreDto(
    follower_id = integer("follower_id"),
    created_at = getTimestamp("created_at")
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun FollowerFirestoreDto.toFollowInfo(followedId: Int): FollowInfo = FollowInfo(
    followerId = follower_id,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    followedId = followedId
)
