package com.example.devicersapp.ui.screens.rate_product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra una opción individual de calificación dentro del selector.
 *
 * Solo la opción elegida se resalta con el dorado de las calificaciones; el resto descansa
 * sobre el fondo reservado a las estrellas sin seleccionar.
 *
 * @param value Valor numérico de la opción.
 * @param selected Indica si esta opción está seleccionada.
 * @param onClick Acción solicitada al tocar la opción.
 * @param modifier Modificador aplicado a la opción.
 */
@Composable
fun RatingOption(
    value: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier
            .height(68.dp)
            .background(
                color = if (selected) colors.rating else colors.ratingTrack,
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(R.drawable.rating_star),
            contentDescription = stringResource(R.string.rate_product_rating_star),
            // La estrella se tiñe con la paleta en vez de mostrar el naranja del recurso original.
            tint = if (selected) colors.textPrimary else colors.rating,
            modifier = Modifier.size(26.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.rate_product_rating_value, value),
            style = CardMetadataText,
            fontWeight = FontWeight.Bold,
            color = if (selected) colors.textPrimary else colors.textSecondary
        )
    }
}

/** Muestra una vista previa de una opción de calificación seleccionada. */
@Composable
@Preview(showBackground = true)
fun RatingOptionPreview() {
    DevicersAppTheme {
        RatingOption(
            value = 5,
            selected = true,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
