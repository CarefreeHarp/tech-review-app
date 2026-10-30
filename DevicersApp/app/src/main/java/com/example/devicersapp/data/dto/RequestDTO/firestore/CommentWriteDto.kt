package com.example.devicersapp.data.dto.RequestDTO.firestore
import com.example.devicersapp.ui.models.CommentDraft
import com.google.firebase.firestore.FieldValue
/** Comentario nuevo en reviews/{reviewId}/comments; guarda una copia del nombre y la foto del autor. */
data class CreateCommentFirestoreDto(val user_id: Int, val user_username: String, val user_profile_image_url: String?,
    val parent_comment_id: Int?, val body: String) {
    fun fields(id: Int): Map<String, Any?> = mapOf(
        "id" to id, "user_id" to user_id, "user_username" to user_username,
        "user_profile_image_url" to user_profile_image_url, "parent_comment_id" to parent_comment_id,
        "body" to body, "likes_count" to 0, "is_active" to true,
        "created_at" to FieldValue.serverTimestamp(), "updated_at" to FieldValue.serverTimestamp()
    )
}
/** Completa el borrador con los datos públicos del autor leídos en la misma transacción. */
fun CommentDraft.toCreateCommentFirestoreDto(username: String, profileImageUrl: String?) =
    CreateCommentFirestoreDto(userId, username, profileImageUrl, parentCommentId, body)
