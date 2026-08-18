package com.example.devicersapp.ui.screens.product.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.RatingDistribution
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.utils.rating.ratingStarsResource

@Composable
fun RatingSummary(summary: RatingSummaryContent, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = stringResource(summary.titleResId),
            style = MaterialTheme.typography.labelLarge,
            color = LocalDevicersColors.current.textPrimary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // Calificación general
            Column(
                modifier = Modifier.width(168.dp)
            ) {
                Text(
                    text = stringResource(summary.averageResId),
                    style = MaterialTheme.typography.displaySmall,
                    color = LocalDevicersColors.current.textPrimary
                )

                Text(
                    text = stringResource(ratingStarsResource(summary.rating)),
                    style = MaterialTheme.typography.titleLarge,
                    color = LocalDevicersColors.current.primaryYellow
                )

                Text(
                    text = stringResource(summary.reviewCountResId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = LocalDevicersColors.current.textSecondary
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Distribución
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                summary.distribution.forEach { row ->
                    RatingBarRow(row)
                }
            }
        }
    }
}

/** Muestra una vista previa del resumen de calificaciones del producto. */
@Composable
@Preview(showBackground = true)
fun RatingSummaryPreview() {
    RatingSummary(sampleRatingSummary())
}

/** Contiene datos de ejemplo para las vistas previas del resumen de calificaciones. */
private fun sampleRatingSummary() = RatingSummaryContent(
    titleResId = R.string.product_average_rating,
    averageResId = R.string.product_average_value,
    rating = 5,
    reviewCountResId = R.string.product_review_count,
    distribution = listOf(
        RatingDistribution(R.string.rating_five, 0.88f, R.string.product_rating_five_percentage),
        RatingDistribution(R.string.rating_four, 0.52f, R.string.product_rating_four_percentage),
        RatingDistribution(R.string.rating_three, 0.17f, R.string.product_rating_three_percentage),
        RatingDistribution(R.string.rating_two, 0.07f, R.string.product_rating_two_percentage),
        RatingDistribution(R.string.rating_one, 0.05f, R.string.product_rating_one_percentage)
    )
)
