package com.example.devicersapp.data.dto.RequestDTO.retrofit
import com.example.devicersapp.ui.models.UserDraft
/** Cuerpo de creación del perfil SQL; el UID debe coincidir con el token autenticado. */
data class CreateUserRequestDto(val firebase_uid: String, val email: String, val username: String, val profile_image_url: String?)
/** Traduce el modelo de registro al contrato HTTP. */
fun UserDraft.toCreateUserRequestDto() = CreateUserRequestDto(firebaseUid, email, username, profileImageUrl)
