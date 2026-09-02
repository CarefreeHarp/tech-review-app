package com.example.devicersapp.ui.screens.home

import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent

/** Representa una reseña del feed junto con la información de su autor. */
data class HomeFeedItem(
    val review: ReviewContent,
    val author: ProfileContent
)

/** Representa el estado visible de la pantalla principal. */
data class HomeState(
    val feedItems: List<HomeFeedItem> = emptyList(),
    val isForYouSelected: Boolean = true
)