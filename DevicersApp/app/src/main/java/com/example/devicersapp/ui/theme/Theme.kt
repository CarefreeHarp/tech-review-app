package com.example.devicersapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R


// ==========================
// Dark Color Scheme
// ==========================

private val DarkColorScheme = darkColorScheme(

    // Color principal de Devicers
    primary = DevicersYellow,

    // Texto o contenido sobre el amarillo
    onPrimary = TextPrimaryLight,

    // Amarillo secundario
    secondary = DevicersSoftYellow,

    onSecondary = TextPrimaryLight,

    // Fondo general
    background = BackgroundDark,

    // Texto principal sobre fondo oscuro
    onBackground = TextPrimaryDark,

    // Superficies como cards, barras, paneles
    surface = SurfaceDark,

    // Texto sobre superficies
    onSurface = TextPrimaryDark,

    // Variante de superficie
    surfaceVariant = SurfaceSecondaryDark,

    // Texto secundario
    onSurfaceVariant = TextSecondaryDark,

    // Bordes
    outline = BorderDark
)


// ==========================
// Light Color Scheme
// ==========================

private val LightColorScheme = lightColorScheme(

    // Color principal de Devicers
    primary = DevicersYellow,

    // Texto o contenido sobre el amarillo
    onPrimary = TextPrimaryLight,

    // Amarillo secundario
    secondary = DevicersSoftYellow,

    onSecondary = TextPrimaryLight,

    // Fondo general
    background = BackgroundLight,

    // Texto principal
    onBackground = TextPrimaryLight,

    // Superficies como cards, filtros y paneles
    surface = SurfaceLight,

    // Texto sobre superficies
    onSurface = TextPrimaryLight,

    // Variante de superficie
    surfaceVariant = SurfaceSecondaryLight,

    // Texto secundario
    onSurfaceVariant = TextSecondaryLight,

    // Bordes
    outline = BorderLight
)


/**
 * Aplica el tema visual de Devicers.
 *
 * El esquema de colores cambia automáticamente entre
 * modo claro y oscuro según la configuración del dispositivo.
 *
 * @param darkTheme Indica si se debe utilizar el modo oscuro.
 * @param content Contenido al que se aplicará el tema.
 */
@Composable
fun DevicersAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {

    val colorScheme =
        if (darkTheme) {
            DarkColorScheme
        } else {
            LightColorScheme
        }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}


/**
 * Preview del tema claro.
 */
@Composable
@Preview(
    name = "Devicers Light Mode",
    showBackground = true
)
fun DevicersLightThemePreview() {

    DevicersAppTheme(
        darkTheme = false
    ) {
        Text(
            text = stringResource(R.string.theme_preview_text)
        )
    }
}


/**
 * Preview del tema oscuro.
 */
@Composable
@Preview(
    name = "Devicers Dark Mode",
    showBackground = true
)
fun DevicersDarkThemePreview() {

    DevicersAppTheme(
        darkTheme = true
    ) {
        Text(
            text = stringResource(R.string.theme_preview_text)
        )
    }
}