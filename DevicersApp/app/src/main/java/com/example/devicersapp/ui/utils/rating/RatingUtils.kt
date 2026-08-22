package com.example.devicersapp.ui.utils.rating

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import androidx.compose.ui.text.TextStyle
import com.example.devicersapp.ui.theme.RatingStarsText

/** Genera una secuencia de estrellas según la calificación recibida. */
@Composable
fun ratingStars(rating: Int): String {
    require(rating in 1..5) { "La calificación debe estar entre 1 y 5." }
    return stringResource(R.string.rating_star_character).repeat(rating)
}

/**
 * Muestra las cinco estrellas de una calificación, resaltando únicamente las obtenidas.
 *
 * @param rating Calificación entera representada por las estrellas resaltadas.
 * @param modifier Modificador aplicado a la fila de estrellas.
 * @param style Escala tipográfica de las estrellas, elegida por la pantalla que las muestra.
 */
@Composable
fun RatingStars(
    rating: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = RatingStarsText
) {
    require(rating in 1..5) { "La calificación debe estar entre 1 y 5." }
    val colors = LocalDevicersColors.current
    val star = stringResource(R.string.rating_star_character)

    Row(modifier = modifier) {
        // Las cinco estrellas siempre se dibujan para que la calificación se lea sobre una escala fija.
        repeat(5) { index ->
            Text(
                text = star,
                color = if (index < rating) colors.rating else colors.ratingTrack,
                style = style
            )
        }
    }
}

/** Muestra una vista previa de la secuencia de estrellas generada. */
@Composable
@Preview(showBackground = true)
fun RatingStarsTextPreview() {
    DevicersAppTheme {
        Text(ratingStars(5))
    }
}

/** Muestra una vista previa de la escala completa de cinco estrellas. */
@Composable
@Preview(showBackground = true)
fun RatingStarsPreview() {
    DevicersAppTheme {
        RatingStars(rating = 4)
    }
}
