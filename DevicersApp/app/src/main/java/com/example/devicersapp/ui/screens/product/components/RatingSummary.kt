package com.example.devicersapp.ui.screens.product.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.RatingDistribution
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.utils.rating.ratingStarsResource

@Composable
fun RatingSummary(summary: RatingSummaryContent, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {

        Text(
            text = summary.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
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
                    text = summary.average,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalDevicersColors.current.textPrimary
                )

                Text(
                    text = stringResource(ratingStarsResource(summary.rating)),
                    fontSize = 24.sp,
                    color = LocalDevicersColors.current.primaryYellow
                )

                Text(
                    text = summary.reviewCount,
                    fontSize = 14.sp,
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

@Composable
private fun RatingBarRow(distribution: RatingDistribution) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = distribution.rating,
            fontSize = 11.sp,
            color = LocalDevicersColors.current.textSecondary,
            modifier = Modifier.width(14.dp)
        )

        Box(
            modifier = Modifier
                .width(145.dp)
                .height(8.dp)
                .background(
                    color = LocalDevicersColors.current.surfaceSecondary,
                    shape = RoundedCornerShape(10.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(distribution.progress)
                    .height(8.dp)
                    .background(
                        color = LocalDevicersColors.current.primaryYellow,
                        shape = RoundedCornerShape(10.dp)
                    )
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = distribution.percentage,
            fontSize = 10.sp,
            color = LocalDevicersColors.current.textSecondary,
            modifier = Modifier.width(28.dp)
        )
    }
}

/** Muestra una vista previa del resumen de calificaciones del producto. */
@Composable
@Preview(showBackground = true)
fun RatingSummaryPreview() {
    RatingSummary(sampleRatingSummary())
}

/** Muestra una vista previa de una fila de distribución de calificaciones. */
@Composable
@Preview(showBackground = true)
fun RatingBarRowPreview() {
    RatingBarRow(RatingDistribution(rating = stringResource(R.string.rating_five), progress = 0.88f, percentage = stringResource(R.string.product_rating_five_percentage)))
}

/** Contiene datos de ejemplo para las vistas previas del resumen de calificaciones. */
@Composable
private fun sampleRatingSummary() = RatingSummaryContent(
    title = stringResource(R.string.product_average_rating),
    average = stringResource(R.string.product_average_value),
    rating = 5,
    reviewCount = stringResource(R.string.product_review_count),
    distribution = listOf(
        RatingDistribution(stringResource(R.string.rating_five), 0.88f, stringResource(R.string.product_rating_five_percentage)),
        RatingDistribution(stringResource(R.string.rating_four), 0.52f, stringResource(R.string.product_rating_four_percentage)),
        RatingDistribution(stringResource(R.string.rating_three), 0.17f, stringResource(R.string.product_rating_three_percentage)),
        RatingDistribution(stringResource(R.string.rating_two), 0.07f, stringResource(R.string.product_rating_two_percentage)),
        RatingDistribution(stringResource(R.string.rating_one), 0.05f, stringResource(R.string.product_rating_one_percentage))
    )
)
