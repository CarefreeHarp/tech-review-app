package com.example.devicersapp.ui.screens.rate_product.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra una opción individual de calificación dentro del selector.
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
            .height(64.dp)
            .background(
                color = if (selected) colors.primaryYellow else colors.surfaceSecondary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
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
            text = stringResource(R.string.rate_product_rating_value, value),
            style = MaterialTheme.typography.labelSmall,
            color = if (selected) colors.textOnPrimary else colors.textPrimary
        )
    }
}

/** Muestra una vista previa de una opción de calificación seleccionada. */
@Composable
@Preview(showBackground = true)
fun RatingOptionPreview() {
    DevicersAppTheme { RatingOption(value = 5, selected = true, onClick = {}) }
}
