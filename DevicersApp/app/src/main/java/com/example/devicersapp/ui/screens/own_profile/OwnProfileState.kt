package com.example.devicersapp.ui.screens.own_profile

import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa el estado visible de la pantalla del perfil propio. */
data class OwnProfileState(
    val profile: ProfileContent? = null,
    val reviews: List<ReviewContent> = emptyList()
)