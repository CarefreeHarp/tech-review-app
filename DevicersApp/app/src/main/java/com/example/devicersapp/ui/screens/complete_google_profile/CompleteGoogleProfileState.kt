package com.example.devicersapp.ui.screens.complete_google_profile

import androidx.annotation.StringRes

data class CompleteGoogleProfileState(
    val username: String = "",
    val saving: Boolean = false,
    @param:StringRes val errorMessageResId: Int? = null
)
