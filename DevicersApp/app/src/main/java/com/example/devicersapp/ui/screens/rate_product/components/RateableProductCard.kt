package com.example.devicersapp.ui.screens.rate_product.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra el producto que será calificado y expone la acción para cambiarlo.
 *
 * @param product Información visible del producto.
 * @param onChangeProduct Acción solicitada al tocar «Cambiar».
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun RateableProductCard(
    product: ProductContent,
    onChangeProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(
                color = colors.surface,
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .background(
                    color = colors.surfaceSecondary,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (product.showImage) {
                Image(
                    painter = painterResource(product.imageResId),
                    contentDescription = stringResource(product.imageDescriptionResId),
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = stringResource(product.nameResId),
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = stringResource(product.brandResId),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
        }

        Text(
            text = stringResource(R.string.rate_product_change),
            style = MaterialTheme.typography.labelMedium,
            color = colors.textPrimary,
            modifier = Modifier.clickable {
                onChangeProduct()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RateableProductCardPreview() {
    DevicersAppTheme {
        RateableProductCard(
            product = ProductContent(
                nameResId = R.string.rate_product_name,
                brandResId = R.string.rate_product_brand,
                imageResId = R.drawable.device_00,
                imageDescriptionResId = R.string.rate_product_image_description
            ),
            onChangeProduct = {}
        )
    }
}
