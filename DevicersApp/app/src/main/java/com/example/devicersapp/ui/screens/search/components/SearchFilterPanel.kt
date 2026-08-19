package com.example.devicersapp.ui.screens.search.components

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.navigation.FilterChip
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
fun SearchFilterPanel(
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
            .padding(top = 14.dp)
            .background(
                color = LocalDevicersColors.current.surfaceSecondary,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(12.dp)
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
                color = LocalDevicersColors.current.textSecondary,
                style = SearchControlText
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        FilterLabel(R.string.brand)
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(
            placeholder = R.string.brand_placeholder,
            backgroundColor = LocalDevicersColors.current.background,
            showSearchIcon = false,
            text = brand,
            onTextChange = onBrandChange
        )

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.product_name)
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(
            placeholder = R.string.product_name_placeholder,
            backgroundColor = LocalDevicersColors.current.background,
            showSearchIcon = false,
            text = productName,
            onTextChange = onProductNameChange
        )

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.launch_date)
        Spacer(modifier = Modifier.height(5.dp))
        SearchBar(
            placeholder = R.string.launch_date_placeholder,
            backgroundColor = LocalDevicersColors.current.background,
            showSearchIcon = false,
            text = launchDate,
            // El filtrado evita letras, incluso cuando el contenido llega por pegado de texto.
            onTextChange = { onLaunchDateChange(formatLaunchDate(it)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        if (showLaunchDateError) {
            Text(
                text = stringResource(R.string.launch_date_invalid),
                modifier = Modifier.padding(top = 4.dp),
                color = LocalDevicersColors.current.error,
                style = SearchControlText
            )
        }

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.category)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
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

        Spacer(modifier = Modifier.height(14.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterLabel(R.string.minimum_rating, Modifier.weight(1f))
            Text(
                text = stringResource(R.string.minimum_rating_format, minimumRating),
                color = LocalDevicersColors.current.textPrimary,
                style = SearchControlText
            )
        }
        RatingSlider(
            value = minimumRating,
            onValueChange = onRatingChange
        )

        Spacer(modifier = Modifier.height(12.dp))
        FilterLabel(R.string.sort_by)
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            FilterChip(
                R.string.most_recent,
                selected = sortBy == "recent",
                onClick = { onSortChange("recent") },
                modifier = Modifier.weight(1f)
            )

            FilterChip(
                R.string.best_rated,
                selected = sortBy == "rated",
                onClick = { onSortChange("rated") },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                // Una fecha parcial o imposible no puede aplicarse como criterio de búsqueda.
                .clickable(enabled = isLaunchDateValid, onClick = onApplyFilters)
                .height(38.dp)
                .background(
                    color = LocalDevicersColors.current.primaryYellow,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.apply_filters),
                color = LocalDevicersColors.current.textOnPrimary,
                style = SearchControlText
            )
        }
    }
}

/**
 * Conserva únicamente los ocho dígitos de una fecha y muestra el formato DD / MM / AAAA.
 *
 * @param input Contenido recibido desde el campo de fecha.
 * @return Fecha parcial o completa sin caracteres no numéricos.
 */
private fun formatLaunchDate(input: String): String {
    val digits = input.filter(Char::isDigit).take(8)
    return buildString {
        digits.forEachIndexed { index, digit ->
            if (index == 2 || index == 4) append(" / ")
            append(digit)
        }
    }
}

/**
 * Comprueba que una fecha completa tenga un mes y día válidos, incluidos los años bisiestos.
 *
 * @param formattedDate Fecha con el formato DD / MM / AAAA.
 * @return `true` solo si la fecha contiene ocho dígitos y existe en el calendario gregoriano.
 */
private fun isValidLaunchDate(formattedDate: String): Boolean {
    val digits = formattedDate.filter(Char::isDigit)
    if (digits.length != 8) return false

    val day = digits.substring(0, 2).toInt()
    val month = digits.substring(2, 4).toInt()
    val year = digits.substring(4, 8).toInt()
    val maximumDays = when (month) {
        1, 3, 5, 7, 8, 10, 12 -> 31
        4, 6, 9, 11 -> 30
        2 -> if (isLeapYear(year)) 29 else 28
        else -> return false
    }

    return day in 1..maximumDays
}

/**
 * Determina si un año es bisiesto según las reglas del calendario gregoriano.
 *
 * @param year Año de cuatro dígitos recibido desde el campo de fecha.
 * @return `true` cuando febrero tiene veintinueve días.
 */
private fun isLeapYear(year: Int): Boolean = year % 400 == 0 || year % 4 == 0 && year % 100 != 0

/** Muestra una vista previa del panel de filtros. */
@Composable
@Preview(showBackground = true)
fun SearchFilterPanelPreview() {
    SearchFilterPanel(
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
