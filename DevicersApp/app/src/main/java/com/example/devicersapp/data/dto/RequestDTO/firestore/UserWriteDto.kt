package com.example.devicersapp.data.dto.RequestDTO.firestore
import com.example.devicersapp.ui.models.*
import com.google.firebase.firestore.FieldValue
/** Campos de registro documental; la transacción asigna el ID y las fechas del servidor. */
data class CreateUserFirestoreDto(val firebase_uid: String, val email: String,
    val username: String, val profile_image_url: String?) {
    fun fields(id: Int): Map<String, Any?> = mapOf(
        "id" to id, "firebase_uid" to firebase_uid, "email" to email,
        "username" to username, "profile_image_url" to profile_image_url,
        "followers_count" to 0, "follows_count" to 0, "review_bookmarks_count" to 0,
        "biography" to "", "notifications_last_viewed_at" to null, "is_active" to true,
        "created_at" to FieldValue.serverTimestamp(), "updated_at" to FieldValue.serverTimestamp()
    )
}
/** Traduce el borrador común al formato persistido. */
fun UserDraft.toCreateUserFirestoreDto() = CreateUserFirestoreDto(firebaseUid, email, username, profileImageUrl)
/** Actualización parcial: las claves ausentes conservan su valor almacenado. */
data class UpdateUserFirestoreDto(val username: String?, val biography: String?, val profile_image_url: String?) {
    fun fields(): Map<String, Any> = buildMap {
        username?.let { put("username", it) }
        biography?.let { put("biography", it) }
        profile_image_url?.let { put("profile_image_url", it) }
        put("updated_at", FieldValue.serverTimestamp())
    }
}
/** Traduce los cambios sin enviar como null los campos omitidos. */
fun UserChanges.toUpdateUserFirestoreDto() = UpdateUserFirestoreDto(username, biography, profileImageUrl)
