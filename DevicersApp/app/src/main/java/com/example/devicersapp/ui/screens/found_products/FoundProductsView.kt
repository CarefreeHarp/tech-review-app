package com.example.devicersapp.ui.screens.found_products

import com.example.devicersapp.ui.screens.found_products.components.FoundProductsSkeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.screens.found_products.components.FoundProductCard
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.navigation.SearchBar

/**
 * Configura los resultados de productos y observa su estado desde el ViewModel.
 */
@Composable
fun FoundProductsView(
    productName: String,
    category: String,
    minimumRating: Float,
    sortBy: String,
    onProductClick: (ProductInfo) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: FoundProductsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(
        productName,
        category,
        minimumRating,
        sortBy
    ) {
        viewModel.loadProducts(
            productName,
            category,
            minimumRating,
            sortBy
        )
    }

    FoundProductsViewContent(
        state = uiState,
        onSearchTextChange = viewModel::onSearchTextChange,
        onProductClick = onProductClick,
        onRetry = {
            viewModel.loadProducts(
                productName,
                category,
                minimumRating,
                sortBy
            )
        },
        modifier = modifier
            .fillMaxSize()
            .background(
                LocalDevicersColors.current.background
            )
    )
}

/** Ensambla la búsqueda y muestra la carga, el error o los productos encontrados. */
@Composable
fun FoundProductsViewContent(
    state: FoundProductsState,
    onSearchTextChange: (String) -> Unit,
    onProductClick: (ProductInfo) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    if (!state.isLoading && state.errorMessage != null) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "No se pudieron cargar los productos",
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = state.errorMessage,
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = onRetry
            ) {
                Text(
                    text = "Reintentar"
                )
            }
        }

        return
    }

    Column(
        modifier = modifier.padding(
            horizontal = 20.dp
        )
    ) {

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        SearchBar(
            placeholder = R.string.search_product_placeholder,
            backgroundColor = colors.surface,
            showSearchIcon = true,
            text = state.searchText,
            onTextChange = onSearchTextChange
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        if (state.isLoading) {
            FoundProductsSkeleton(modifier = Modifier.weight(1f))
            return@Column
        }

        Text(
            text = stringResource(
                R.string.found_products_count,
                state.results.size
            ),
            color = colors.textSecondary,
            style = SearchControlText
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(
                items = state.results,
                key = { product ->
                    product.id
                }
            ) { product ->

                FoundProductCard(
                    product = product,
                    onClick = {
                        onProductClick(product)
                    }
                )

                Spacer(
                    modifier = Modifier.height(14.dp)
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(110.dp)
                )
            }
        }
    }
}



///** Muestra una vista previa de los productos encontrados en el tema claro. */
//@Composable
//@Preview(showBackground = true, heightDp = 900)
//fun FoundProductsViewPreview() {
//    DevicersAppTheme(darkTheme = false) {
//        DevicersScaffold(
//            selectedItem = "search",
//            showBottomBar = true,
//            topBarNumber = 8
//        ) { innerPadding ->
//            FoundProductsViewContent(
//                modifier = Modifier.padding(
//                    top = innerPadding.calculateTopPadding()
//                ),
//                state = FoundProductsState(results = LocalProductProvider.products),
//                onSearchTextChange = {},
//                onProductClick = {}
//            )
//        }
//    }
//}
//
///** Muestra una vista previa de los productos encontrados en el tema oscuro. */
//@Composable
//@Preview(showBackground = true, heightDp = 900)
//fun FoundProductsViewDarkPreview() {
//    DevicersAppTheme(darkTheme = true) {
//        DevicersScaffold(
//            selectedItem = "search",
//            showBottomBar = true,
//            topBarNumber = 8
//        ) { innerPadding ->
//            FoundProductsViewContent(
//                modifier = Modifier.padding(
//                    top = innerPadding.calculateTopPadding()
//                ),
//                state = FoundProductsState(results = LocalProductProvider.products),
//                onSearchTextChange = {},
//                onProductClick = {}
//            )
//        }
//    }
//}
