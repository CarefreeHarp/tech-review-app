package com.example.devicersapp.ui.screens.product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.devicersapp.data.local.LocalProductScreenProvider
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.product.components.ProductImageCard
import com.example.devicersapp.ui.screens.product.components.RatingSummary
import com.example.devicersapp.ui.screens.product.components.ReviewCard
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Configura el detalle del producto usando los datos locales de ejemplo. */
@Composable
fun ProductScreen(
    productNameResId: Int? = null,
    onRateClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProductScreenContent(
        product = productNameResId?.let {
            LocalProductScreenProvider.getProductByNameResId(it)
        } ?: LocalProductScreenProvider.product,
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
    onRateClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    LazyColumn(modifier = modifier.padding(horizontal = 20.dp)) {
        item {
            Text(
                text = stringResource(product.nameResId),
                color = colors.textPrimary,
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(product.brandResId),
                color = colors.textSecondary,
                style = CardMetadataText
            )

            Spacer(modifier = Modifier.height(18.dp))
            ProductImageCard(product)

            Spacer(modifier = Modifier.height(24.dp))
            RatingSummary(ratingSummary)

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onRateClick(product.nameResId) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.textOnPrimary
                )
            ) {
                Text(
                    text = stringResource(R.string.product_rate),
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.product_top_reviews),
                color = colors.textPrimary,
                style = SearchHeadingText
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        items(reviews) { review ->
            ReviewCard(review)
            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
            // Deja aire para que la barra flotante no tape la última reseña.
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Muestra una vista previa del detalle de producto en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProductScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 6
        ) { innerPadding ->
            ProductScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}

/** Muestra una vista previa del detalle de producto en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProductScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 6
        ) { innerPadding ->
            ProductScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}
