package com.example.devicersapp.ui.screens.edit_profile

import android.net.Uri
import androidx.annotation.StringRes

/** Contiene todos los valores visibles y el borrador de edición del perfil actual. */
data class EditProfileState(
    val username: String = "",
    val biography: String = "",
    val email: String = "",
    val profileImageUrl: String? = null,
    val selectedPhoto: Uri? = null,
    val loading: Boolean = true,
    val loaded: Boolean = false,
    @param:StringRes val errorMessageResId: Int? = null
)
