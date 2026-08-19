package com.example.devicersapp.ui.screens.create_review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import com.example.devicersapp.data.local.LocalCreateReviewScreenProvider
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.ui.models.ProductSearchContent
import com.example.devicersapp.ui.screens.create_review.components.CategoryChipRow
import com.example.devicersapp.ui.screens.create_review.components.CreateReviewFilterDialog
import com.example.devicersapp.ui.screens.create_review.components.CreateReviewSearchRow
import com.example.devicersapp.ui.screens.create_review.components.ProductReviewItem
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/**
 * Configura la búsqueda, la categoría y el diálogo de filtros de creación de reseñas.
 *
 * @param onFiltersClick Acción adicional solicitada al abrir filtros.
 * @param onProductClick Acción solicitada al elegir un producto.
 * @param modifier Modificador aplicado a la pantalla.
 */
@Composable
fun CreateReviewScreen(
    onFiltersClick: () -> Unit = {},
    onProductClick: (ProductSearchContent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Este estado se eleva desde los componentes para conservarlos presentacionales.
    var searchText by remember { mutableStateOf("") }
    var selectedCategoryId by remember { mutableStateOf("all") }
    var showFilters by remember { mutableStateOf(false) }

    CreateReviewScreenContent(
        categories = LocalCreateReviewScreenProvider.categories,
        products = LocalCreateReviewScreenProvider.products,
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        selectedCategoryId = selectedCategoryId,
        onCategoryChange = { selectedCategoryId = it },
        onFiltersClick = {
            showFilters = true
            onFiltersClick()
        },
        onProductClick = onProductClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )

    if (showFilters) {
        CreateReviewFilterDialog(
            categories = LocalCreateReviewScreenProvider.categories,
            selectedCategoryId = selectedCategoryId,
            onCategoryChange = { selectedCategoryId = it },
            onDismiss = { showFilters = false }
        )
    }
}

/**
 * Ensambla las categorías y los resultados locales filtrados de creación de reseña.
 *
 * @param categories Categorías que se pueden seleccionar.
 * @param products Productos locales disponibles para reseñar.
 * @param searchText Texto actual de búsqueda.
 * @param onSearchTextChange Acción que solicita actualizar el texto.
 * @param selectedCategoryId Identificador de la categoría activa.
 * @param onCategoryChange Acción que solicita actualizar la categoría.
 * @param onFiltersClick Acción que solicita abrir los filtros.
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
    onFiltersClick: () -> Unit,
    onProductClick: (ProductSearchContent) -> Unit,
    modifier: Modifier = Modifier
) {
    // La búsqueda conserva la escritura, pero por decisión actual solo la categoría modifica resultados.
    val filteredProducts = products.filter { product ->
        val matchesCategory = selectedCategoryId == "all" || product.categoryId == selectedCategoryId
        matchesCategory
    }
    val colors = LocalDevicersColors.current

    LazyColumn(
        modifier = modifier.padding(horizontal = 16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.create_review_subtitle),
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(12.dp))
            CreateReviewSearchRow(
                searchText = searchText,
                onSearchTextChange = onSearchTextChange,
                onFiltersClick = onFiltersClick
            )
            Spacer(modifier = Modifier.height(12.dp))
            CategoryChipRow(
                categories = categories,
                selectedCategoryId = selectedCategoryId,
                onCategoryChange = onCategoryChange
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = stringResource(R.string.create_review_categories),
                color = colors.textSecondary,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = stringResource(R.string.create_review_popular_results),
                color = colors.textSecondary,
                style = MaterialTheme.typography.labelSmall
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
        if (filteredProducts.isEmpty()) {
            item {
                Text(
                    text = stringResource(R.string.create_review_empty_results),
                    color = colors.textSecondary,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        } else {
            items(filteredProducts, key = { it.id }) { product ->
                ProductReviewItem(product = product, onRateClick = { onProductClick(product) })
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        item {
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(R.string.create_review_product_missing_title),
                color = colors.textPrimary,
                style = MaterialTheme.typography.labelLarge
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(R.string.create_review_product_missing_description),
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

/** Muestra una vista previa clara de la pantalla de creación de reseña. */
@Composable
@Preview(name = "Crear reseña claro", showBackground = true)
fun CreateReviewLightPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "add", showBottomBar = true, topBarNumber = 3) { innerPadding ->
            CreateReviewScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

/** Muestra una vista previa oscura de la pantalla de creación de reseña. */
@Composable
@Preview(name = "Crear reseña oscuro", showBackground = true)
fun CreateReviewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "add", showBottomBar = true, topBarNumber = 3) { innerPadding ->
            CreateReviewScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
