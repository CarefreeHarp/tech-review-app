package com.example.devicersapp.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.product.components.ProductImageCard
import com.example.devicersapp.ui.screens.product.components.RatingSummary
import com.example.devicersapp.ui.screens.product.components.ReviewCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Configura el detalle del producto usando los datos locales de ejemplo. */
@Composable
fun ProductScreen(
    onRateClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProductScreenContent(
        product = LocalProductScreenProvider.product,
        ratingSummary = LocalProductScreenProvider.ratingSummary,
        reviews = LocalProductScreenProvider.reviews,
        onRateClick = onRateClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la información, el resumen de calificaciones y las reseñas del producto.
 *
 * @param product Producto visible en el detalle.
 * @param ratingSummary Resumen de calificaciones del producto.
 * @param reviews Reseñas locales que se muestran debajo del resumen.
 * @param onRateClick Acción solicitada al seleccionar calificar producto.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun ProductScreenContent(
    product: ProductContent,
    ratingSummary: RatingSummaryContent,
    reviews: List<ReviewContent>,
    onRateClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(product.nameResId),
                color = LocalDevicersColors.current.textPrimary,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = stringResource(product.brandResId),
                color = LocalDevicersColors.current.textSecondary,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(14.dp))
            ProductImageCard(product)
            Spacer(modifier = Modifier.height(20.dp))
            RatingSummary(ratingSummary)
            Spacer(modifier = Modifier.height(38.dp))
            Button(
                onClick = onRateClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LocalDevicersColors.current.primaryYellow,
                    contentColor = LocalDevicersColors.current.textOnPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.product_rate),
                    style = MaterialTheme.typography.labelLarge
                )
            }
            Spacer(modifier = Modifier.height(25.dp))
        }
        items(reviews) { review ->
            ReviewCard(review)
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/** Muestra una vista previa de la composición completa del producto. */
@Composable
@Preview(showBackground = true)
fun ProductScreenPreview() {
    DevicersAppTheme {
        DevicersScaffold(selectedItem = "search", showBottomBar = true, topBarNumber = 1) { innerPadding ->
            ProductScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
