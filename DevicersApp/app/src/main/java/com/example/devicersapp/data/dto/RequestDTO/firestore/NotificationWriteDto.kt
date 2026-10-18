package com.example.devicersapp.data.dto.RequestDTO.firestore
import com.google.firebase.firestore.FieldValue
/** Notificación nueva; user_id es quien la recibe y actor_user_id quien la genera. */
data class CreateNotificationFirestoreDto(val user_id: Int, val actor_user_id: Int, val type: String,
    val review_id: Int?, val comment_id: Int?) {
    fun fields(id: Int): Map<String, Any?> = mapOf(
        "id" to id, "user_id" to user_id, "actor_user_id" to actor_user_id, "type" to type,
        "review_id" to review_id, "comment_id" to comment_id, "is_read" to false,
        "created_at" to FieldValue.serverTimestamp()
    )
}
