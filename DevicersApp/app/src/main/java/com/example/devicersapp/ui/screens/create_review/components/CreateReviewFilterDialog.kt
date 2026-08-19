package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalCreateReviewScreenProvider
import com.example.devicersapp.ui.models.ProductCategoryContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra el selector de categorías dentro de un diálogo de filtros.
 *
 * @param categories Categorías disponibles para filtrar productos.
 * @param selectedCategoryId Identificador de la categoría seleccionada.
 * @param onCategoryChange Acción que solicita actualizar la categoría.
 * @param onDismiss Acción que solicita cerrar el diálogo.
 * @param modifier Modificador aplicado al diálogo.
 */
@Composable
fun CreateReviewFilterDialog(
    categories: List<ProductCategoryContent>,
    selectedCategoryId: String,
    onCategoryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    AlertDialog(
        onDismissRequest = onDismiss,
        modifier = modifier,
        title = {
            Text(
                text = stringResource(R.string.create_review_filter_title),
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )
        },
        text = {
            Column {
                categories.forEach { category ->
                    TextButton(
                        onClick = {
                            onCategoryChange(category.id)
                            onDismiss()
                        },
                    ) {
                        val label = stringResource(category.labelResId)
                        Text(
                            text = if (selectedCategoryId == category.id) {
                                stringResource(R.string.create_review_selected_category, label)
                            } else {
                                label
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textPrimary
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
            ) {
                Text(
                    text = stringResource(R.string.create_review_filter_close),
                    color = colors.textPrimary
                )
            }
        },
        containerColor = colors.surface
    )
}

/** Muestra una vista previa del diálogo de filtros de creación de reseña. */
@Composable
@Preview(showBackground = true)
fun CreateReviewFilterDialogPreview() {
    DevicersAppTheme {
        CreateReviewFilterDialog(
            categories = LocalCreateReviewScreenProvider.categories,
            selectedCategoryId = "all",
            onCategoryChange = {},
            onDismiss = {}
        )
    }
}
