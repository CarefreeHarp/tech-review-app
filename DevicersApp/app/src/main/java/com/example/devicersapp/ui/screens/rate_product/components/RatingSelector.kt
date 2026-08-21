package com.example.devicersapp.ui.screens.rate_product.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Permite seleccionar una calificación entre una y cinco estrellas.
 *
 * @param rating Calificación actualmente seleccionada.
 * @param onRatingChange Acción al seleccionar otra calificación.
 * @param modifier Modificador aplicado al selector.
 */
@Composable
fun RatingSelector(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.rate_product_rating_title),
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = stringResource(R.string.rate_product_rating_subtitle).trim(),
            style = MaterialTheme.typography.bodySmall,
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            (1..5).forEach { value ->

                RatingOption(
                    value = value,
                    selected = rating == value,
                    onClick = {
                        onRatingChange(value)
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RatingSelectorPreview() {
    DevicersAppTheme {
        RatingSelector(
            rating = 3,
            onRatingChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
