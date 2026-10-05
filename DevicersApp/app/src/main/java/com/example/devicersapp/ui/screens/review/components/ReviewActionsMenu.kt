package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/** Renderiza las acciones de la reseña propia; el ViewModel controla apertura y operaciones. */
@Composable
fun ReviewActionsMenu(
    expanded: Boolean,
    enabled: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    Box(modifier = modifier) {
        IconButton(onClick = { onExpandedChange(true) }, enabled = enabled) {
            Icon(painterResource(R.drawable.review_more_options), stringResource(R.string.review_actions_menu), tint = colors.textPrimary)
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpandedChange(false) },
            containerColor = colors.surface) {
            DropdownMenuItem(
                text = { Text(stringResource(R.string.review_action_delete), color = colors.textPrimary) },
                enabled = enabled, onClick = onDelete
            )
            DropdownMenuItem(
                text = { Text(stringResource(R.string.review_action_edit), color = colors.textPrimary) },
                enabled = enabled, onClick = onEdit
            )
        }
    }
}

/** Muestra el botón de opciones sin ejecutar acciones remotas. */
@Preview(showBackground = true)
@Composable
fun ReviewActionsMenuPreview() {
    DevicersAppTheme {
        ReviewActionsMenu(expanded = false, enabled = true, onExpandedChange = {}, onEdit = {}, onDelete = {})
    }
}
