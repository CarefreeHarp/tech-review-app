package com.example.devicersapp.ui.screens.create_review

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.screens.create_review.components.CategoryChipRow
import com.example.devicersapp.ui.screens.create_review.components.CreateReviewFilterDialog
import com.example.devicersapp.ui.screens.create_review.components.CreateReviewHeader
import com.example.devicersapp.ui.screens.create_review.components.CreateReviewSearchRow
import com.example.devicersapp.ui.screens.create_review.components.ProductReviewItem
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.R

/**
 * Pantalla para seleccionar el producto que el usuario quiere reseñar.
 *
 * Administra el estado de búsqueda, categoría y filtros.
 * Los eventos se envían a los componentes hijos mediante state hoisting.
 */
@Composable
fun CreateReviewScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onFiltersClick: () -> Unit = {},
    onProductClick: (ProductContent) -> Unit = {},
) {

    var searchText by remember {
        mutableStateOf("")
    }

    var selectedCategory by remember {
        mutableStateOf("Todos")
    }

    var showFilters by remember {
        mutableStateOf(false)
    }

    CreateReviewScreenContent(
        searchText = searchText,
        onSearchTextChange = {
            searchText = it
        },
        selectedCategory = selectedCategory,
        onCategoryChange = {
            selectedCategory = it
        },
        onBackClick = onBackClick,
        onFiltersClick = {
            showFilters = true

            // Conservamos también el callback externo.
            onFiltersClick()
        },
        onProductClick = onProductClick,
        modifier = modifier
    )

    if (showFilters) {
        CreateReviewFilterDialog(
            selectedCategory = selectedCategory,
            onCategoryChange = {
                selectedCategory = it
            },
            onDismiss = {
                showFilters = false
            }
        )
    }
}

/**
 * Contenido visual de la pantalla Crear reseña.
 */
@Composable
fun CreateReviewScreenContent(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onFiltersClick: () -> Unit,
    onProductClick: (ProductContent) -> Unit,
    modifier: Modifier = Modifier
) {

    /*
     * Productos almacenados actualmente en la base quemada.
     */
    val headphones = ProductContent(
        name = "Auriculares",
        brand = "Marca · Audio",
        imageResId = R.drawable.devicers_headphones_black,
        imageDescription = "Auriculares"
    )

    val phone = ProductContent(
        name = "Teléfono",
        brand = "Marca · Celulares",
        imageResId = R.drawable.electronic_phone,
        imageDescription = "Teléfono"
    )

    /*
     * Filtrado de Auriculares.
     */
    val showHeadphones =
        (selectedCategory == "Todos" || selectedCategory == "Audio") &&
                (
                        searchText.isBlank() ||
                                headphones.name.contains(
                                    searchText,
                                    ignoreCase = true
                                ) ||
                                headphones.brand.contains(
                                    searchText,
                                    ignoreCase = true
                                )
                        )

    /*
     * Filtrado de Teléfono.
     */
    val showPhone =
        (selectedCategory == "Todos" || selectedCategory == "Celulares") &&
                (
                        searchText.isBlank() ||
                                phone.name.contains(
                                    searchText,
                                    ignoreCase = true
                                ) ||
                                phone.brand.contains(
                                    searchText,
                                    ignoreCase = true
                                )
                        )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
    ) {
        CreateReviewHeader(
            onBackClick = onBackClick,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Busca el producto que quieres reseñar.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        CreateReviewSearchRow(
            searchText = searchText,
            onSearchTextChange = onSearchTextChange,
            onFiltersClick = onFiltersClick
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        CategoryChipRow(
            selectedCategory = selectedCategory,
            onCategoryChange = onCategoryChange
        )

        Spacer(
            modifier = Modifier.height(14.dp)
        )

        Text(
            text = "CATEGORÍAS",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "RESULTADOS POPULARES",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        if (showHeadphones) {

                ProductReviewItem(
                    title = headphones.name,
                    subtitle = headphones.brand,
                    imageResId = headphones.imageResId,
                    rating = 5,
                    onRateClick = {
                        onProductClick(headphones)
                    }
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
        }

        if (showPhone) {

                ProductReviewItem(
                    title = phone.name,
                    subtitle = phone.brand,
                    imageResId = phone.imageResId,
                    rating = 5,
                    onRateClick = {
                        onProductClick(phone)
                    }
                )

                Spacer(
                    modifier = Modifier.height(12.dp)
                )
        }

        if (!showHeadphones && !showPhone) {

                Text(
                    text = "No encontramos productos con esos filtros.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 13.sp
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )
        }

        Spacer(
            modifier = Modifier.height(6.dp)
        )

        Text(
            text = "¿No encuentras tu producto?",
            color = MaterialTheme.colorScheme.onBackground,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp
        )

        Spacer(
            modifier = Modifier.height(3.dp)
        )

        Text(
            text = "Prueba buscando por marca o modelo.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp
        )

        Spacer(
            modifier = Modifier.height(28.dp)
        )
    }
}

/**
 * Preview modo claro.
 */
@Composable
@Preview(showBackground = true)
fun CreateReviewLightPreview() {
    DevicersAppTheme(
        darkTheme = false
    ) {
        DevicersScaffold(selectedItem = "add") { innerPadding ->
            CreateReviewScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

/**
 * Preview modo oscuro.
 */
@Composable
@Preview(showBackground = true)
fun CreateReviewDarkPreview() {
    DevicersAppTheme(
        darkTheme = true
    ) {
        DevicersScaffold(selectedItem = "add") { innerPadding ->
            CreateReviewScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
