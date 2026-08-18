package com.example.devicersapp.ui.screens.create_review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra el campo controlado usado para buscar productos antes de crear una reseña.
 *
 * @param value Texto actual de búsqueda.
 * @param onValueChange Acción que solicita actualizar la búsqueda.
 * @param modifier Modificador aplicado al campo.
 */
@Composable
fun CreateReviewSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(42.dp)
            .background(colors.surfaceSecondary, RoundedCornerShape(9.dp)),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            color = colors.textPrimary,
            fontSize = MaterialTheme.typography.bodySmall.fontSize
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.explore_icon),
                    contentDescription = null,
                    tint = colors.textSecondary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier.weight(1f),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.search_placeholder),
                            color = colors.textSecondary,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    innerTextField()
                }
            }
        }
    )
}

/** Muestra una vista previa del campo de búsqueda de creación de reseña. */
@Composable
@Preview(showBackground = true)
fun CreateReviewSearchBarPreview() {
    DevicersAppTheme { CreateReviewSearchBar(value = "", onValueChange = {}) }
}
