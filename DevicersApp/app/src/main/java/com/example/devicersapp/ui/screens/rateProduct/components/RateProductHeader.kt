package com.example.devicersapp.ui.screens.rateProduct.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Icon
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
 * Muestra el encabezado de la pantalla para calificar un producto.
 *
 * @param onBackClick Acción solicitada al tocar el botón de regreso.
 * @param modifier Modificador aplicado al encabezado.
 */
@Composable
fun RateProductHeader(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
        ) {

            Icon(
                painter = painterResource(R.drawable.back_icon),
                contentDescription = stringResource(R.string.rate_product_back),
                tint = colors.textPrimary,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.CenterStart)
                    .clickable {
                        onBackClick()
                    }
            )

            Text(
                text = stringResource(R.string.rate_product_title),
                style = MaterialTheme.typography.titleLarge,
                color = colors.textPrimary,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        Text(
            text = stringResource(R.string.rate_product_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = colors.textSecondary
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RateProductHeaderPreview() {
    DevicersAppTheme { RateProductHeader(onBackClick = {}) }
}
