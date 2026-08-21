package com.example.devicersapp.ui.screens.profile.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.RatedProductContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra un producto ya calificado dentro de la cuadrícula del perfil.
 *
 * @param product Producto calificado que se representa en la tarjeta.
 * @param modifier Modificador aplicado a la tarjeta.
 * @param onClick Acción solicitada al abrir el producto.
 */
@Composable
fun ProfileProductCard(
    product: RatedProductContent,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 6.dp, shape = RoundedCornerShape(16.dp))
            .background(colors.surface, RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(104.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(product.imageResId),
                contentDescription = stringResource(product.imageDescriptionResId),
                modifier = Modifier.size(92.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(product.nameResId),
            color = colors.textPrimary,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(4.dp))

        RatingStars(rating = product.rating)
    }
}

/** Muestra una vista previa de una tarjeta de producto calificado. */
@Composable
@Preview(showBackground = true)
fun ProfileProductCardPreview() {
    DevicersAppTheme {
        ProfileProductCard(
            RatedProductContent(
                nameResId = R.string.profile_product_second,
                imageResId = R.drawable.device_03,
                imageDescriptionResId = R.string.profile_product_image,
                rating = 5
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
