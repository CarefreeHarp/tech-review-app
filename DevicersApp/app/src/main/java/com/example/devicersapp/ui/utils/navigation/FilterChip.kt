package com.example.devicersapp.ui.utils.navigation

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

/**
 * Muestra una opción visual de filtro.
 *
 * El color de fondo cambia según si la opción está seleccionada.
 * Actualmente el componente es únicamente visual.
 *
 * @param text Recurso de texto mostrado en la opción.
 * @param selected Indica si la opción debe aparecer seleccionada.
 * @param modifier Permite modificar el diseño externo del componente.
 */
@Composable
fun FilterChip(
    @StringRes text: Int,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = if (selected) {
                    colorResource(R.color.primary_yellow)
                } else {
                    colorResource(R.color.surface_light)
                },
                shape = RoundedCornerShape(8.dp)
            )
            .padding(
                horizontal = 10.dp,
                vertical = 7.dp
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(text),
            color = colorResource(R.color.text_primary_light),
            fontSize = 8.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

/**
 * Muestra una vista previa de una opción de filtro seleccionada.
 */
@Composable
@Preview(showBackground = true)
fun FilterChipPreview() {
    FilterChip(
        text = R.string.all,
        selected = true
    )
}