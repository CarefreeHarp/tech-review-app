package com.example.devicersapp.ui.screens.search_product

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.devicersapp.ui.screens.search_product.components.SearchProductFilterPanel
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.ScreenTitleText
import com.example.devicersapp.ui.utils.navigation.SearchBar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.search.SearchEntityToggle

/**
 * Configura la pantalla de búsqueda y establece su fondo.
 *
 * La pantalla delega la construcción de su contenido visual a
 * [SearchProductScreenContent] para mantener separada la configuración
 * general de la composición de los elementos.
 *
 * @param onApplyFilters Acción solicitada al aplicar los filtros visibles.
 * @param modifier Permite modificar el diseño externo de la pantalla.
 */
@Composable
fun SearchProductScreen(
    onApplyFilters: () -> Unit = {},
    onUsersClick: () -> Unit = {},
    onProductsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // La pantalla es propietaria del estado y los componentes reciben valores y callbacks.
    var searchText by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var launchDate by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }
    var minimumRating by remember { mutableStateOf(4f) }
    var sortBy by remember { mutableStateOf("recent") }

    SearchProductScreenContent(
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        brand = brand,
        onBrandChange = { brand = it },
        productName = productName,
        onProductNameChange = { productName = it },
        launchDate = launchDate,
        onLaunchDateChange = { launchDate = it },
        selectedCategory = selectedCategory,
        onCategorySelected = { selectedCategory = it },
        minimumRating = minimumRating,
        onRatingChange = { minimumRating = it },
        sortBy = sortBy,
        onSortChange = { sortBy = it },
        onClearFilters = {
            brand = ""
            productName = ""
            launchDate = ""
            selectedCategory = "all"
            minimumRating = 4f
            sortBy = "recent"
        },
        onApplyFilters = onApplyFilters,
        onUsersClick = onUsersClick,
        onProductsClick = onProductsClick,
        modifier = modifier.fillMaxSize().background(LocalDevicersColors.current.background)
    )
}

/**
 * Reúne los elementos visuales de la pantalla de búsqueda.
 *
 * La pantalla muestra el título, la barra de búsqueda y la tarjeta
 * que agrupa todos los filtros disponibles.
 *
 * @param searchText Texto actual de la búsqueda general.
 * @param onSearchTextChange Acción que solicita actualizar la búsqueda general.
 * @param brand Marca seleccionada para filtrar.
 * @param onBrandChange Acción que solicita actualizar la marca.
 * @param productName Nombre de producto seleccionado para filtrar.
 * @param onProductNameChange Acción que solicita actualizar el nombre del producto.
 * @param launchDate Fecha de lanzamiento escrita por el usuario.
 * @param onLaunchDateChange Acción que solicita actualizar la fecha de lanzamiento.
 * @param selectedCategory Identificador de categoría activa.
 * @param onCategorySelected Acción que solicita cambiar la categoría.
 * @param minimumRating Calificación mínima seleccionada.
 * @param onRatingChange Acción que solicita cambiar la calificación mínima.
 * @param sortBy Identificador del orden activo.
 * @param onSortChange Acción que solicita cambiar el orden.
 * @param onClearFilters Acción que solicita restablecer los filtros.
 * @param onApplyFilters Acción que solicita aplicar los filtros.
 * @param onUsersClick Acción que solicita mostrar la búsqueda de usuarios.
 * @param onProductsClick Acción que solicita mostrar la búsqueda de productos.
 * @param modifier Permite modificar el diseño externo del contenido.
 */
@Composable
fun SearchProductScreenContent(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    brand: String,
    onBrandChange: (String) -> Unit,
    productName: String,
    onProductNameChange: (String) -> Unit,
    launchDate: String,
    onLaunchDateChange: (String) -> Unit,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    minimumRating: Float,
    onRatingChange: (Float) -> Unit,
    sortBy: String,
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
            text = searchText,
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
            brand = brand,
            onBrandChange = onBrandChange,

            productName = productName,
            onProductNameChange = onProductNameChange,

            launchDate = launchDate,
            onLaunchDateChange = onLaunchDateChange,

            selectedCategory = selectedCategory,
            onCategorySelected = onCategorySelected,

            minimumRating = minimumRating,
            onRatingChange = onRatingChange,

            sortBy = sortBy,
            onSortChange = onSortChange,
            onClearFilters = onClearFilters,
            onApplyFilters = onApplyFilters
        )

        // Deja aire para que la barra flotante no tape el botón de aplicar.
        Spacer(modifier = Modifier.height(120.dp))
    }
}

/** Muestra una vista previa de la pantalla de búsqueda en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun SearchProductScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true) { innerPadding ->
            SearchProductScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}

/** Muestra una vista previa de la pantalla de búsqueda en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun SearchProductScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "search", showBottomBar = true) { innerPadding ->
            SearchProductScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}
