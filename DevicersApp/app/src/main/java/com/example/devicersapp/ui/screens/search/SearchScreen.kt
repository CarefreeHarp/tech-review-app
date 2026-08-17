package com.example.devicersapp.ui.screens.search

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.screens.search.components.SearchFilterPanel
import com.example.devicersapp.ui.theme.SearchScreenTitleText
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar
import com.example.devicersapp.ui.utils.navigation.SearchBar

/**
 * Configura la pantalla de búsqueda y establece su fondo.
 *
 * La pantalla delega la construcción de su contenido visual a
 * [SearchScreenContent] para mantener separada la configuración
 * general de la composición de los elementos.
 *
 * @param onApplyFilters Acción solicitada al aplicar los filtros visibles.
 * @param modifier Permite modificar el diseño externo de la pantalla.
 */
@Composable
fun SearchScreen(
    onApplyFilters: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var searchText by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var launchDate by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("all") }
    var minimumRating by remember { mutableStateOf(4f) }
    var sortBy by remember { mutableStateOf("recent") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.background_light),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = "search",
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        SearchScreenContent(
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
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

/**
 * Reúne los elementos visuales de la pantalla de búsqueda.
 *
 * La pantalla muestra un título centrado, una barra de búsqueda y
 * los filtros dentro de una lista desplazable.
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
 * @param modifier Permite modificar el diseño externo del contenido.
 */
@Composable
fun SearchScreenContent(
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
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        item {

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.search),
                modifier = Modifier.fillMaxWidth(),
                color = colorResource(R.color.text_primary_light),
                style = SearchScreenTitleText,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(25.dp))

            SearchBar(
                placeholder = R.string.search_placeholder,
                backgroundColor = R.color.surface_light,
                showSearchIcon = true,
                text = searchText,
                onTextChange = onSearchTextChange
            )

            Spacer(modifier = Modifier.height(5.dp))

            SearchFilterPanel(
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

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

/**
 * Muestra una vista previa de la pantalla completa de búsqueda.
 */
@Composable
@Preview(showBackground = true)
fun SearchScreenPreview() {
    SearchScreen()
}
