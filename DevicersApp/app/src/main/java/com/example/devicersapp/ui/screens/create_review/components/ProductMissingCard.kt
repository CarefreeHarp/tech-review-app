package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText

/**
 * Ofrece solicitar un producto que todavía no existe en el catálogo.
 *
 * @param modifier Modificador aplicado a la tarjeta.
 * @param onRequestClick Acción solicitada al pedir que se agregue el producto.
 */
@Composable
fun ProductMissingCard(
    modifier: Modifier = Modifier,
    onRequestClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(18.dp))
            .background(colors.surfaceSecondary, RoundedCornerShape(18.dp))
            .padding(horizontal = 18.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.create_review_product_missing_title).trim(),
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(R.string.create_review_product_missing_description).trim(),
                color = colors.textSecondary,
                style = CardMetadataText
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Button(
            onClick = onRequestClick,
            modifier = Modifier.height(42.dp),
            shape = RoundedCornerShape(percent = 50),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.textOnPrimary
            ),
            contentPadding = PaddingValues(horizontal = 18.dp)
        ) {
            Text(
                text = stringResource(R.string.create_review_request_product),
                style = SearchControlText,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

/** Muestra una vista previa del bloque para solicitar un producto ausente. */
@Composable
@Preview(showBackground = true)
fun ProductMissingCardPreview() {
    DevicersAppTheme {
        ProductMissingCard(modifier = Modifier.padding(16.dp))
    }
}
