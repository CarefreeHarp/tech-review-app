package com.example.devicersapp.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/** Representa la información básica de un producto para las pantallas de detalle. */
data class ProductContent(
    @param:StringRes val nameResId: Int,
    @param:StringRes val brandResId: Int,
    @param:DrawableRes val imageResId: Int,
    @param:StringRes val imageDescriptionResId: Int,
    val showImage: Boolean = true
)

/**
 * Representa un producto del catálogo devuelto por una búsqueda.
 *
 * @param id Identificador único del producto.
 * @param categoryId Categoría con la que se filtra el producto.
 * @param searchTerms Términos con los que el producto responde a una búsqueda por texto.
 * @param nameResId Nombre del producto.
 * @param brandResId Marca y categoría mostradas como metadata.
 * @param categoryResId Nombre visible de su categoría.
 * @param imageDescriptionResId Texto accesible que describe la imagen.
 * @param imageResId Imagen del producto.
 * @param rating Calificación entera representada por las estrellas.
 * @param averageResId Promedio del producto, cuando la pantalla lo muestra junto a las estrellas.
 */
data class ProductSearchContent(
    val id: String,
    val categoryId: String,
    val searchTerms: List<String>,
    @param:StringRes val nameResId: Int,
    @param:StringRes val brandResId: Int,
    @param:StringRes val categoryResId: Int,
    @param:StringRes val imageDescriptionResId: Int,
    @param:DrawableRes val imageResId: Int,
    val rating: Int,
    @param:StringRes val averageResId: Int? = null
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}

/** Representa una categoría que se puede seleccionar para filtrar productos. */
data class ProductCategoryContent(
    val id: String,
    @param:StringRes val labelResId: Int
)

/** Representa el resumen de calificaciones de un producto. */
data class RatingSummaryContent(
    @param:StringRes val averageResId: Int,
    val rating: Int,
    @param:StringRes val reviewCountResId: Int,
    val distribution: List<RatingDistribution>
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}

/** Representa una fila de la distribución de calificaciones. */
data class RatingDistribution(
    val rating: Int,
    val progress: Float,
    @param:StringRes val percentageResId: Int
) {
    init { require(rating in 1..5) { "La calificación debe estar entre 1 y 5." } }
}
