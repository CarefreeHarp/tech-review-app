package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.theme.DevicersAppTheme

/**
 * Muestra la imagen principal del producto sin contenedor visual adicional.
 *
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun ProductImageCard(product: ProductContent, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(product.imageResId),
        contentDescription = stringResource(product.imageDescriptionResId),
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(20.dp)),
        contentScale = ContentScale.Fit
    )
}

/** Muestra una vista previa de la tarjeta de imagen del producto. */
@Composable
@Preview(showBackground = true)
fun ProductImageCardPreview() {
    DevicersAppTheme {
        ProductImageCard(
            ProductContent(
                nameResId = R.string.product_title,
                brandResId = R.string.product_brand_label,
                imageResId = R.drawable.device_00,
                imageDescriptionResId = R.string.review_product_image
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
