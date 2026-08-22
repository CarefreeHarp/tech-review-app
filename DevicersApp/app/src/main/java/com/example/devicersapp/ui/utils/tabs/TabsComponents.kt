package com.example.devicersapp.ui.utils.tabs

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchHeadingText

/**
 * Muestra las dos secciones entre las que alterna una pantalla, subrayando la activa.
 *
 * La fila no conserva estado: la pantalla propietaria decide cuál sección está activa
 * y reacciona a las acciones que emite cada pestaña.
 *
 * @param startLabelResId Recurso de texto de la sección izquierda.
 * @param endLabelResId Recurso de texto de la sección derecha.
 * @param isStartSelected Indica si la sección activa es la de la izquierda.
 * @param onStartClick Acción que solicita mostrar la sección izquierda.
 * @param onEndClick Acción que solicita mostrar la sección derecha.
 * @param modifier Modificador aplicado a la fila de pestañas.
 * @param selectedColor Color del texto de la sección activa, elegido por la pantalla.
 */
@Composable
fun SectionTabsRow(
    @StringRes startLabelResId: Int,
    @StringRes endLabelResId: Int,
    isStartSelected: Boolean,
    onStartClick: () -> Unit,
    onEndClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = LocalDevicersColors.current.textPrimary
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        SectionTab(
            labelResId = startLabelResId,
            isSelected = isStartSelected,
            onClick = onStartClick,
            selectedColor = selectedColor
        )
        SectionTab(
            labelResId = endLabelResId,
            isSelected = !isStartSelected,
            onClick = onEndClick,
            selectedColor = selectedColor
        )
    }
}

/**
 * Muestra una sección individual y la subraya cuando está activa.
 *
 * @param labelResId Recurso de texto de la sección.
 * @param isSelected Indica si la sección está activa.
 * @param onClick Acción solicitada al elegir la sección.
 * @param modifier Modificador aplicado a la pestaña.
 * @param selectedColor Color del texto cuando la sección está activa; la pantalla decide si la
 * destaca con el acento, como ocurre cuando es la única sección disponible.
 */
@Composable
fun SectionTab(
    @StringRes labelResId: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = LocalDevicersColors.current.textPrimary
) {
    val colors = LocalDevicersColors.current
    Column(
        modifier = modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(labelResId),
            color = if (isSelected) selectedColor else colors.textSecondary,
            style = SearchHeadingText
        )
        Spacer(modifier = Modifier.height(6.dp))
        // El subrayado de acento solo acompaña a la sección activa.
        if (isSelected) {
            Spacer(
                modifier = Modifier
                    .width(44.dp)
                    .height(3.dp)
                    .background(colors.primary, RoundedCornerShape(2.dp))
            )
        }
    }
}

/** Muestra una vista previa de la fila de secciones. */
@Composable
@Preview(showBackground = true)
fun SectionTabsRowPreview() {
    DevicersAppTheme {
        SectionTabsRow(
            startLabelResId = R.string.home_tab_for_you,
            endLabelResId = R.string.home_tab_following,
            isStartSelected = true,
            onStartClick = {},
            onEndClick = {}
        )
    }
}

/** Muestra una vista previa de una sección activa. */
@Composable
@Preview(showBackground = true)
fun SectionTabPreview() {
    DevicersAppTheme {
        SectionTab(
            labelResId = R.string.home_tab_for_you,
            isSelected = true,
            onClick = {}
        )
    }
}
