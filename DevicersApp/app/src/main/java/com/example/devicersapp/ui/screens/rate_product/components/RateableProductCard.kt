package com.example.devicersapp.ui.screens.rate_product.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.devicersapp.R
import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra el producto que será calificado.
 */
@Composable
fun RateableProductCard(
    product: ProductDto,
    onChangeProduct: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(90.dp)
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = colors.surface,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = product.imageUrl,
            contentDescription = product.name,
            placeholder = painterResource(R.drawable.device_00),
            error = painterResource(R.drawable.device_00),
            modifier = Modifier
                .size(60.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(
            modifier = Modifier.width(16.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = product.model ?: "Sin modelo",
                style = CardMetadataText,
                color = colors.textSecondary
            )
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RateableProductCardPreview() {
//    DevicersAppTheme {
//        RateableProductCard(
//            product = ProductContent(
//                nameResId = R.string.rate_product_name,
//                brandResId = R.string.rate_product_brand,
//                imageResId = R.drawable.device_00,
//                imageDescriptionResId = R.string.rate_product_image_description
//            ),
//            onChangeProduct = {},
//            modifier = Modifier.padding(16.dp)
//        )
//    }
//}
