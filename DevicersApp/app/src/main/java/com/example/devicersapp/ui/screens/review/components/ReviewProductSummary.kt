package com.example.devicersapp.ui.screens.review.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
 * Muestra el producto reseñado con un bloque de imagen amplio junto a su nombre y su marca.
 *
 * @param product Información visible del producto.
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun ReviewProductSummary(product: ProductContent, modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp)
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(20.dp))
            .background(colors.surface, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (product.showImage) {
            Image(
                painter = painterResource(product.imageResId),
                contentDescription = stringResource(product.imageDescriptionResId),
                modifier = Modifier
                    .width(136.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(16.dp)),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(Modifier.width(18.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(product.nameResId),
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stringResource(product.brandResId),
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )
        }
    }
}

/** Muestra una vista previa del resumen de producto. */
@Composable
@Preview(showBackground = true)
fun ReviewProductSummaryPreview() {
    DevicersAppTheme {
        ReviewProductSummary(
            ProductContent(
                nameResId = R.string.review_product_name,
                brandResId = R.string.review_product_brand,
                imageResId = R.drawable.device_00,
                imageDescriptionResId = R.string.review_product_image
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
