package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import kotlin.math.roundToInt

/** Muestra las estrellas, la proporción y el porcentaje de una calificación en la distribución. */
@Composable
internal fun RatingDistributionRow(
    rating: Int,
    progress: Float,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    val percentage = (progress * 100).roundToInt()

    Row(
        modifier = modifier.fillMaxWidth(),
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

/** Muestra una fila de distribución con una proporción de ejemplo. */
@Preview(showBackground = true)
@Composable
private fun RatingDistributionRowPreview() {
    DevicersAppTheme {
        RatingDistributionRow(rating = 5, progress = 0.6f)
    }
}
