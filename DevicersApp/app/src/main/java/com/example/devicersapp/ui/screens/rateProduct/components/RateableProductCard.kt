package com.example.devicersapp.ui.screens.rateProduct.components

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent

 // Muestra el producto que será calificado y permite cambiarlo.
// Reutiliza ProductContent
@Composable
fun RateableProductCard(
    product: ProductContent,
    onChangeProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(84.dp)
            .background(
                color = colorResource(R.color.surface_secondary_light),
                shape = RoundedCornerShape(15.dp)
            )
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .size(58.dp)
                .background(
                    color = colorResource(R.color.surface_light),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (product.showImage) {
                Image(
                    painter = painterResource(product.imageResId),
                    contentDescription = product.imageDescription,
                    modifier = Modifier.size(46.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                color = colorResource(R.color.text_primary_light)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = product.brand,
                style = MaterialTheme.typography.bodyMedium,
                color = colorResource(R.color.text_secondary_light)
            )
        }

        Text(
            text = stringResource(R.string.rate_product_change),
            style = MaterialTheme.typography.labelMedium,
            color = colorResource(R.color.text_primary_light),
            modifier = Modifier.clickable {
                onChangeProduct()
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RateableProductCardPreview() {
    RateableProductCard(
        product = ProductContent(
            name = "Auriculares",
            brand = "Marca · Audio",
            imageResId = R.drawable.auriculares_logo,
            imageDescription = "Auriculares",
            showImage = true
        ),
        onChangeProduct = {}
    )
}