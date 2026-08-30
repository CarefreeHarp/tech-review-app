package com.example.devicersapp.ui.screens.search_product

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.search_product.components.SearchProductFilterPanel
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.ScreenTitleText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.search.SearchEntityToggle

/** Renderiza la búsqueda de productos y conecta sus eventos con el estado del ViewModel. */
@Composable
fun SearchProductView(
    onApplyFilters: () -> Unit = {},
    onUsersClick: () -> Unit = {},
    onProductsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: SearchProductViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchProductViewContent(
        uiState = uiState,
        onSearchTextChange = viewModel::onSearchTextChange,
        onBrandChange = viewModel::onBrandChange,
        onProductNameChange = viewModel::onProductNameChange,
        onLaunchDateChange = viewModel::onLaunchDateChange,
        onCategorySelected = viewModel::onCategorySelected,
        onRatingChange = viewModel::onRatingChange,
        onSortChange = viewModel::onSortChange,
        onClearFilters = viewModel::onClearFilters,
        onApplyFilters = onApplyFilters,
        onUsersClick = onUsersClick,
        onProductsClick = onProductsClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Reúne el título, la búsqueda general y la tarjeta de filtros de productos.
 *
 * @param uiState Estado inmutable que describe la consulta y los filtros activos.
 * @param onSearchTextChange Acción que solicita actualizar la búsqueda general.
 * @param onBrandChange Acción que solicita actualizar la marca.
 * @param onProductNameChange Acción que solicita actualizar el nombre del producto.
 * @param onLaunchDateChange Acción que solicita actualizar la fecha de lanzamiento.
 * @param onCategorySelected Acción que solicita cambiar la categoría.
 * @param onRatingChange Acción que solicita cambiar la calificación mínima.
 * @param onSortChange Acción que solicita cambiar el orden.
 * @param onClearFilters Acción que solicita restablecer los filtros.
 * @param onApplyFilters Acción que solicita aplicar los filtros.
 * @param onUsersClick Acción que solicita mostrar la búsqueda de usuarios.
 * @param onProductsClick Acción que solicita mostrar la búsqueda de productos.
 * @param modifier Permite modificar el diseño externo del contenido.
 */
@Composable
fun SearchProductViewContent(
    uiState: SearchProductState,
    onSearchTextChange: (String) -> Unit,
    onBrandChange: (String) -> Unit,
    onProductNameChange: (String) -> Unit,
    onLaunchDateChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onRatingChange: (Float) -> Unit,
    onSortChange: (String) -> Unit,
    onClearFilters: () -> Unit,
    onApplyFilters: () -> Unit,
    onUsersClick: () -> Unit,
    onProductsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // El contenido no es una colección repetida, así que basta con un contenedor desplazable.
    Column(
        modifier = modifier
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.search_product_title),
            modifier = Modifier.fillMaxWidth(),
            color = LocalDevicersColors.current.textPrimary,
            style = ScreenTitleText
        )

        Spacer(modifier = Modifier.height(20.dp))

        SearchBar(
            placeholder = R.string.search_product_placeholder,
            backgroundColor = LocalDevicersColors.current.surface,
            showSearchIcon = true,
            text = uiState.searchText,
            onTextChange = onSearchTextChange
        )

        Spacer(modifier = Modifier.height(16.dp))
        SearchEntityToggle(
            isUsersSelected = false,
            onUsersClick = onUsersClick,
            onProductsClick = onProductsClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        SearchProductFilterPanel(
            brand = uiState.brand,
            onBrandChange = onBrandChange,

            productName = uiState.productName,
            onProductNameChange = onProductNameChange,

            launchDate = uiState.launchDate,
            onLaunchDateChange = onLaunchDateChange,

            isLaunchDateValid = uiState.isLaunchDateValid,
            showLaunchDateError = uiState.showLaunchDateError,

            selectedCategory = uiState.selectedCategory,
            onCategorySelected = onCategorySelected,

            minimumRating = uiState.minimumRating,
            onRatingChange = onRatingChange,

            sortBy = uiState.sortBy,
            onSortChange = onSortChange,
            onClearFilters = onClearFilters,
            onApplyFilters = onApplyFilters
        )

        // Deja aire para que la barra flotante no tape el botón de aplicar.
        Spacer(modifier = Modifier.height(120.dp))
    }
}

/** Muestra la búsqueda de productos en tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun SearchProductViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true) { innerPadding ->
            SearchProductViewContent(
                uiState = SearchProductState(),
                onSearchTextChange = {},
                onBrandChange = {},
                onProductNameChange = {},
                onLaunchDateChange = {},
                onCategorySelected = {},
                onRatingChange = {},
                onSortChange = {},
                onClearFilters = {},
                onApplyFilters = {},
                onUsersClick = {},
                onProductsClick = {},
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}

/** Muestra la búsqueda de productos en tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun SearchProductViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true) { innerPadding ->
            SearchProductViewContent(
                uiState = SearchProductState(),
                onSearchTextChange = {},
                onBrandChange = {},
                onProductNameChange = {},
                onLaunchDateChange = {},
                onCategorySelected = {},
                onRatingChange = {},
                onSortChange = {},
                onClearFilters = {},
                onApplyFilters = {},
                onUsersClick = {},
                onProductsClick = {},
                modifier = Modifier
                    .padding(top = innerPadding.calculateTopPadding())
                    .fillMaxSize()
                    .background(LocalDevicersColors.current.background)
            )
        }
    }
}
