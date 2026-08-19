package com.example.devicersapp.ui.utils.rating

import androidx.compose.runtime.Composable
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme

/** Genera una secuencia de estrellas según la calificación recibida. */
@Composable
fun ratingStars(rating: Int): String {
    require(rating in 1..5) { "La calificación debe estar entre 1 y 5." }
    return stringResource(R.string.rating_star_character).repeat(rating)
}

/** Muestra una vista previa de la secuencia de estrellas generada. */
@Composable
@Preview(showBackground = true)
fun RatingStarsPreview() {
    DevicersAppTheme {
        Text(ratingStars(5))
    }
}
