package com.example.devicersapp.ui.models

import androidx.annotation.StringRes

/**
 * Representa el contenido variable de una reseña mostrada en una tarjeta o en su detalle.
 *
 * @param authorId Identificador del perfil que publicó la reseña.
 * @param productNameResId Nombre del producto asociado a la reseña.
 * @param productImageResId Imagen del producto asociado a la reseña.
 * @param productMetadataResId Marca y categoría del producto asociado a la reseña.
 * @param rating Calificación entera que el autor otorgó al producto.
 * @param textResId Cuerpo de la reseña.
 * @param likes Cantidad de reacciones recibidas.
 * @param comments Comentarios publicados directamente en esta reseña.
 * @param timeAgoResId Antigüedad de la publicación, cuando la pantalla la muestra.
 * @param productAverageResId Promedio del producto, cuando la pantalla lo muestra junto a las estrellas.
 * @param id Identificador estable usado por las rutas y los providers de reseñas.
 */
data class ReviewContent(
    val authorId: String,
    @param:StringRes val productNameResId: Int,
    @param:androidx.annotation.DrawableRes val productImageResId: Int,
    @param:StringRes val productMetadataResId: Int,
    val rating: Int,
    @param:StringRes val textResId: Int,
    val likes: Int,
    val comments: List<ReplyContent> = emptyList(),
    @param:StringRes val timeAgoResId: Int? = null,
    @param:StringRes val productAverageResId: Int? = null,
    val id: Int = productNameResId
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}

/**
 * Representa una respuesta publicada dentro del hilo de una reseña.
 *
 * @param authorId Identificador del perfil que publicó la respuesta.
 * @param timeAgoResId Antigüedad de la respuesta.
 * @param textResId Cuerpo de la respuesta.
 * @param likes Cantidad de reacciones recibidas; se llena desde la fuente de datos de respuestas.
 * @param depth Nivel de anidación dentro del hilo; cero para una respuesta a la reseña.
 */
data class ReplyContent(
    val authorId: String,
    @param:StringRes val timeAgoResId: Int,
    @param:StringRes val textResId: Int,
    val likes: Int = 0,
    val depth: Int = 0
) {
    init {
        require(likes >= 0) { "La cantidad de reacciones no puede ser negativa." }
        require(depth >= 0) { "El nivel de anidación no puede ser negativo." }
    }
}
