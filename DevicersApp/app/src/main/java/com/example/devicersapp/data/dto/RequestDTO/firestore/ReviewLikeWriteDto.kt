package com.example.devicersapp.data.dto.RequestDTO.firestore
import com.google.firebase.firestore.FieldValue
/** Like guardado en reviews/{reviewId}/likes/{userId}; el ID del documento identifica al usuario. */
data class CreateReviewLikeFirestoreDto(val user_id: Int) {
    fun fields(): Map<String, Any> = mapOf(
        "user_id" to user_id,
        "created_at" to FieldValue.serverTimestamp()
    )
}
