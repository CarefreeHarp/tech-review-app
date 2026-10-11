package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.UserInfo

/** Datos públicos vigentes de un perfil: los únicos que muestran las reseñas y sus comentarios. */
data class UserSummaryDto(
    val id: Int,
    val username: String,
    val profileImageUrl: String?
)

/** Construye el autor con los datos visibles; el resto del perfil no se consulta para estas pantallas. */
fun UserSummaryDto.toUserInfo(): UserInfo = UserInfo(
    id = id,
    email = "",
    username = username,
    firebaseUid = null,
    biography = null,
    profileImageUrl = profileImageUrl,
    notificationsLastViewedAt = null,
    isActive = true,
    createdAt = "",
    updatedAt = ""
)
