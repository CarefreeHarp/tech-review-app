package com.example.devicersapp.ui.utils.search

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText

/**
 * Muestra la etiqueta que encabeza un filtro de búsqueda.
 *
 * @param textResId Recurso de texto utilizado como etiqueta.
 * @param modifier Modificador aplicado a la etiqueta.
 * @param color Color de la etiqueta; los formularios la destacan más que los paneles de filtros.
 */
@Composable
fun FilterLabel(
    @StringRes textResId: Int,
    modifier: Modifier = Modifier,
    color: Color = LocalDevicersColors.current.textSecondary
) {
    Text(
        text = stringResource(textResId),
        modifier = modifier,
        color = color,
        style = SearchControlText
    )
}

/**
 * Muestra el selector entre la búsqueda de usuarios y la búsqueda de productos.
 *
 * @param isUsersSelected Indica si la pantalla actual corresponde a la búsqueda de usuarios.
 * @param onUsersClick Acción solicitada al seleccionar usuarios.
 * @param onProductsClick Acción solicitada al seleccionar productos.
 * @param modifier Modificador aplicado a la fila de botones.
 */
@Composable
fun SearchEntityToggle(
    isUsersSelected: Boolean,
    onUsersClick: () -> Unit,
    onProductsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = {
                println("BOTON USERS PRESIONADO")
                onUsersClick()
            },
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isUsersSelected) {
                    colors.primary
                } else {
                    colors.surface
                },
                contentColor = if (isUsersSelected) {
                    colors.textOnPrimary
                } else {
                    colors.textPrimary
                }
            )
        ) {
            Text(
                text = stringResource(R.string.search_entity_users),
                style = SearchControlText
            )
        }

        Button(
            onClick = {
                println("BOTON PRODUCTS PRESIONADO")
                onProductsClick()
            },
            modifier = Modifier
                .weight(1f)
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isUsersSelected) {
                    colors.surface
                } else {
                    colors.primary
                },
                contentColor = if (isUsersSelected) {
                    colors.textPrimary
                } else {
                    colors.textOnPrimary
                }
            )
        ) {
            Text(
                text = stringResource(R.string.search_entity_products),
                style = SearchControlText
            )
        }
    }
}

/**
 * Muestra el control deslizante con el que se acota un filtro numérico.
 *
 * Reemplaza la apariencia predeterminada de Material por una línea delgada y un pulgar circular.
 * El color lo decide la pantalla, porque cada filtro mide una magnitud distinta: las estrellas
 * se acompañan del dorado de las calificaciones y el resto del acento de la aplicación.
 *
 * @param value Valor seleccionado actualmente.
 * @param onValueChange Acción que solicita cambiar el valor.
 * @param valueRange Rango que admite el filtro.
 * @param steps Cantidad de posiciones intermedias entre los extremos del rango.
 * @param trackColor Color del recorrido ya alcanzado y del pulgar.
 * @param modifier Modificador aplicado al control.
 */
// El pulgar y la pista propios exigen la variante experimental de `Slider` en Material3 1.4.
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FilterSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int,
    trackColor: Color,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Slider(
        value = value,
        onValueChange = onValueChange,
        valueRange = valueRange,
        steps = steps,
        modifier = modifier.fillMaxWidth().height(24.dp),
        thumb = {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .background(trackColor, CircleShape)
                    .border(2.dp, colors.surfaceSecondary, CircleShape)
            )
        },
        track = { sliderState ->
            val span = valueRange.endInclusive - valueRange.start
            val progress = ((sliderState.value - valueRange.start) / span).coerceIn(0f, 1f)
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
                // Recorrido ya alcanzado por el valor elegido.
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress)
                        .fillMaxHeight()
                        .background(trackColor, RoundedCornerShape(3.dp))
                )
            }
        }
    )
}

/** Muestra una vista previa de una etiqueta de filtro. */
@Composable
@Preview(showBackground = true)
fun FilterLabelPreview() {
    DevicersAppTheme {
        FilterLabel(R.string.brand, modifier = Modifier.padding(16.dp))
    }
}

/** Muestra una vista previa del control deslizante con el acento de la aplicación. */
@Composable
@Preview(showBackground = true)
fun FilterSliderPreview() {
    DevicersAppTheme {
        FilterSlider(
            value = 20f,
            onValueChange = {},
            valueRange = 0f..50f,
            steps = 9,
            trackColor = LocalDevicersColors.current.primary,
            modifier = Modifier.padding(16.dp)
        )
    }
}

/** Muestra una vista previa del selector de tipos de búsqueda. */
@Composable
@Preview(showBackground = true)
fun SearchEntityTogglePreview() {
    DevicersAppTheme {
        SearchEntityToggle(
            isUsersSelected = true,
            onUsersClick = {},
            onProductsClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
