package com.example.devicersapp.ui.screens.search.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText

/**
 * Muestra el control para seleccionar la calificación mínima de cero a cinco estrellas.
 *
 * El control reemplaza la apariencia predeterminada de Material por una línea delgada
 * y un pulgar circular dorado, tal como aparece en el diseño de Devicers.
 *
 * @param value Calificación mínima seleccionada.
 * @param onValueChange Acción que solicita cambiar la calificación mínima.
 * @param modifier Modificador aplicado al control.
 */
// El pulgar y la pista propios exigen la variante experimental de `Slider` en Material3 1.4.
@OptIn(ExperimentalMaterial3Api::class)
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
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = 0f..5f,
            steps = 4,
            modifier = Modifier.fillMaxWidth().height(24.dp),
            thumb = {
                // El pulgar dorado retoma el color con el que se representan las calificaciones.
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .background(colors.rating, CircleShape)
                        .border(2.dp, colors.surfaceSecondary, CircleShape)
                )
            },
            track = { sliderState ->
                val progress = (sliderState.value / 5f).coerceIn(0f, 1f)
                Box(
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Recorrido pendiente del control.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .background(colors.border, RoundedCornerShape(3.dp))
                    )
                    // Recorrido ya alcanzado por la calificación mínima elegida.
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(colors.selection, RoundedCornerShape(3.dp))
                    )
                }
            }
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
