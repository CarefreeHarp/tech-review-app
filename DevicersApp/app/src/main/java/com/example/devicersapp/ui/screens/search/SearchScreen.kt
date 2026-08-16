package com.example.devicersapp.ui.screens.search

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
 * @param modifier Permite modificar el diseño externo de la pantalla.
 */
@Composable
fun SearchScreen(modifier: Modifier = Modifier) {
    SearchScreenContent(
        modifier = modifier
            .fillMaxSize()
            .background(colorResource(R.color.background_light))
    )
}

/**
 * Reúne los elementos visuales de la pantalla de búsqueda.
 *
 * La pantalla muestra un título centrado, una barra de búsqueda y
 * los filtros. La navegación inferior permanece fija mientras
 * el contenido superior puede desplazarse.
 *
 * @param modifier Permite modificar el diseño externo del contenido.
 */
@Composable
fun SearchScreenContent(modifier: Modifier = Modifier) {
    var searchText by remember { mutableStateOf("") }

    var brand by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var launchDate by remember { mutableStateOf("") }

    var selectedCategory by remember { mutableStateOf("all") }
    var minimumRating by remember { mutableStateOf(4f) }
    var sortBy by remember { mutableStateOf("recent") }
    Column(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {

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
                onTextChange = { searchText = it }
            )

            Spacer(modifier = Modifier.height(5.dp))

            SearchFilterPanel(
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
                onSortChange = { sortBy = it }
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

        BottomNavigationBar(
            modifier = Modifier.fillMaxWidth(),
            selectedItem = "search"
        )
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
