package com.example.devicersapp.ui.screens.search_product.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.search.FilterSlider

/**
 * Muestra el control para seleccionar la calificación mínima de cero a cinco estrellas.
 *
 * Añade al control compartido la escala numérica que ayuda a situar cada posición, y lo tiñe
 * con el dorado con el que la aplicación representa las calificaciones.
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
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 4.dp)
    ) {
        FilterSlider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..5f,
            steps = 4,
            trackColor = colors.rating
        )

        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // La escala se apoya en el color secundario para no competir con el valor activo.
            val scaleColor = colors.textSecondary
            Text(stringResource(R.string.rating_zero), color = scaleColor, style = SearchControlText)
            Text(stringResource(R.string.rating_one), color = scaleColor, style = SearchControlText)
            Text(stringResource(R.string.rating_two), color = scaleColor, style = SearchControlText)
            Text(stringResource(R.string.rating_three), color = scaleColor, style = SearchControlText)
            Text(stringResource(R.string.rating_four), color = scaleColor, style = SearchControlText)
            Text(stringResource(R.string.rating_five), color = scaleColor, style = SearchControlText)
        }
    }
}

/** Muestra una vista previa del control de calificación. */
@Preview(showBackground = true)
@Composable
fun RatingSliderPreview() {
    DevicersAppTheme {
        RatingSlider(
            value = 4f,
            onValueChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
