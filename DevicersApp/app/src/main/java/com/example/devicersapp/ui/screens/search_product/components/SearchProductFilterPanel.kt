package com.example.devicersapp.ui.screens.search_product.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.navigation.FilterChip
import com.example.devicersapp.ui.utils.search.FilterLabel
import com.example.devicersapp.ui.utils.search.isValidLaunchDate
import com.example.devicersapp.ui.utils.navigation.SearchBar

/**
 * Muestra el panel visual con los filtros disponibles para la búsqueda.
 *
 * @param brand Marca escrita para filtrar.
 * @param onBrandChange Acción que solicita actualizar la marca.
 * @param productName Nombre de producto escrito para filtrar.
 * @param onProductNameChange Acción que solicita actualizar el nombre de producto.
 * @param launchDate Fecha de lanzamiento escrita para filtrar.
 * @param onLaunchDateChange Acción que solicita actualizar la fecha de lanzamiento.
 * @param selectedCategory Identificador de categoría activa.
 * @param onCategorySelected Acción que solicita cambiar la categoría.
 * @param minimumRating Calificación mínima activa.
 * @param onRatingChange Acción que solicita cambiar la calificación mínima.
 * @param sortBy Identificador del orden activo.
 * @param onSortChange Acción que solicita cambiar el orden.
 * @param onClearFilters Acción que solicita restablecer todos los filtros.
 * @param onApplyFilters Acción que solicita aplicar los filtros.
 * @param modifier Modificador aplicado al panel.
 */
@Composable
fun SearchProductFilterPanel(
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
    val isLaunchDateValid = launchDate.isBlank() || isValidLaunchDate(launchDate)
    val showLaunchDateError = launchDate.isNotBlank() && launchDate.filter(Char::isDigit).length == 8 && !isLaunchDateValid

    Column(
        modifier = modifier
            .fillMaxWidth()
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(18.dp))
            // La superficie secundaria agrupa los filtros y los separa del fondo de la pantalla.
            .background(
                color = LocalDevicersColors.current.surfaceSecondary,
                shape = RoundedCornerShape(18.dp)
            )
            .padding(18.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.search_filters),
                color = LocalDevicersColors.current.textPrimary,
                style = SearchHeadingText
            )
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.clear_filters),
                modifier = Modifier.clickable(onClick = onClearFilters),
                color = LocalDevicersColors.current.primaryText,
                style = SearchControlText,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        FilterLabel(R.string.brand)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.brand_placeholder,
            backgroundColor = LocalDevicersColors.current.surface,
            showSearchIcon = false,
            height = 42.dp,
            text = brand,
            onTextChange = onBrandChange
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterLabel(R.string.product_name)
        Spacer(modifier = Modifier.height(7.dp))
        SearchBar(
            placeholder = R.string.product_name_placeholder,
            backgroundColor = LocalDevicersColors.current.surface,
            showSearchIcon = false,
            height = 42.dp,
            text = productName,
            onTextChange = onProductNameChange
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterLabel(R.string.launch_date)
        Spacer(modifier = Modifier.height(7.dp))
        LaunchDateField(
            launchDate = launchDate,
            placeholder = R.string.launch_date_placeholder,
            onLaunchDateChange = onLaunchDateChange
        )
        if (showLaunchDateError) {
            Text(
                text = stringResource(R.string.launch_date_invalid),
                modifier = Modifier.padding(top = 4.dp),
                color = LocalDevicersColors.current.error,
                style = SearchControlText
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        FilterLabel(R.string.category)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                R.string.all,
                selected = selectedCategory == "all",
                onClick = { onCategorySelected("all") }
            )

            FilterChip(
                R.string.cellphones,
                selected = selectedCategory == "cellphones",
                onClick = { onCategorySelected("cellphones") }
            )

            FilterChip(
                R.string.audio,
                selected = selectedCategory == "audio",
                onClick = { onCategorySelected("audio") }
            )

            FilterChip(
                R.string.computers,
                selected = selectedCategory == "computers",
                onClick = { onCategorySelected("computers") }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterLabel(R.string.minimum_rating, Modifier.weight(1f))
            Text(
                text = stringResource(R.string.minimum_rating_format, minimumRating),
                color = LocalDevicersColors.current.textPrimary,
                style = SearchControlText,
                fontWeight = FontWeight.Bold
            )
        }
        RatingSlider(
            value = minimumRating,
            onValueChange = onRatingChange
        )

        Spacer(modifier = Modifier.height(16.dp))
        FilterLabel(R.string.sort_by)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                R.string.most_recent,
                selected = sortBy == "recent",
                onClick = { onSortChange("recent") }
            )

            FilterChip(
                R.string.best_rated,
                selected = sortBy == "rated",
                onClick = { onSortChange("rated") }
            )
        }

        Spacer(modifier = Modifier.height(22.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Una fecha parcial o imposible no puede aplicarse como criterio de búsqueda.
                .clickable(enabled = isLaunchDateValid, onClick = onApplyFilters)
                .height(46.dp)
                .background(
                    color = LocalDevicersColors.current.primary,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.apply_filters),
                color = LocalDevicersColors.current.textOnPrimary,
                style = SearchControlText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** Muestra una vista previa del panel de filtros. */
@Composable
@Preview(showBackground = true, heightDp = 700)
fun SearchProductFilterPanelPreview() {
    DevicersAppTheme {
        SearchProductFilterPanel(
            brand = "",
            onBrandChange = {},

            productName = "",
            onProductNameChange = {},

            launchDate = "",
            onLaunchDateChange = {},

            selectedCategory = "all",
            onCategorySelected = {},

            minimumRating = 4f,
            onRatingChange = {},

            sortBy = "recent",
            onSortChange = {},
            onClearFilters = {},
            onApplyFilters = {},

            modifier = Modifier.padding(20.dp)
        )
    }
}
