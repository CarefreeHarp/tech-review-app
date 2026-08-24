package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.ui.models.ProductSearchContent
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText

/**
 * Muestra un producto sugerido que puede elegirse para comenzar una reseña.
 *
 * La fila descansa sobre el fondo y se separa de las demás con divisores, de modo que la
 * acción de elegir quede alineada al centro de cada producto.
 *
 * @param product Producto mostrado en la fila.
 * @param onRateClick Acción solicitada al elegir el producto.
 * @param modifier Modificador aplicado a la fila.
 */
@Composable
fun ProductReviewItem(
    product: ProductSearchContent,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(product.imageResId),
            contentDescription = stringResource(product.imageDescriptionResId),
            modifier = Modifier
                .size(58.dp)
                .clip(RoundedCornerShape(14.dp)),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(product.nameResId),
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(product.brandResId),
                color = colors.textSecondary,
                style = CardMetadataText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = onRateClick,
            modifier = Modifier.height(40.dp),
            shape = RoundedCornerShape(percent = 50),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primary,
                contentColor = colors.textOnPrimary
            ),
            contentPadding = PaddingValues(horizontal = 22.dp)
        ) {
            Text(
                text = stringResource(R.string.create_review_rate),
                style = SearchControlText,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

/** Muestra una vista previa de un producto seleccionable para reseñar. */
@Composable
@Preview(showBackground = true)
fun ProductReviewItemPreview() {
    DevicersAppTheme {
        ProductReviewItem(
            product = LocalProductProvider.products.first(),
            onRateClick = {},
            modifier = Modifier.padding(horizontal = 20.dp)
        )
    }
}
