package com.example.devicersapp.ui.screens.search_product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.annotation.StringRes
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.search.formatLaunchDate

/**
 * Muestra el campo de fecha de lanzamiento y mantiene el cursor al final tras aplicar su formato.
 *
 * @param launchDate Fecha mostrada con el formato DD / MM / AAAA.
 * @param placeholder Recurso mostrado cuando no hay fecha escrita.
 * @param onLaunchDateChange Acción que solicita actualizar la fecha formateada.
 * @param modifier Modificador aplicado al campo.
 */
@Composable
fun LaunchDateField(
    launchDate: String,
    @StringRes placeholder: Int,
    onLaunchDateChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    var fieldValue by remember(launchDate) {
        mutableStateOf(
            TextFieldValue(
                text = launchDate,
                selection = TextRange(launchDate.length)
            )
        )
    }

    BasicTextField(
        value = fieldValue,
        onValueChange = { inputValue ->
            val formattedDate = formatLaunchDate(inputValue.text)
            // Al añadir separadores, el cursor debe avanzar para evitar insertar dígitos en medio.
            fieldValue = TextFieldValue(
                text = formattedDate,
                selection = TextRange(formattedDate.length)
            )
            onLaunchDateChange(formattedDate)
        },
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(
                color = colors.surface,
                shape = RoundedCornerShape(12.dp)
            )
            .border(
                width = 1.dp,
                color = colors.border,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        textStyle = LocalTextStyle.current.copy(
            color = colors.textPrimary,
            fontSize = SearchControlText.fontSize
        ),
        decorationBox = { innerTextField ->
            Box(contentAlignment = Alignment.CenterStart) {
                if (fieldValue.text.isEmpty()) {
                    Text(
                        text = stringResource(placeholder),
                        color = colors.textSecondary,
                        style = SearchControlText
                    )
                }
                innerTextField()
            }
        }
    )
}

/** Muestra una vista previa del campo de fecha de lanzamiento. */
@Composable
@Preview(showBackground = true)
fun LaunchDateFieldPreview() {
    DevicersAppTheme {
        LaunchDateField(
            launchDate = "12 / 04 / 2026",
            placeholder = R.string.launch_date_placeholder,
            onLaunchDateChange = {}
        )
    }
}
