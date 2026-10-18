package com.example.devicersapp.data.dto.RequestDTO.retrofit

/** Cuerpo para alternar un like: el backend decide si lo crea o lo elimina según su estado actual. */
data class ToggleReviewLikeRequestDto(
    val user_id: Int,
)
