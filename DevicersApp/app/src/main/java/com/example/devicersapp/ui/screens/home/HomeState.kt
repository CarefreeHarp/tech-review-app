package com.example.devicersapp.ui.screens.home

import androidx.annotation.StringRes
import com.example.devicersapp.ui.models.FeedReviewContent

/**
 * Representa el estado visible de la pantalla principal.
 *
 * @param feedReviews Reseñas obtenidas del backend, de la más reciente a la más antigua.
 * @param isLoading Indica si hay una carga del feed en curso.
 * @param errorMessageResId Mensaje del último error de carga, o `null` si la carga fue exitosa.
 * @param isForYouSelected Indica si la pestaña "Para ti" está seleccionada.
 */
data class HomeState(
    val feedReviews: List<FeedReviewContent> = emptyList(),
    val isLoading: Boolean = false,
    @param:StringRes val errorMessageResId: Int? = null,
    val isForYouSelected: Boolean = true
)
