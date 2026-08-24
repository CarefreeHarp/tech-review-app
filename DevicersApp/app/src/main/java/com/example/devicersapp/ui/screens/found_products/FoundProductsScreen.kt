package com.example.devicersapp.ui.screens.found_products

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.ui.models.ProductSearchContent
import com.example.devicersapp.ui.screens.found_products.components.FoundProductCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/**
 * Configura los resultados que devuelve una búsqueda del catálogo de productos.
 *
 * @param onProductClick Acción solicitada al abrir el detalle de un producto.
 * @param modifier Modificador aplicado a la pantalla.
 */
@Composable
fun FoundProductsScreen(
    onProductClick: (ProductSearchContent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // La búsqueda llega desde la pantalla anterior y se puede seguir afinando aquí.
    var searchText by remember { mutableStateOf("") }

    val queryHint = stringResource(R.string.found_products_query)
    val query = searchText.ifEmpty { queryHint }

    FoundProductsScreenContent(
        results = LocalProductProvider.products,
        searchText = query,
        onSearchTextChange = { searchText = it },
        onProductClick = onProductClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la búsqueda, el conteo de coincidencias y la lista de productos encontrados.
 *
 * @param results Productos devueltos por la búsqueda.
 * @param searchText Texto actual de la búsqueda.
 * @param onSearchTextChange Acción que solicita actualizar la búsqueda.
 * @param onProductClick Acción solicitada al abrir el detalle de un producto.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun FoundProductsScreenContent(
    results: List<ProductSearchContent>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onProductClick: (ProductSearchContent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    LazyColumn(modifier = modifier.padding(horizontal = 20.dp)) {
        item {
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
                text = stringResource(R.string.found_products_count, results.size),
                color = colors.textSecondary,
                style = SearchControlText
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(results, key = { it.id }) { product ->
            FoundProductCard(
                product = product,
                onClick = { onProductClick(product) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
            // Deja aire para que la barra flotante no tape el último producto.
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Muestra una vista previa de los productos encontrados en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun FoundProductsScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 8
        ) { innerPadding ->
            FoundProductsScreen(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}

/** Muestra una vista previa de los productos encontrados en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun FoundProductsScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "search",
            showBottomBar = true,
            topBarNumber = 8
        ) { innerPadding ->
            FoundProductsScreen(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}
