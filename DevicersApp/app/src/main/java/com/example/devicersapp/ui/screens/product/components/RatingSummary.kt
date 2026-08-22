package com.example.devicersapp.ui.screens.product.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
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
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra el promedio, las estrellas y la distribución de calificaciones de un producto.
 *
 * @param summary Datos locales que se representan en el resumen.
 * @param modifier Modificador aplicado al contenedor del resumen.
 */
@Composable
fun RatingSummary(summary: RatingSummaryContent, modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Calificación general del producto.
        Column {
            Text(
                text = stringResource(summary.averageResId),
                style = MaterialTheme.typography.displaySmall,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            RatingStars(rating = summary.rating)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = stringResource(summary.reviewCountResId),
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        // Distribución de las calificaciones recibidas, de cinco a una estrella.
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            summary.distribution.forEach { row ->
                RatingBarRow(row)
            }
        }
    }
}

/** Muestra una vista previa del resumen de calificaciones del producto. */
@Composable
@Preview(showBackground = true)
fun RatingSummaryPreview() {
    DevicersAppTheme {
        RatingSummary(sampleRatingSummary())
    }
}

/** Contiene datos de ejemplo para las vistas previas del resumen de calificaciones. */
private fun sampleRatingSummary() = RatingSummaryContent(
    averageResId = R.string.product_average_value,
    rating = 5,
    reviewCountResId = R.string.product_review_count,
    distribution = listOf(
        RatingDistribution(5, 0.68f, R.string.product_rating_five_percentage),
        RatingDistribution(4, 0.22f, R.string.product_rating_four_percentage),
        RatingDistribution(3, 0.07f, R.string.product_rating_three_percentage),
        RatingDistribution(2, 0.02f, R.string.product_rating_two_percentage),
        RatingDistribution(1, 0.01f, R.string.product_rating_one_percentage)
    )
)
