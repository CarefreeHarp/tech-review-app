package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalCreateReviewScreenProvider
import com.example.devicersapp.ui.models.ProductSearchContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.rating.ratingStarsResource

/**
 * Muestra un producto local que puede seleccionarse para comenzar una reseña.
 *
 * @param product Producto mostrado en la tarjeta.
 * @param onRateClick Acción solicitada al seleccionar calificar.
 * @param modifier Modificador aplicado a la tarjeta.
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
            .background(colors.surface, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(product.imageResId),
            contentDescription = stringResource(product.imageDescriptionResId),
            modifier = Modifier
                .size(72.dp)
                .background(colors.surfaceSecondary, RoundedCornerShape(12.dp))
                .padding(8.dp),
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(product.nameResId),
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleSmall
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(product.brandResId),
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                text = stringResource(ratingStarsResource(product.rating)),
                color = colors.primaryYellow,
                style = MaterialTheme.typography.bodySmall
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onRateClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primaryYellow,
                contentColor = colors.textOnPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.create_review_rate),
                style = MaterialTheme.typography.labelSmall
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
            product = LocalCreateReviewScreenProvider.products.first(),
            onRateClick = {}
        )
    }
}
