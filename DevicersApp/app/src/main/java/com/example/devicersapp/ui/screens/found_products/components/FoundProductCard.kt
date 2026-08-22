package com.example.devicersapp.ui.screens.found_products.components

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalFoundProductsScreenProvider
import com.example.devicersapp.ui.models.ProductSearchContent
import com.example.devicersapp.ui.theme.CardHighlightText
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.rating.RatingStars

/**
 * Muestra un producto encontrado con su imagen, su metadata y su calificación.
 *
 * @param product Producto devuelto por la búsqueda.
 * @param modifier Modificador aplicado a la tarjeta.
 * @param onClick Acción solicitada al abrir el detalle del producto.
 */
@Composable
fun FoundProductCard(
    product: ProductSearchContent,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp))
            .background(colors.surface, RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(62.dp)
                .background(colors.background, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(product.imageResId),
                contentDescription = stringResource(product.imageDescriptionResId),
                modifier = Modifier.size(42.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(product.nameResId),
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(product.brandResId),
                color = colors.textSecondary,
                style = CardMetadataText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                RatingStars(rating = product.rating)
                product.averageResId?.let { averageResId ->
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(averageResId),
                        color = colors.textPrimary,
                        style = CardHighlightText
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(10.dp))

        // La flecha reutiliza el ícono de regreso girado, en vez de duplicar el recurso.
        Icon(
            painter = painterResource(R.drawable.back_icon),
            contentDescription = stringResource(R.string.found_products_open),
            modifier = Modifier
                .size(18.dp)
                .rotate(180f),
            tint = colors.textSecondary
        )
    }
}

/** Muestra una vista previa de un producto encontrado. */
@Composable
@Preview(showBackground = true)
fun FoundProductCardPreview() {
    DevicersAppTheme {
        FoundProductCard(
            product = LocalFoundProductsScreenProvider.results.first(),
            modifier = Modifier.padding(16.dp)
        )
    }
}
