package com.example.devicersapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.devicersapp.navigation.AppNavigation
import com.example.devicersapp.ui.theme.DevicersAppTheme

/**
 * Compone la aplicación Devicers con su tema global.
 *
 * La navegación principal se encuentra centralizada en AppNavigation.
 *
 * @param modifier Modificador aplicado al contenedor raíz.
 */
@Composable
fun DevicersApp(
    modifier: Modifier = Modifier
) {
    DevicersAppTheme {
        AppNavigation(
            modifier = modifier
        )
    }
}