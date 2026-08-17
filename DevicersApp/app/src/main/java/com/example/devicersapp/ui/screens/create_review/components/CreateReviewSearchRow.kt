package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Agrupa el campo de búsqueda y el botón de filtros.
 *
 * El estado de búsqueda se recibe desde el componente padre
 * siguiendo el patrón de state hoisting.
 */
@Composable
fun CreateReviewSearchRow(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onFiltersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {

        CreateReviewSearchBar(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.weight(1f)
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Button(
            onClick = onFiltersClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Text(
                text = "Filtros",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}