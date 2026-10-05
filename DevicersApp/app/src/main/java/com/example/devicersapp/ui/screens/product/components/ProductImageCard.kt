package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.utils.images.localImageResIdFor

/**
 * Muestra la imagen principal del producto desde un recurso local o una URL enviada por el backend.
 */
@Composable
fun ProductImageCard(
    product: ProductInfo,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = localImageResIdFor(product.imageUrl) ?: product.imageUrl,
        contentDescription = product.name,
        modifier = modifier
            .fillMaxWidth()
            .height(230.dp)
            .clip(RoundedCornerShape(20.dp)),
        contentScale = ContentScale.Fit
    )
}

//hibribda para ambos casos
//@Composable
//fun ProductImageCard(
//    product: ProductInfo,
//    modifier: Modifier = Modifier
//) {
//    AsyncImage(
//        model = product.imageUrl,
//        contentDescription = product.name,
//        placeholder = painterResource(R.drawable.device_00),
//        error = painterResource(R.drawable.device_00),
//        modifier = modifier
//            .fillMaxWidth()
//            .height(230.dp)
//            .clip(RoundedCornerShape(20.dp)),
//        contentScale = ContentScale.Fit
//    )
//}
