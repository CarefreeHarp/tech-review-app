package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun CreateReviewFilterDialog(
    selectedCategory: String,
    onCategoryChange: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val categories = listOf(
        "Todos",
        "Celulares",
        "Audio",
        "Computadores"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Filtrar productos",
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            androidx.compose.foundation.layout.Column {
                categories.forEach { category ->
                    TextButton(
                        onClick = {
                            onCategoryChange(category)
                            onDismiss()
                        }
                    ) {
                        Text(
                            text = if (selectedCategory == category) {
                                "✓ $category"
                            } else {
                                category
                            },
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text("Cerrar")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}
@Composable
private fun ProductSelectedDialog(
    productName: String,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Crear reseña")
        },
        text = {
            Text(
                text = "Seleccionaste $productName para calificar."
            )
        },
        confirmButton = {
            androidx.compose.material3.TextButton(
                onClick = onDismiss
            ) {
                Text("Continuar")
            }
        },
        dismissButton = {
            androidx.compose.material3.TextButton(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}