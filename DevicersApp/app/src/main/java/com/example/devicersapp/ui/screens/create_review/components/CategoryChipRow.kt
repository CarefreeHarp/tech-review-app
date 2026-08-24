package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.navigation.FilterChip

/**
 * Muestra las categorías disponibles y comunica cuál fue seleccionada.
 *
 * @param categories Categorías que se pueden elegir.
 * @param selectedCategoryId Identificador de la categoría seleccionada.
 * @param onCategoryChange Acción que solicita cambiar la categoría activa.
 * @param modifier Modificador aplicado a la fila.
 */
@Composable
fun CategoryChipRow(
    categories: List<ProductCategoryContent>,
    selectedCategoryId: String,
    onCategoryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            // Reutiliza el mismo chip que filtra la búsqueda, para no repetir su apariencia.
            FilterChip(
                textResId = category.labelResId,
                selected = category.id == selectedCategoryId,
                onClick = { onCategoryChange(category.id) }
            )
        }
    }
}

/** Muestra una vista previa de las categorías de creación de reseña. */
@Composable
@Preview(showBackground = true)
fun CategoryChipRowPreview() {
    DevicersAppTheme {
        CategoryChipRow(
            categories = LocalProductProvider.categories,
            selectedCategoryId = "all",
            onCategoryChange = {}
        )
    }
}
