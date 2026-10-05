package com.example.devicersapp.ui.screens.rate_product

import com.example.devicersapp.ui.utils.loading.CenteredLoading

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
import com.example.devicersapp.ui.utils.review_form.RateableProductCard
import com.example.devicersapp.ui.utils.review_form.RatingSelector
import com.example.devicersapp.ui.utils.review_form.ReviewForm
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Renderiza la calificación y solicita la carga del producto que llega por argumento. */
@Composable
fun RateProductView(
    productId: Int? = null,
    onPublishClick: () -> Unit = {},
    onChooseProduct: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: RateProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    LaunchedEffect(uiState.published) { if (uiState.published) onPublishClick() }
    if (uiState.loading || uiState.saving) { CenteredLoading(modifier); return }
    if (uiState.product == null) {
        androidx.compose.foundation.layout.Column {
            androidx.compose.material3.Text(uiState.error.orEmpty())
            androidx.compose.material3.TextButton(onClick = onChooseProduct) { androidx.compose.material3.Text("Elegir producto") }
            androidx.compose.material3.TextButton(onClick = { viewModel.loadProduct(productId) }) { androidx.compose.material3.Text("Reintentar") }
        }
        return
    }
    RateProductViewContent(
        state = uiState,
        onRatingChange = viewModel::onRatingChange,
        onTitleChange = viewModel::onTitleChange,
        onExperienceChange = viewModel::onExperienceChange,
        onAdvantageChange = viewModel::onAdvantageChange,
        onDisadvantageChange = viewModel::onDisadvantageChange,
        onChangeProduct = viewModel::onChangeProduct,
        onPublishClick = viewModel::publish,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla los componentes presentacionales de la pantalla de calificación.
 *
 * @param state Estado inmutable que describe el producto y el formulario de la reseña.
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
    state: RateProductState,
    onRatingChange: (Int) -> Unit,
    onTitleChange: (String) -> Unit,
    onExperienceChange: (String) -> Unit,
    onAdvantageChange: (String) -> Unit,
    onDisadvantageChange: (String) -> Unit,
    onChangeProduct: () -> Unit,
    onPublishClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.loading || state.saving) { CenteredLoading(modifier); return }
    // La calificación solo se puede mostrar cuando el ViewModel ya resolvió el producto.
    val product = state.product ?: return

    LazyColumn(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {

        item {
            state.error?.let { androidx.compose.material3.Text(it, color = androidx.compose.material3.MaterialTheme.colorScheme.error) }
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
                rating = state.rating,
                onRatingChange = onRatingChange
            )

            Spacer(modifier = Modifier.height(28.dp))
        }

        item {
            ReviewForm(
                title = state.title,
                onTitleChange = onTitleChange,

                experience = state.experience,
                onExperienceChange = onExperienceChange,

                advantage = state.advantage,
                onAdvantageChange = onAdvantageChange,

                disadvantage = state.disadvantage,
                onDisadvantageChange = onDisadvantageChange,

                onPublishClick = onPublishClick,
                enabled = !state.saving
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
                state = RateProductState(product = LocalProductProvider.product),
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
                state = RateProductState(product = LocalProductProvider.product),
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
