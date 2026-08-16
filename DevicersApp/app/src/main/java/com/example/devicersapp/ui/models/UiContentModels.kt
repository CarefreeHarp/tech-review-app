package com.example.devicersapp.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/** Representa el contenido variable de una reseña mostrada en una tarjeta. */
data class ReviewContent(
    val avatarInitial: String,
    val author: String,
    val rating: Int,
    val text: String,
    val likes: Int,
    val timeAgo: String? = null
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}

/** Representa una reseña compacta publicada dentro del feed. */
data class FeedReviewContent(
    @StringRes val productName: Int,
    @DrawableRes val productImageResId: Int,
    @StringRes val author: Int,
    @StringRes val reviewText: Int,
    val likes: Int,
    @StringRes val timeAgo: Int,
    val rating: Int
) {
    init {
        require(rating in 1..5) {
            "La calificación debe estar entre 1 y 5."
        }
    }
}

/** Representa la información básica de un producto para las pantallas de detalle. */
data class ProductContent(
    val name: String,
    val brand: String,
    @param:DrawableRes val imageResId: Int,
    val imageDescription: String,
    val showImage: Boolean = true
)

/** Representa una fila de la distribución de calificaciones. */
data class RatingDistribution(
    val rating: String,
    val progress: Float,
    val percentage: String
)

/** Representa el resumen de calificaciones de un producto. */
data class RatingSummaryContent(
    val title: String,
    val average: String,
    val rating: Int,
    val reviewCount: String,
    val distribution: List<RatingDistribution>
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}

/** Representa una estadística visible dentro de un perfil. */
data class ProfileStatContent(
    val number: String,
    val label: String
)

/** Representa la información visible en el encabezado de un perfil. */
data class ProfileContent(
    val avatarInitial: String,
    val name: String,
    val handle: String,
    val biography: String,
    val stats: List<ProfileStatContent>
)

/** Representa una respuesta publicada dentro del detalle de una reseña. */
data class ReplyContent(
    val avatarInitial: String,
    val author: String,
    val timeAgo: String,
    val text: String
)
