package com.example.devicersapp.ui.screens.profile

import androidx.annotation.StringRes
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa los datos que necesita la vista de detalle de un perfil. */
data class ProfileState(
    val profile: ProfileContent? = null,
    val reviews: List<ReviewContent> = emptyList(),
    val loading: Boolean = false,
    @param:StringRes val error: Int? = null,
    val isFollowed: Boolean = false,
    val followsYou: Boolean = false,
    val isOwnProfile: Boolean = false,
    val followInProgress: Boolean = false,
    @param:StringRes val followError: Int? = null
)
