package com.example.devicersapp.ui.screens.review.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent

/** Muestra el producto asociado a la reseña con su imagen, nombre y marca. */
@Composable
fun ReviewProductSummary(product: ProductContent, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(LocalDevicersColors.current.surfaceSecondary, RoundedCornerShape(15.dp))
            .border(1.dp, LocalDevicersColors.current.border, RoundedCornerShape(15.dp))
            .padding(horizontal = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(LocalDevicersColors.current.surface, RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            if (product.showImage) {
                Image(
                    painter = painterResource(product.imageResId),
                    contentDescription = product.imageDescription,
                    modifier = Modifier.size(42.dp)
                )
            }
        }
        Spacer(Modifier.width(14.dp))
        Column {
            Text(product.name, style = MaterialTheme.typography.titleMedium, color = LocalDevicersColors.current.textPrimary)
            Spacer(Modifier.height(2.dp))
            Text(product.brand, style = MaterialTheme.typography.bodyMedium, color = LocalDevicersColors.current.textSecondary)
        }
    }
}

/** Muestra una vista previa del resumen de producto. */
@Composable
@Preview(showBackground = true)
fun ReviewProductSummaryPreview() {
    ReviewProductSummary(
        ProductContent(
            name = stringResource(R.string.review_product_name),
            brand = stringResource(R.string.review_product_brand),
            imageResId = R.drawable.auriculares_logo,
            imageDescription = stringResource(R.string.review_product_image),
            showImage = false
        )
    )
}
