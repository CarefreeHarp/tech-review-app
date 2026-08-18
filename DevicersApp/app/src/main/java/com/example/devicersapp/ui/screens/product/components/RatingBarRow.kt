package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.RatingDistribution
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra una fila de distribución de calificaciones con su progreso y porcentaje.
 *
 * @param distribution Información de calificación y progreso que se representa.
 * @param modifier Modificador aplicado a la fila.
 */
@Composable
fun RatingBarRow(
    distribution: RatingDistribution,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(distribution.ratingResId),
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary,
            modifier = Modifier.width(14.dp)
        )
        Box(
            modifier = Modifier
                .width(145.dp)
                .height(8.dp)
                .background(colors.surfaceSecondary, RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(distribution.progress)
                    .height(8.dp)
                    .background(colors.primaryYellow, RoundedCornerShape(10.dp))
            )
        }
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = stringResource(distribution.percentageResId),
            style = MaterialTheme.typography.labelSmall,
            color = colors.textSecondary,
            modifier = Modifier.width(28.dp)
        )
    }
}

/** Muestra una vista previa de una fila de distribución de calificaciones. */
@Composable
@Preview(showBackground = true)
fun RatingBarRowPreview() {
    DevicersAppTheme {
        RatingBarRow(
            distribution = RatingDistribution(
                ratingResId = R.string.rating_five,
                progress = 0.88f,
                percentageResId = R.string.product_rating_five_percentage
            )
        )
    }
}
