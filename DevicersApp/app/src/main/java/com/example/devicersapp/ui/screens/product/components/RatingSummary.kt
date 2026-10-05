package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.rating.RatingStars
import kotlin.math.roundToInt

/**
 * Muestra el promedio y la distribución de las reseñas recibidas desde el backend.
 */
@Composable
fun RatingSummary(
    reviews: List<ReviewInfo>,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    val averageRating =
        if (reviews.isNotEmpty()) {
            reviews.map { it.rating }.average()
        } else {
            0.0
        }

    val roundedRating =
        if (reviews.isNotEmpty()) {
            averageRating.roundToInt().coerceIn(1, 5)
        } else {
            0
        }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column {

            Text(
                text = String.format("%.1f", averageRating),
                style = MaterialTheme.typography.displaySmall,
                color = colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            if (roundedRating > 0) {
                RatingStars(
                    rating = roundedRating
                )
            }

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = stringResource(R.string.remote_review_count, reviews.size),
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )
        }

        Spacer(
            modifier = Modifier.width(24.dp)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            for (rating in 5 downTo 1) {

                val amount =
                    reviews.count { review ->
                        review.rating == rating
                    }

                val progress =
                    if (reviews.isNotEmpty()) {
                        amount.toFloat() / reviews.size.toFloat()
                    } else {
                        0f
                    }

                RatingDistributionRow(
                    rating = rating,
                    progress = progress
                )
            }
        }
    }
}

@Composable
private fun RatingDistributionRow(
    rating: Int,
    progress: Float
) {
    val colors = LocalDevicersColors.current
    val percentage = (progress * 100).roundToInt()

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = rating.toString(),
            style = MaterialTheme.typography.bodySmall,
            color = colors.textSecondary
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier.weight(1f)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Text(
            text = "$percentage%",
            style = MaterialTheme.typography.bodySmall,
            color = colors.textSecondary
        )
    }
}