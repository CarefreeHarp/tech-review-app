package com.example.devicersapp.ui.screens.user_preferences.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.navigation.TitleTopBar

/** Presenta el título, el regreso y la división entre la cabecera y el contenido futuro. */
@Composable
fun UserPreferencesHeader(@StringRes titleResId: Int, onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        TitleTopBar(titleResId = titleResId, onBackClick = onBackClick)
        HorizontalDivider(modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp),
            thickness = 3.dp, color = LocalDevicersColors.current.border)
    }
}

/** Previsualiza la cabecera con la barra divisoria del diseño compartido. */
@Preview(showBackground = true)
@Composable
fun UserPreferencesHeaderPreview() {
    DevicersAppTheme { UserPreferencesHeader(R.string.user_preferences_title, {}) }
}
