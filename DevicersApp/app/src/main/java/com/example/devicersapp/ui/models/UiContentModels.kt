package com.example.devicersapp.ui.models

import androidx.annotation.DrawableRes

/** Representa el contenido variable de una reseña mostrada en una tarjeta. */
data class ReviewContent(
    @param:DrawableRes val avatarResId: Int,
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
    val productName: String,
    @param:DrawableRes val productImageResId: Int,
    val author: String,
    val reviewText: String,
    val likes: Int,
    val timeAgo: String,
    val rating: Int
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
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
    @param:DrawableRes val avatarResId: Int,
    val name: String,
    val handle: String,
    val biography: String,
    val stats: List<ProfileStatContent>
)

/** Representa una respuesta publicada dentro del detalle de una reseña. */
data class ReplyContent(
    @param:DrawableRes val avatarResId: Int,
    val author: String,
    val timeAgo: String,
    val text: String
)

/**
 * Representa el contenido visible de una notificación dentro de la aplicación.
 *
 * @param id Identificador único usado para conservar el estado de seguimiento de la tarjeta.
 * @param avatarResId Recurso de imagen que representa al autor de la notificación.
 * @param author Nombre de usuario mostrado como autor de la actividad.
 * @param action Acción que el autor realizó sobre el contenido del usuario.
 * @param detail Contexto adicional opcional sobre el producto o comentario relacionado.
 * @param time Instante de llegada de la notificación en milisegundos desde época Unix.
 * @param showFollowAction Indica si la tarjeta ofrece la acción visual para seguir al autor.
 */
data class NotificationContent(
    val id: String,
    @param:DrawableRes val avatarResId: Int,
    val author: String,
    val action: String,
    val detail: String,
    val time: Long,
    val showFollowAction: Boolean = false
)
