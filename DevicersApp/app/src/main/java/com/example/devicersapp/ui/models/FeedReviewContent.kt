package com.example.devicersapp.ui.models

/**
 * Representa una reseña del feed construida con datos del backend.
 *
 * A diferencia de [ReviewContent], los textos llegan como cadenas de la API en lugar de recursos locales.
 *
 * @param reviewId Identificador de la reseña en el backend.
 * @param productId Identificador del artículo reseñado.
 * @param productName Nombre del artículo.
 * @param productBrand Nombre de la marca del artículo.
 * @param productCategory Nombre de la categoría del artículo.
 * @param productImage Imagen del artículo: nombre de un drawable local o URL remota.
 * @param productAverage Promedio de las calificaciones activas del artículo.
 * @param authorId Identificador del autor de la reseña.
 * @param authorUsername Nombre de usuario del autor, sin el prefijo `@`.
 * @param authorImage Foto del autor: nombre de un drawable local o URL remota.
 * @param rating Calificación entera que el autor otorgó al artículo.
 * @param title Título opcional de la reseña.
 * @param body Cuerpo de la reseña.
 * @param likes Cantidad de likes recibidos.
 * @param comments Cantidad de comentarios activos publicados en la reseña.
 * @param createdAtMillis Momento de publicación en milisegundos desde la época Unix.
 */
data class FeedReviewContent(
    val reviewId: Int,
    val productId: Int,
    val productName: String,
    val productBrand: String,
    val productCategory: String,
    val productImage: String?,
    val productAverage: Float,
    val authorId: Int,
    val authorUsername: String,
    val authorImage: String?,
    val rating: Int,
    val title: String?,
    val body: String,
    val likes: Int,
    val comments: Int,
    val createdAtMillis: Long
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}
