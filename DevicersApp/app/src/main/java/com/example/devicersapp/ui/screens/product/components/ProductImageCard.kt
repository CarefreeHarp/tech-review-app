package com.example.devicersapp.ui.screens.product.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.theme.DevicersAppTheme

/**
 * Muestra la imagen principal del producto dentro de una tarjeta de superficie.
 *
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun ProductImageCard(product: ProductContent, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(20.dp))
            .background(
                color = LocalDevicersColors.current.surface,
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(product.imageResId),
            contentDescription = stringResource(product.imageDescriptionResId),
            modifier = Modifier.size(width = 240.dp, height = 196.dp),
            contentScale = ContentScale.Fit
        )
    }
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
