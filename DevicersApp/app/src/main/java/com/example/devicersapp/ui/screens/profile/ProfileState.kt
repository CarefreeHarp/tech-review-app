package com.example.devicersapp.ui.screens.profile

import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa los datos que necesita la vista de detalle de un perfil. */
data class ProfileState(
    val profile: ProfileContent? = null,
    val reviews: List<ReviewContent> = emptyList()
)
