package com.example.devicersapp.ui.screens.create_review

import androidx.compose.foundation.background
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

/**
 * Configura la búsqueda y la categoría con las que se elige el producto a reseñar.
 *
 * @param onProductClick Acción solicitada al elegir un producto.
 * @param modifier Modificador aplicado a la pantalla.
 */
@Composable
fun CreateReviewScreen(
    onProductClick: (ProductSearchContent) -> Unit = {},
    onRequestProductClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Este estado se eleva desde los componentes para conservarlos presentacionales.
    var searchText by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("all") }

    CreateReviewScreenContent(
        categories = LocalProductProvider.categories,
        products = LocalProductProvider.products,
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        selectedCategoryId = selectedCategoryId,
        onCategoryChange = { selectedCategoryId = it },
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
 * @param categories Categorías que se pueden seleccionar.
 * @param products Productos locales disponibles para reseñar.
 * @param searchText Texto actual de búsqueda.
 * @param onSearchTextChange Acción que solicita actualizar el texto.
 * @param selectedCategoryId Identificador de la categoría activa.
 * @param onCategoryChange Acción que solicita actualizar la categoría.
 * @param onProductClick Acción solicitada al elegir un producto.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun CreateReviewScreenContent(
    categories: List<ProductCategoryContent>,
    products: List<ProductSearchContent>,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedCategoryId: String,
    onCategoryChange: (String) -> Unit,
    onProductClick: (ProductSearchContent) -> Unit,
    onRequestProductClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    // La categoría acota el catálogo y el texto escrito refina la búsqueda por nombre.
    val filteredProducts = products.filter { product ->
        val matchesCategory = selectedCategoryId == "all" || product.categoryId == selectedCategoryId
        val matchesSearch = searchText.isBlank() || product.searchTerms.any { term ->
            term.contains(searchText.trim(), ignoreCase = true)
        }
        matchesCategory && matchesSearch
    }

    LazyColumn(modifier = modifier.padding(horizontal = 20.dp)) {
        item {
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar(
                placeholder = R.string.create_review_search_placeholder,
                backgroundColor = colors.surface,
                showSearchIcon = true,
                text = searchText,
                onTextChange = onSearchTextChange
            )
            Spacer(modifier = Modifier.height(16.dp))
            CategoryChipRow(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategoryChange = onCategoryChange
            )
            Spacer(modifier = Modifier.height(22.dp))
            Text(
                text = stringResource(R.string.create_review_suggested),
                color = colors.textPrimary,
                style = SearchHeadingText
            )
            Spacer(modifier = Modifier.height(4.dp))
        }

        if (filteredProducts.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.create_review_empty_results).trim(),
                    modifier = Modifier.padding(vertical = 16.dp),
                    color = colors.textSecondary,
                    style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
                )
            }
        } else {
            itemsIndexed(filteredProducts, key = { _, product -> product.id }) { index, product ->
                ProductReviewItem(product = product, onRateClick = { onProductClick(product) })

                // Un divisor separa cada producto del siguiente sin encerrarlos en tarjetas.
                if (index < filteredProducts.lastIndex) {
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
            // Deja aire para que la barra flotante no tape la última tarjeta.
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

/** Muestra una vista previa de la creación de reseña en el tema claro. */
@Composable
@Preview(name = "Crear reseña claro", showBackground = true, heightDp = 1000)
fun CreateReviewLightPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "create",
            showBottomBar = true,
            topBarNumber = 3
        ) { innerPadding ->
            CreateReviewScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}

/** Muestra una vista previa de la creación de reseña en el tema oscuro. */
@Composable
@Preview(name = "Crear reseña oscuro", showBackground = true, heightDp = 1000)
fun CreateReviewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "create",
            showBottomBar = true,
            topBarNumber = 3
        ) { innerPadding ->
            CreateReviewScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}
