package com.example.devicersapp.ui.screens.rateProduct.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

// Permite seleccionar una calificación entre 1 y 5 estrellas
@Composable
fun RatingSelector(
    rating: Int,
    onRatingChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Text(
            text = stringResource(R.string.rate_product_rating_title),
            style = MaterialTheme.typography.titleMedium,
            color = colorResource(R.color.text_primary_light)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(R.string.rate_product_rating_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = colorResource(R.color.text_secondary_light)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
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

// Muestra una opción individual de calificación
@Composable
private fun RatingOption(
    value: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .height(64.dp)
            .background(
                color = if (selected) {
                    colorResource(R.color.rating_selected_light)
                } else {
                    colorResource(R.color.surface_secondary_light)
                },
                shape = RoundedCornerShape(12.dp)
            )
            .clickable {
                onClick()
            }
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Image(
            painter = painterResource(R.drawable.rating_star),
            contentDescription = null,
            modifier = Modifier.size(26.dp)
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = value.toString(),
            style = MaterialTheme.typography.labelSmall,
            color = colorResource(R.color.text_primary_light)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RatingSelectorPreview() {
    RatingSelector(
        rating = 5,
        onRatingChange = {}
    )
}