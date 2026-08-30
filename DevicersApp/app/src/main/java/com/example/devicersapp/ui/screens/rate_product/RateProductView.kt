package com.example.devicersapp.ui.screens.rate_product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.ui.screens.rate_product.components.RateableProductCard
import com.example.devicersapp.ui.screens.rate_product.components.RatingSelector
import com.example.devicersapp.ui.screens.rate_product.components.ReviewForm
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Renderiza la calificación y solicita la carga del producto que llega por argumento. */
@Composable
fun RateProductView(
    productNameResId: Int? = null,
    onPublishClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RateProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productNameResId) {
        viewModel.loadProduct(productNameResId)
    }

    RateProductViewContent(
        uiState = uiState,
        onRatingChange = viewModel::onRatingChange,
        onTitleChange = viewModel::onTitleChange,
        onExperienceChange = viewModel::onExperienceChange,
        onAdvantageChange = viewModel::onAdvantageChange,
        onDisadvantageChange = viewModel::onDisadvantageChange,
        onChangeProduct = viewModel::onChangeProduct,
        onPublishClick = onPublishClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla los componentes presentacionales de la pantalla de calificación.
 *
 * @param uiState Estado inmutable que describe el producto y el formulario de la reseña.
 * @param onRatingChange Acción al seleccionar una calificación.
 * @param onTitleChange Acción al cambiar el título.
 * @param onExperienceChange Acción al cambiar la experiencia.
 * @param onAdvantageChange Acción al cambiar la ventaja.
 * @param onDisadvantageChange Acción al cambiar la desventaja.
 * @param onChangeProduct Acción para cambiar de producto.
 * @param onPublishClick Acción para publicar la calificación.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun RateProductViewContent(
    uiState: RateProductState,
    onRatingChange: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onExperienceChange: (String) -> Unit,
    onAdvantageChange: (String) -> Unit,
    onDisadvantageChange: (String) -> Unit,
    onChangeProduct: () -> Unit,
    onPublishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // La calificación solo se puede mostrar cuando el ViewModel ya resolvió el producto.
    val product = uiState.product ?: return

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }

        item {
            RateableProductCard(
                product = product,
                onChangeProduct = onChangeProduct
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            RatingSelector(
                rating = uiState.rating,
                onRatingChange = onRatingChange
            )

            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            ReviewForm(
                title = uiState.title,
                onTitleChange = onTitleChange,

                experience = uiState.experience,
                onExperienceChange = onExperienceChange,

                advantage = uiState.advantage,
                onAdvantageChange = onAdvantageChange,

                disadvantage = uiState.disadvantage,
                onDisadvantageChange = onDisadvantageChange,

                onPublishClick = onPublishClick
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

/** Muestra la calificación de producto en tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun RateProductViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(topBarNumber = 2) { innerPadding ->
            RateProductViewContent(
                uiState = RateProductState(product = LocalProductProvider.product),
                onRatingChange = {},
                onTitleChange = {},
                onExperienceChange = {},
                onAdvantageChange = {},
                onDisadvantageChange = {},
                onChangeProduct = {},
                onPublishClick = {},
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}

/** Muestra la calificación de producto en tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun RateProductViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(topBarNumber = 2) { innerPadding ->
            RateProductViewContent(
                uiState = RateProductState(product = LocalProductProvider.product),
                onRatingChange = {},
                onTitleChange = {},
                onExperienceChange = {},
                onAdvantageChange = {},
                onDisadvantageChange = {},
                onChangeProduct = {},
                onPublishClick = {},
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}
