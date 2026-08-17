package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Selector interactivo de calificación entre 1 y 5 estrellas.
 *
 * El estado es administrado por el componente padre.
 */
@Composable
fun RatingSelector(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        for (star in 1..5) {

            Text(
                text = if (star <= rating) "★" else "☆",
                fontSize = 36.sp,
                color = if (star <= rating) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier
                    .size(44.dp)
                    .clickable {
                        onRatingChange(star)
                    }
            )
        }
    }
}