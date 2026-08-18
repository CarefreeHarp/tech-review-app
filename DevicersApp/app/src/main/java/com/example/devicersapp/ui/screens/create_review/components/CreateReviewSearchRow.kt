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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Agrupa el campo de búsqueda controlado y la acción que abre los filtros.
 *
 * @param searchText Texto actual de búsqueda.
 * @param onSearchTextChange Acción que solicita actualizar el texto.
 * @param onFiltersClick Acción que solicita mostrar los filtros.
 * @param modifier Modificador aplicado a la fila.
 */
@Composable
fun CreateReviewSearchRow(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onFiltersClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(modifier = modifier.fillMaxWidth()) {
        CreateReviewSearchBar(
            value = searchText,
            onValueChange = onSearchTextChange,
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Button(
            onClick = onFiltersClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.surfaceSecondary,
                contentColor = colors.textPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.create_review_filters),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}

/** Muestra una vista previa de la fila de búsqueda y filtros. */
@Composable
@Preview(showBackground = true)
fun CreateReviewSearchRowPreview() {
    DevicersAppTheme {
        CreateReviewSearchRow(searchText = "", onSearchTextChange = {}, onFiltersClick = {})
    }
}
