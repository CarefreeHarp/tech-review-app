package com.example.devicersapp.ui.models

/** Identidad pública necesaria para mostrar una relación de seguimiento. */
data class FollowListUser(
    val id: Int,
    val username: String,
    val biography: String,
    val imageUrl: String?,
    val isBiographyTruncated: Boolean = false
)

/** Selecciona el sentido de la relación consultada para el mismo perfil. */
enum class FollowListSection { FOLLOWERS, FOLLOWING }
