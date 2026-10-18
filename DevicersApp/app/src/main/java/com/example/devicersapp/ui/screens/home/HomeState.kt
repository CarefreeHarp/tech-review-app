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
 * @param allFeedReviews Reseñas consultadas para la pestaña activa, antes de aplicar cambios de presentación.
 * @param pendingLikeReviewIds Reseñas cuyo like se está guardando; ignoran toques repetidos hasta confirmarse.
 */
data class HomeState(
    val feedReviews: List<FeedReviewContent> = emptyList(),
    val isLoading: Boolean = false,
    @param:StringRes val errorMessageResId: Int? = null,
    val isForYouSelected: Boolean = true,
    val allFeedReviews: List<FeedReviewContent> = emptyList(),
    val pendingLikeReviewIds: Set<Int> = emptySet()
)
