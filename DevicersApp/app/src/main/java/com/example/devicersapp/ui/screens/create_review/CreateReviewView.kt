package com.example.devicersapp.ui.screens.create_review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.ui.models.ProductSearchContent
import com.example.devicersapp.ui.screens.create_review.components.CategoryChipRow
import com.example.devicersapp.ui.screens.create_review.components.ProductMissingCard
import com.example.devicersapp.ui.screens.create_review.components.ProductReviewItem
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Configura la pantalla para crear una reseña y observa su estado desde el ViewModel. */
@Composable
fun CreateReviewView(
    onProductClick: (ProductSearchContent) -> Unit = {},
    onRequestProductClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: CreateReviewViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    CreateReviewViewContent(
        state = uiState,
        onSearchTextChange = viewModel::onSearchTextChange,
        onCategoryChange = viewModel::onCategoryChange,
        onProductClick = onProductClick,
        onRequestProductClick = onRequestProductClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla las categorías y los productos sugeridos para comenzar una reseña.
 *
 * @param state Estado inmutable con las categorías, productos y filtros visibles.
 * @param onSearchTextChange Acción que solicita actualizar el texto.
 * @param onCategoryChange Acción que solicita actualizar la categoría.
 * @param onProductClick Acción solicitada al elegir un producto.
 * @param onRequestProductClick Acción solicitada cuando el producto no existe.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun CreateReviewViewContent(
    state: CreateReviewState,
    onSearchTextChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onProductClick: (ProductSearchContent) -> Unit,
    onRequestProductClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        SearchBar(
            placeholder = R.string.create_review_search_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = true,
            text = state.searchText,
            onTextChange = onSearchTextChange
        )

        Spacer(modifier = Modifier.height(16.dp))

        CategoryChipRow(
            categories = state.categories,
            selectedCategoryId = state.selectedCategoryId,
            onCategoryChange = onCategoryChange
        )

        Spacer(modifier = Modifier.height(11.dp))

        Text(
            text = stringResource(R.string.create_review_suggested),
            color = colors.textPrimary,
            style = SearchHeadingText
        )

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 3.dp,
            color = colors.border
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (state.filteredProducts.isEmpty()) {
                item {
                    Text(
                        text = stringResource(
                            R.string.create_review_empty_results
                        ).trim(),
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = colors.textSecondary,
                        style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                    )
                }
            } else {
                itemsIndexed(
                    items = state.filteredProducts,
                    key = { _, product -> product.id }
                ) { index, product ->

                    ProductReviewItem(
                        product = product,
                        onRateClick = {
                            onProductClick(product)
                        }
                    )

                    if (index < state.filteredProducts.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.fillMaxWidth(),
                            thickness = 1.dp,
                            color = colors.border
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(22.dp))

                ProductMissingCard(
                    onRequestClick = onRequestProductClick
                )

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

/** Muestra una vista previa de la creación de reseña en el tema claro. */
@Composable
@Preview(
    name = "Crear reseña claro",
    showBackground = true,
    heightDp = 1000
)
fun CreateReviewLightPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "create",
            showBottomBar = true,
            topBarNumber = 3
        ) { innerPadding ->
            CreateReviewViewContent(
                state = CreateReviewState(
                    categories = LocalProductProvider.categories,
                    filteredProducts = LocalProductProvider.products
                ),
                onSearchTextChange = {},
                onCategoryChange = {},
                onProductClick = {},
                onRequestProductClick = {},
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
                    .fillMaxSize()
                    .background(
                        LocalDevicersColors.current.background
                    )
            )
        }
    }
}

/** Muestra una vista previa de la creación de reseña en el tema oscuro. */
@Composable
@Preview(
    name = "Crear reseña oscuro",
    showBackground = true,
    heightDp = 1000
)
fun CreateReviewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "create",
            showBottomBar = true,
            topBarNumber = 3
        ) { innerPadding ->
            CreateReviewViewContent(
                state = CreateReviewState(
                    categories = LocalProductProvider.categories,
                    filteredProducts = LocalProductProvider.products
                ),
                onSearchTextChange = {},
                onCategoryChange = {},
                onProductClick = {},
                onRequestProductClick = {},
                modifier = Modifier
                    .padding(
                        top = innerPadding.calculateTopPadding()
                    )
                    .fillMaxSize()
                    .background(
                        LocalDevicersColors.current.background
                    )
            )
        }
    }
}
