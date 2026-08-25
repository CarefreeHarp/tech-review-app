package com.example.devicersapp.ui.screens.activity.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.theme.DevicersAppTheme

/**
 * Muestra el encabezado temporal que separa grupos de eventos de actividad.
 *
 * @param titleResId Recurso del encabezado del grupo.
 * @param modifier Modificador aplicado al grupo.
 */
@Composable
fun ActivitySection(
    @StringRes titleResId: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(titleResId),
            modifier = Modifier.padding(start = 4.dp),
            style = SearchControlText,
            fontWeight = FontWeight.Bold,
            color = LocalDevicersColors.current.textSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}

/** Muestra una vista previa de una sección de actividad reciente. */
@Composable
@Preview(showBackground = true)
fun ActivitySectionPreview() {
    DevicersAppTheme {
        ActivitySection(
            titleResId = R.string.activity_group_today,
            modifier = Modifier.padding(16.dp)
        )
    }
}
