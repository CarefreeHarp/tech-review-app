package com.example.devicersapp.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Representa el contenido variable de una reseña mostrada en una tarjeta o en su detalle.
 *
 * @param avatarResId Imagen de perfil del autor de la reseña.
 * @param authorResId Nombre de usuario que publicó la reseña.
 * @param rating Calificación entera que el autor otorgó al producto.
 * @param textResId Cuerpo de la reseña.
 * @param likes Cantidad de reacciones recibidas.
 * @param comments Cantidad de comentarios recibidos.
 * @param timeAgoResId Antigüedad de la publicación, cuando la pantalla la muestra.
 */
data class ReviewContent(
    @param:DrawableRes val avatarResId: Int,
    @param:StringRes val authorResId: Int,
    val rating: Int,
    @param:StringRes val textResId: Int,
    val likes: Int,
    val comments: Int = 0,
    @param:StringRes val timeAgoResId: Int? = null
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}

/**
 * Representa una reseña publicada dentro del feed editorial de la pantalla principal.
 *
 * @param productName Nombre del producto reseñado.
 * @param productImageResId Miniatura grande que encabeza la reseña.
 * @param productMetadata Marca y categoría del producto mostradas bajo el autor.
 * @param author Nombre de usuario que publicó la reseña.
 * @param avatarResId Imagen de perfil del autor de la reseña.
 * @param reviewText Cuerpo de la reseña.
 * @param likes Cantidad de reacciones recibidas.
 * @param comments Cantidad de comentarios recibidos.
 * @param timeAgo Antigüedad de la publicación.
 * @param rating Calificación entera que el autor otorgó al producto.
 * @param productAverage Calificación promedio del producto entre toda la comunidad.
 */
data class FeedReviewContent(
    @StringRes val productName: Int,
    @DrawableRes val productImageResId: Int,
    @StringRes val productMetadata: Int,
    @StringRes val author: Int,
    @DrawableRes val avatarResId: Int,
    @StringRes val reviewText: Int,
    val likes: Int,
    val comments: Int,
    @StringRes val timeAgo: Int,
    val rating: Int,
    @StringRes val productAverage: Int
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
    @param:StringRes val handleResId: Int,
    @param:StringRes val biographyResId: Int,
    val stats: List<ProfileStatContent>
)

/**
 * Representa un producto que el perfil ya calificó y muestra en su cuadrícula.
 *
 * @param nameResId Nombre del producto calificado.
 * @param imageResId Imagen del producto mostrada en la tarjeta.
 * @param imageDescriptionResId Texto accesible que describe la imagen.
 * @param rating Calificación entera que el perfil otorgó al producto.
 */
data class RatedProductContent(
    @param:StringRes val nameResId: Int,
    @param:DrawableRes val imageResId: Int,
    @param:StringRes val imageDescriptionResId: Int,
    val rating: Int
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}

/** Representa una respuesta publicada dentro del detalle de una reseña. */
data class ReplyContent(
    @param:DrawableRes val avatarResId: Int,
    @param:StringRes val authorResId: Int,
    @param:StringRes val timeAgoResId: Int,
    @param:StringRes val textResId: Int
)

/** Distingue el tipo de evento de actividad para elegir su insignia y su color. */
enum class ActivityType { LIKE, COMMENT, FOLLOW }

/**
 * Representa el contenido visible de un evento de actividad dentro de la aplicación.
 *
 * @param id Identificador único usado para conservar el estado de seguimiento de la tarjeta.
 * @param type Tipo de evento, que determina la insignia mostrada sobre el avatar.
 * @param avatarResId Recurso de imagen que representa al autor del evento.
 * @param authorResId Nombre de usuario mostrado como autor de la actividad.
 * @param actionResId Acción que el autor realizó sobre el contenido del usuario.
 * @param detailResId Contexto adicional opcional sobre el producto o comentario relacionado.
 * @param time Instante de llegada del evento en milisegundos desde época Unix.
 * @param showFollowAction Indica si la tarjeta ofrece la acción visual para seguir al autor.
 */
data class ActivityContent(
    val id: String,
    val type: ActivityType,
    @param:DrawableRes val avatarResId: Int,
    @param:StringRes val authorResId: Int,
    @param:StringRes val actionResId: Int,
    @param:StringRes val detailResId: Int? = null,
    val time: Long,
    val showFollowAction: Boolean = false
)

/** Representa un grupo temporal de eventos de actividad dentro de la interfaz. */
data class ActivityGroupContent(
    val id: String,
    @param:StringRes val titleResId: Int,
    val activities: List<ActivityContent>
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
