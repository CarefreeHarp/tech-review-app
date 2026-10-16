package com.example.devicersapp.data.dto.RequestDTO.retrofit
import com.example.devicersapp.ui.models.UserChanges
/** Campos editables; Gson omite null para conservar el valor existente. */
data class UpdateUserRequestDto(val username: String?, val biography: String?, val profile_image_url: String?)
/** Traduce la actualización común al cuerpo HTTP. */
fun UserChanges.toUpdateUserRequestDto() = UpdateUserRequestDto(username, biography, profileImageUrl)
