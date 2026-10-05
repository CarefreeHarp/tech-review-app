package com.example.devicersapp.ui.screens.own_profile

import androidx.annotation.StringRes

import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa el estado visible de la pantalla del perfil propio. */
data class OwnProfileState(
    val userId: String? = null,
    val displayName: String = "",
    val email: String = "",
    val profile: ProfileContent? = null,
    val profileImageUrl: String? = null,
    val reviews: List<ReviewContent> = emptyList(),
    val loading: Boolean = false,
    val deleting: Boolean = false,
    @param:StringRes val errorMessageResId: Int? = null
)
