package com.example.devicersapp.ui.screens.review.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme

/** Muestra el producto asociado a la reseña con su imagen, nombre y marca. */
@Composable
fun ReviewProductSummary(product: ProductContent, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
            .background(LocalDevicersColors.current.surface, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(66.dp)
                .background(LocalDevicersColors.current.background, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (product.showImage) {
                Image(
                    painter = painterResource(product.imageResId),
                    contentDescription = stringResource(product.imageDescriptionResId),
                    modifier = Modifier.size(54.dp)
                )
            }
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(
                text = stringResource(product.nameResId),
                style = MaterialTheme.typography.titleMedium,
                color = LocalDevicersColors.current.textPrimary
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = stringResource(product.brandResId),
                style = MaterialTheme.typography.bodySmall,
                color = LocalDevicersColors.current.textSecondary
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
