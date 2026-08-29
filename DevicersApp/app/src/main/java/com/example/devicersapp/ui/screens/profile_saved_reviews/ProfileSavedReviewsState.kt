package com.example.devicersapp.ui.screens.profile_saved_reviews

import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa el contenido y la pestaña activa de las reseñas guardadas. */
data class ProfileSavedReviewsState(
    val profile: ProfileContent,
    val savedReviews: List<ReviewContent> = emptyList(),
    val isReviewsSelected: Boolean = false
)
