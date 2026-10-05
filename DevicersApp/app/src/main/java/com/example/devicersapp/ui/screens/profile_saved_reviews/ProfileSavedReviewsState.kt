package com.example.devicersapp.ui.screens.profile_saved_reviews

import androidx.annotation.StringRes

import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa el contenido y la pestaña activa de las reseñas guardadas. */
data class ProfileSavedReviewsState(
    val profile: ProfileContent? = null,
    val profileImageUrl: String? = null,
    val loading: Boolean = false,
    @param:StringRes val errorMessageResId: Int? = null,
    val email: String = "",
    val savedReviews: List<ReviewContent> = emptyList(),
    val isReviewsSelected: Boolean = false
)
