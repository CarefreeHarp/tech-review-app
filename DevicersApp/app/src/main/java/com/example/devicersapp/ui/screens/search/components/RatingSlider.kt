package com.example.devicersapp.ui.screens.search.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.SearchControlText
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults

/**
 * Muestra el control para seleccionar la calificación mínima de cero a cinco estrellas.
 *
 * @param value Calificación mínima seleccionada.
 * @param onValueChange Acción que solicita cambiar la calificación mínima.
 * @param modifier Modificador aplicado al control.
 */
@Composable
fun RatingSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..5f,
            steps = 4,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(
                thumbColor = colorResource(R.color.primary_yellow),
                activeTrackColor = colorResource(R.color.primary_yellow),
                inactiveTrackColor = colorResource(R.color.border_light)
            )
        )

        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(stringResource(R.string.rating_zero), style = SearchControlText)
            Text(stringResource(R.string.rating_one), style = SearchControlText)
            Text(stringResource(R.string.rating_two), style = SearchControlText)
            Text(stringResource(R.string.rating_three), style = SearchControlText)
            Text(stringResource(R.string.rating_four), style = SearchControlText)
            Text(stringResource(R.string.rating_five), style = SearchControlText)
        }
    }
}

/** Muestra una vista previa del control de calificación. */
@Preview(showBackground = true)
@Composable
fun RatingSliderPreview() {
    RatingSlider(
        value = 4f,
        onValueChange = {},
        modifier = Modifier.padding(16.dp)
    )
}
