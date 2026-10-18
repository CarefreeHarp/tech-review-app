package com.example.devicersapp.data.dto.RequestDTO.firestore
import com.google.firebase.firestore.FieldValue
/** Relación de like con el ID del aviso para retirarlo junto con el like, incluso ante concurrencia. */
data class CreateCommentLikeFirestoreDto(val user_id: Int, val notification_id: Int?) {
    fun fields(): Map<String, Any?> = mapOf("user_id" to user_id, "notification_id" to notification_id,
        "created_at" to FieldValue.serverTimestamp())
}
