package com.example.devicersapp.ui.models

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/** Representa la información visible en el encabezado de un perfil. */
data class ProfileContent(
    val id: String,
    @param:DrawableRes val avatarResId: Int,
    @param:StringRes val handleResId: Int,
    @param:StringRes val biographyResId: Int,
    val stats: List<ProfileStatContent>
)

/** Representa una estadística visible dentro de un perfil. */
data class ProfileStatContent(
    @param:StringRes val numberResId: Int,
    @param:StringRes val labelResId: Int
)

/**
 * Representa una reseña que el perfil guardó para leer más adelante.
 *
 * @param reviewId Identificador de una reseña existente. Sus datos se consultan en el provider de reseñas.
 */
data class SavedReviewContent(
    val reviewId: Int
)

/**
 * Representa un perfil devuelto por una búsqueda de usuarios.
 *
 * @param id Identificador único usado para conservar el estado de seguimiento de la tarjeta.
 * @param avatarResId Imagen de perfil del usuario encontrado.
 * @param handleResId Nombre de usuario del perfil.
 * @param interestsResId Intereses declarados por el perfil.
 * @param reviewCountResId Cantidad de reseñas que ha publicado.
 */
data class ProfileSearchResultContent(
    val id: String,
    @param:DrawableRes val avatarResId: Int,
    @param:StringRes val handleResId: Int,
    @param:StringRes val interestsResId: Int,
    @param:StringRes val reviewCountResId: Int
)
