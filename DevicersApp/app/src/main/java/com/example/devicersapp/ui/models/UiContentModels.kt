package com.example.devicersapp.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/** Representa el contenido variable de una reseña mostrada en una tarjeta. */
data class ReviewContent(
    @param:DrawableRes val avatarResId: Int,
    @param:StringRes val authorResId: Int,
    val rating: Int,
    @param:StringRes val textResId: Int,
    val likes: Int,
    @param:StringRes val timeAgoResId: Int? = null
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
    @param:StringRes val nameResId: Int,
    @param:StringRes val brandResId: Int,
    @param:DrawableRes val imageResId: Int,
    @param:StringRes val imageDescriptionResId: Int,
    val showImage: Boolean = true
)

/** Representa una fila de la distribución de calificaciones. */
data class RatingDistribution(
    val rating: Int,
    val progress: Float,
    @param:StringRes val percentageResId: Int
) {
    init {
        require(rating in 1..5) { "La calificación debe estar entre 1 y 5." }
    }
}

/** Representa el resumen de calificaciones de un producto. */
data class RatingSummaryContent(
    @param:StringRes val titleResId: Int,
    @param:StringRes val averageResId: Int,
    val rating: Int,
    @param:StringRes val reviewCountResId: Int,
    val distribution: List<RatingDistribution>
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}

/** Representa una estadística visible dentro de un perfil. */
data class ProfileStatContent(
    @param:StringRes val numberResId: Int,
    @param:StringRes val labelResId: Int
)

/** Representa la información visible en el encabezado de un perfil. */
data class ProfileContent(
    @param:DrawableRes val avatarResId: Int,
    @param:StringRes val nameResId: Int,
    @param:StringRes val handleResId: Int,
    @param:StringRes val biographyResId: Int,
    val stats: List<ProfileStatContent>
)

/** Representa una respuesta publicada dentro del detalle de una reseña. */
data class ReplyContent(
    @param:DrawableRes val avatarResId: Int,
    @param:StringRes val authorResId: Int,
    @param:StringRes val timeAgoResId: Int,
    @param:StringRes val textResId: Int
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
    @param:StringRes val authorResId: Int,
    @param:StringRes val actionResId: Int,
    @param:StringRes val detailResId: Int? = null,
    val time: Long,
    val showFollowAction: Boolean = false
)

/** Representa un grupo temporal de notificaciones dentro de la interfaz. */
data class NotificationGroupContent(
    val id: String,
    @param:StringRes val titleResId: Int,
    val notifications: List<NotificationContent>
)

/** Representa un producto que se puede elegir antes de crear una reseña. */
data class ProductSearchContent(
    val id: String,
    val categoryId: String,
    val searchTerms: List<String>,
    @param:StringRes val nameResId: Int,
    @param:StringRes val brandResId: Int,
    @param:StringRes val categoryResId: Int,
    @param:StringRes val imageDescriptionResId: Int,
    @param:DrawableRes val imageResId: Int,
    val rating: Int
) {
    init {
        require(rating in 1..5) { "La calificación debe estar entre 1 y 5." }
    }
}

/** Representa una categoría que se puede seleccionar para filtrar productos. */
data class ProductCategoryContent(
    val id: String,
    @param:StringRes val labelResId: Int
)
