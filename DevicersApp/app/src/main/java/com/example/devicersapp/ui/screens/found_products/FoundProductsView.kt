package com.example.devicersapp.ui.screens.found_products

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductSearchContent
import com.example.devicersapp.ui.screens.found_products.components.FoundProductCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/**
 * Configura los resultados de productos y observa su estado desde el ViewModel.
 *
 * @param onProductClick Acción solicitada al abrir el detalle de un producto.
 * @param modifier Modificador aplicado a la pantalla.
 * @param viewModel ViewModel que conserva el estado de la pantalla.
 */
@Composable
fun FoundProductsView(
    onProductClick: (ProductSearchContent) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: FoundProductsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Conserva el comportamiento existente: si no se ha escrito nada,
    // se muestra la consulta inicial definida en los recursos.
    val queryHint = stringResource(R.string.found_products_query)
    val query = uiState.searchText.ifEmpty { queryHint }

    FoundProductsViewContent(
        results = uiState.results,
        searchText = query,
        onSearchTextChange = viewModel::onSearchTextChange,
        onProductClick = onProductClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la búsqueda, el conteo de coincidencias y la lista de productos encontrados.
 */
@Composable
fun FoundProductsViewContent(
    results: List<ProductSearchContent>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onProductClick: (ProductSearchContent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        SearchBar(
            placeholder = R.string.search_product_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = true,
            text = searchText,
            onTextChange = onSearchTextChange
        )

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(
                R.string.found_products_count,
                results.size
            ),
            color = colors.textSecondary,
            style = SearchControlText
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(
                items = results,
                key = { it.id }
            ) { product ->
                FoundProductCard(
                    product = product,
                    onClick = {
                        onProductClick(product)
                    }
                )

                Spacer(modifier = Modifier.height(14.dp))
            }

            item {
                // Deja aire para que la barra flotante no tape el último producto.
                Spacer(modifier = Modifier.height(110.dp))
            }
        }
    }
}

/** Muestra una vista previa de los productos encontrados en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun FoundProductsViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 8
        ) { innerPadding ->
            FoundProductsView(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                viewModel = FoundProductsViewModel()
            )
        }
    }
}

/** Muestra una vista previa de los productos encontrados en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun FoundProductsViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 8
        ) { innerPadding ->
            FoundProductsView(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                viewModel = FoundProductsViewModel()
            )
        }
    }
}