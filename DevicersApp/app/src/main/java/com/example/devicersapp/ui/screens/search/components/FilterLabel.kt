package com.example.devicersapp.ui.screens.search.components

import androidx.annotation.StringRes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.SearchControlText

/**
 * Muestra la etiqueta superior de un filtro de búsqueda.
 *
 * @param textResId Recurso de texto utilizado como etiqueta.
 * @param modifier Modificador aplicado a la etiqueta.
 */
@Composable
fun FilterLabel(@StringRes textResId: Int, modifier: Modifier = Modifier) {
    Text(
        text = stringResource(textResId),
        modifier = modifier,
        color = colorResource(R.color.text_secondary_light),
        style = SearchControlText
    )
}

/** Muestra una vista previa de una etiqueta de filtro. */
@Composable
@Preview(showBackground = true)
fun FilterLabelPreview() {
    FilterLabel(R.string.brand)
}
