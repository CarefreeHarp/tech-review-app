package com.example.devicersapp.ui.utils.rating

import androidx.annotation.StringRes
import com.example.devicersapp.R

/** Devuelve el recurso de estrellas que corresponde a una calificación de uno a cinco. */
@StringRes
fun ratingStarsResource(rating: Int): Int = when (rating) {
    5 -> R.string.rating_stars_five
    4 -> R.string.rating_stars_four
    3 -> R.string.rating_stars_three
    2 -> R.string.rating_stars_two
    1 -> R.string.rating_stars_one
    else -> throw IllegalArgumentException("La calificación debe estar entre 1 y 5.")
}
