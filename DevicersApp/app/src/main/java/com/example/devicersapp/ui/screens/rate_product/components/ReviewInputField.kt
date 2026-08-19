package com.example.devicersapp.ui.screens.rate_product.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra un campo de texto del formulario de calificación con un límite opcional.
 *
 * @param value Texto actual del campo.
 * @param onValueChange Acción al cambiar el texto.
 * @param placeholder Texto de ayuda del campo.
 * @param modifier Modificador aplicado al campo.
 * @param singleLine Indica si el campo usa una sola línea.
 * @param minLines Cantidad mínima de líneas visibles.
 * @param maxLength Límite opcional de caracteres.
 */
@Composable
fun ReviewInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    minLines: Int = 1,
    maxLength: Int? = null
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier
    ) {

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {

            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->

                    if (maxLength == null || newValue.length <= maxLength) {
                        onValueChange(newValue)
                    }
                },
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium,
                        color = colors.textSecondary
                    )
                },
                singleLine = singleLine,
                minLines = minLines,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colors.border,
                    unfocusedBorderColor = colors.border,
                    focusedContainerColor = colors.surfaceSecondary,
                    unfocusedContainerColor = colors.surfaceSecondary,
                    focusedTextColor = colors.textPrimary,
                    unfocusedTextColor = colors.textPrimary,
                    cursorColor = colors.primaryYellow
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (maxLength != null) {
                Text(
                    text = stringResource(
                        R.string.rate_product_character_count,
                        value.length,
                        maxLength
                    ),
                    style = MaterialTheme.typography.labelSmall,
                    color = colors.textSecondary,
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(
                            end = 12.dp,
                            bottom = 8.dp
                        )
                )
            }
        }
    }
}

/** Muestra una vista previa de un campo de reseña con límite de caracteres. */
@Composable
@Preview(showBackground = true)
fun ReviewInputFieldPreview() {
    DevicersAppTheme {
        ReviewInputField(
            value = "",
            onValueChange = {},
            placeholder = stringResource(R.string.rate_product_experience_placeholder),
            maxLength = 500
        )
    }
}
