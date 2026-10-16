package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de usuarios en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class UserFirestoreDto(
    val id: Int,
    val firebase_uid: String?,
    val email: String,
    val username: String,
    val biography: String?,
    val profile_image_url: String?,
    val notifications_last_viewed_at: Timestamp?,
    val is_active: Boolean,
    val created_at: Timestamp?,
    val updated_at: Timestamp?,
    val followers_count: Int = 0,
    val follows_count: Int = 0,
    val review_bookmarks_count: Int = 0
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toUserFirestoreDto(): UserFirestoreDto {
    check(integer("id") == numericDocumentId())
    return checkNotNull(data).toUserFirestoreDto()
}
internal fun Map<String, Any?>.toUserFirestoreDto(): UserFirestoreDto = UserFirestoreDto(
    id = integer("id"),
    firebase_uid = getString("firebase_uid"),
    email = text("email"),
    username = text("username"),
    biography = getString("biography"),
    profile_image_url = getString("profile_image_url"),
    notifications_last_viewed_at = getTimestamp("notifications_last_viewed_at"),
    is_active = flag("is_active"),
    created_at = getTimestamp("created_at"),
    updated_at = getTimestamp("updated_at"),
    followers_count = optionalInteger("followers_count") ?: 0,
    follows_count = optionalInteger("follows_count") ?: 0,
    review_bookmarks_count = optionalInteger("review_bookmarks_count") ?: 0
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun UserFirestoreDto.toUserInfo(): UserInfo = UserInfo(
    id = id,
    firebaseUid = firebase_uid,
    email = email,
    username = username,
    biography = biography,
    profileImageUrl = profile_image_url,
    notificationsLastViewedAt = notifications_last_viewed_at.isoText(),
    isActive = is_active,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    updatedAt = checkNotNull(updated_at.isoText()) { "Fecha updated_at ausente" },
    followersCount = followers_count,
    followsCount = follows_count,
    reviewBookmarksCount = review_bookmarks_count
)

/** Proyecta el mismo documento a una identidad pública sin otro DTO redundante. */
fun UserFirestoreDto.toUserSummary() = UserSummary(id, username, biography, profile_image_url)
