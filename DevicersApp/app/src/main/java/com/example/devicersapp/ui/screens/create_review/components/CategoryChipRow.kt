package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.data.local.LocalCreateReviewScreenProvider
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

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
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        categories.forEach { category ->
            val isSelected = category.id == selectedCategoryId
            Text(
                text = stringResource(category.labelResId),
                style = MaterialTheme.typography.labelSmall,
                color = if (isSelected) colors.textOnPrimary else colors.textPrimary,
                modifier = Modifier
                    .background(
                        color = if (isSelected) colors.primaryYellow else colors.surfaceSecondary,
                        shape = RoundedCornerShape(10.dp)
                    )
                    .clickable { onCategoryChange(category.id) }
                    .padding(horizontal = 10.dp, vertical = 8.dp)
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
            categories = LocalCreateReviewScreenProvider.categories,
            selectedCategoryId = "all",
            onCategoryChange = {}
        )
    }
}
