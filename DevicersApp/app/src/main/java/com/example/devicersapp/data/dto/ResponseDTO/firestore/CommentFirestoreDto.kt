package com.example.devicersapp.data.dto.ResponseDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
/** Campos de comentarios en Firestore v9; los IDs de los padres se reciben desde la ruta. */
data class CommentFirestoreDto(
    val id: Int,
    val user_id: Int,
    val user_username: String?,
    val user_profile_image_url: String?,
    val parent_comment_id: Int?,
    val body: String,
    val is_active: Boolean,
    val created_at: Timestamp?,
    val updated_at: Timestamp?,
    val likes_count: Int = 0
)
/** Decodifica el documento sin consultas implícitas a otras colecciones. */
internal fun DocumentSnapshot.toCommentFirestoreDto(): CommentFirestoreDto {
    check(integer("id") == numericDocumentId())
    return checkNotNull(data).toCommentFirestoreDto()
}
internal fun Map<String, Any?>.toCommentFirestoreDto(): CommentFirestoreDto = CommentFirestoreDto(
    id = integer("id"),
    user_id = integer("user_id"),
    user_username = getString("user_username"),
    user_profile_image_url = getString("user_profile_image_url"),
    parent_comment_id = optionalInteger("parent_comment_id"),
    body = text("body"),
    is_active = flag("is_active"),
    created_at = getTimestamp("created_at"),
    updated_at = getTimestamp("updated_at"),
    likes_count = optionalInteger("likes_count") ?: 0
)
/** Traduce el transporte al modelo común conservando campos y referencias. */
fun CommentFirestoreDto.toCommentInfo(reviewId: Int): CommentInfo = CommentInfo(
    id = id,
    userId = user_id,
    userUsername = user_username,
    userProfileImageUrl = user_profile_image_url,
    parentCommentId = parent_comment_id,
    body = body,
    isActive = is_active,
    createdAt = checkNotNull(created_at.isoText()) { "Fecha created_at ausente" },
    updatedAt = checkNotNull(updated_at.isoText()) { "Fecha updated_at ausente" },
    likesCount = likes_count,
    reviewId = reviewId
)
