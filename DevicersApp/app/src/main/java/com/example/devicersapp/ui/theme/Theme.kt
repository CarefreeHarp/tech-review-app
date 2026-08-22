package com.example.devicersapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R


// ==========================
// Esquema de colores oscuro
// ==========================

private val DarkColorScheme = darkColorScheme(

    // Color principal de Devicers
    primary = DarkDevicersColors.primary,

    // Texto o contenido sobre el burgundy
    onPrimary = DarkDevicersColors.textOnPrimary,

    // Acento rosa suave
    secondary = DarkDevicersColors.primarySoft,

    onSecondary = DarkDevicersColors.textOnPrimary,

    // Fondo general
    background = DarkDevicersColors.background,

    // Texto principal sobre fondo oscuro
    onBackground = DarkDevicersColors.textPrimary,

    // Superficies como cards, barras, paneles
    surface = DarkDevicersColors.surface,

    // Texto sobre superficies
    onSurface = DarkDevicersColors.textPrimary,

    // Variante de superficie
    surfaceVariant = DarkDevicersColors.surfaceSecondary,

    // Texto secundario
    onSurfaceVariant = DarkDevicersColors.textSecondary,

    // Bordes
    outline = DarkDevicersColors.border,

    // Mensajes y estados de error
    error = DarkDevicersColors.error
)


// ==========================
// Esquema de colores claro
// ==========================

private val LightColorScheme = lightColorScheme(

    // Color principal de Devicers
    primary = LightDevicersColors.primary,

    // Texto o contenido sobre el burgundy
    onPrimary = LightDevicersColors.textOnPrimary,

    // Acento rosa suave
    secondary = LightDevicersColors.primarySoft,

    onSecondary = LightDevicersColors.textOnPrimary,

    // Fondo general
    background = LightDevicersColors.background,

    // Texto principal
    onBackground = LightDevicersColors.textPrimary,

    // Superficies como cards, filtros y paneles
    surface = LightDevicersColors.surface,

    // Texto sobre superficies
    onSurface = LightDevicersColors.textPrimary,

    // Variante de superficie
    surfaceVariant = LightDevicersColors.surfaceSecondary,

    // Texto secundario
    onSurfaceVariant = LightDevicersColors.textSecondary,

    // Bordes
    outline = LightDevicersColors.border,

    // Mensajes y estados de error
    error = LightDevicersColors.error
)

/** Representa un color de la paleta mostrado en las vistas previas del tema. */
private data class ThemeColorPreviewItem(
    @param:StringRes val labelResId: Int,
    val color: Color
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

    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val colors = if (darkTheme) DarkDevicersColors else LightDevicersColors

    CompositionLocalProvider(LocalDevicersColors provides colors) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}


/**
 * Preview del tema claro.
 */
@Composable
@Preview(
    name = "Devicers Light Mode",
    showBackground = true,
    heightDp = 620
)
fun DevicersLightThemePreview() {
    DevicersAppTheme(
        darkTheme = false
    ) {
        ThemeColorPalettePreview()
    }
}


/**
 * Preview del tema oscuro.
 */
@Composable
@Preview(
    name = "Devicers Dark Mode",
    showBackground = true,
    heightDp = 620
)
fun DevicersDarkThemePreview() {
    DevicersAppTheme(
        darkTheme = true
    ) {
        ThemeColorPalettePreview()
    }
}

/** Muestra todos los colores de la paleta activa con su nombre y muestra visual. */
@Composable
private fun ThemeColorPalettePreview(modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    val colorItems = listOf(
        ThemeColorPreviewItem(R.string.theme_color_primary, colors.primary),
        ThemeColorPreviewItem(R.string.theme_color_primary_soft, colors.primarySoft),
        ThemeColorPreviewItem(R.string.theme_color_primary_text, colors.primaryText),
        ThemeColorPreviewItem(R.string.theme_color_selection, colors.selection),
        ThemeColorPreviewItem(R.string.theme_color_text_on_selection, colors.textOnSelection),
        ThemeColorPreviewItem(R.string.theme_color_background, colors.background),
        ThemeColorPreviewItem(R.string.theme_color_surface, colors.surface),
        ThemeColorPreviewItem(R.string.theme_color_surface_secondary, colors.surfaceSecondary),
        ThemeColorPreviewItem(R.string.theme_color_text_primary, colors.textPrimary),
        ThemeColorPreviewItem(R.string.theme_color_text_secondary, colors.textSecondary),
        ThemeColorPreviewItem(R.string.theme_color_text_on_primary, colors.textOnPrimary),
        ThemeColorPreviewItem(R.string.theme_color_border, colors.border),
        ThemeColorPreviewItem(R.string.theme_color_rating, colors.rating),
        ThemeColorPreviewItem(R.string.theme_color_rating_track, colors.ratingTrack),
        ThemeColorPreviewItem(R.string.theme_color_avatar, colors.avatar),
        ThemeColorPreviewItem(R.string.theme_color_like_badge, colors.likeBadge),
        ThemeColorPreviewItem(R.string.theme_color_comment_badge, colors.commentBadge),
        ThemeColorPreviewItem(R.string.theme_color_follow_badge, colors.followBadge),
        ThemeColorPreviewItem(R.string.theme_color_error, colors.error)
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = colors.background
    ) {
        LazyColumn(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(colorItems) { item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(colors.surfaceSecondary, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(item.color, CircleShape)
                    )
                    Text(
                        text = stringResource(item.labelResId),
                        modifier = Modifier.padding(start = 12.dp),
                        color = item.color,
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}
