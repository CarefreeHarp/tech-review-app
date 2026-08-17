package com.example.devicersapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.staticCompositionLocalOf

// ==========================
// Colores compartidos
// ==========================

/** Agrupa los colores de Devicers que cambian según el tema activo. */
data class DevicersColorPalette(
    val primaryYellow: Color,
    val softYellow: Color,
    val background: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val border: Color
)


// ==========================
// Light Mode
// ==========================

val LightDevicersColors = DevicersColorPalette(
    primaryYellow = Color(0xFFE8E84F),
    softYellow = Color(0xFFF3F3A9),
    background = Color(0xFFF4F4ED),
    surface = Color(0xFFD6D6C5),
    surfaceSecondary = Color(0xFFE7E7DB),
    textPrimary = Color(0xFF1C1C18),
    textSecondary = Color(0xFF595950),
    border = Color(0xFFBDBDAC)
)


// ==========================
// Dark Mode
// ==========================

val DarkDevicersColors = DevicersColorPalette(
    primaryYellow = Color(0xFFE8E84F),
    softYellow = Color(0xFFF3F3A9),
    background = Color(0xFF11110F),
    surface = Color(0xFF24241F),
    surfaceSecondary = Color(0xFF30302A),
    textPrimary = Color(0xFFF7F7F0),
    textSecondary = Color(0xFFB8B8AA),
    border = Color(0xFF44443B)
)

/** Expone la paleta activa a todos los composables de la aplicación. */
val LocalDevicersColors = staticCompositionLocalOf { LightDevicersColors }
