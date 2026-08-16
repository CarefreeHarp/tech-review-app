package com.example.devicersapp.ui.screens.rateProduct.components

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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

// Muestra un campo de texto reutilizable para el formulario de calificación.
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
                        color = colorResource(R.color.text_secondary_light)
                    )
                },
                singleLine = singleLine,
                minLines = minLines,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = colorResource(R.color.border_light),
                    unfocusedBorderColor = colorResource(R.color.border_light),
                    focusedContainerColor = colorResource(R.color.surface_secondary_light),
                    unfocusedContainerColor = colorResource(R.color.surface_secondary_light)
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (maxLength != null) {
                Text(
                    text = "${value.length}/$maxLength",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorResource(R.color.text_secondary_light),
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