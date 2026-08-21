package com.example.devicersapp.ui.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.staticCompositionLocalOf

// ==========================
// Colores compartidos
// ==========================

/**
 * Agrupa los colores de la identidad editorial de Devicers que cambian según el tema activo.
 *
 * La identidad se apoya en una base crema y en un burgundy de acción. El dorado queda reservado
 * para las calificaciones y las insignias diferencian los eventos mostrados en Activity.
 */
data class DevicersColorPalette(
    val isDarkTheme: Boolean,
    val primary: Color,
    val primarySoft: Color,
    // Acento de texto sobre el fondo: el burgundy pleno solo es legible en el modo claro.
    val primaryText: Color,
    val selection: Color,
    val textOnSelection: Color,
    val background: Color,
    val surface: Color,
    val surfaceSecondary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textOnPrimary: Color,
    val border: Color,
    val rating: Color,
    val ratingTrack: Color,
    val avatar: Color,
    val likeBadge: Color,
    val commentBadge: Color,
    val followBadge: Color,
    val error: Color
)


// ==========================
// Modo claro
// ==========================

val LightDevicersColors = DevicersColorPalette(
    isDarkTheme = false,
    primary = Color(0xFF770002),
    primarySoft = Color(0xFFD98E91),
    primaryText = Color(0xFF770002),
    selection = Color(0xFF770002),
    textOnSelection = Color(0xFFF6EEE8),
    background = Color(0xFFF6EEE8),
    surface = Color(0xFFEDDECC),
    surfaceSecondary = Color(0xFFE6DDD2),
    textPrimary = Color(0xFF211D1B),
    textSecondary = Color(0xFF6F6661),
    textOnPrimary = Color(0xFFF6EEE8),
    border = Color(0xFFD5CAC0),
    rating = Color(0xFFB88934),
    // En modo claro las estrellas sin seleccionar se apoyan en la superficie crema.
    ratingTrack = Color(0xFFEDDECC),
    avatar = Color(0xFFCDBEB4),
    likeBadge = Color(0xFF770002),
    commentBadge = Color(0xFF3F6B4F),
    followBadge = Color(0xFF3B5E82),
    error = Color(0xFFB3261E)
)


// ==========================
// Modo oscuro
// ==========================

val DarkDevicersColors = DevicersColorPalette(
    isDarkTheme = true,
    primary = Color(0xFF770002),
    primarySoft = Color(0xFFD98E91),
    // Sobre el fondo casi negro el acento de texto sube al rosa suave para conservar contraste.
    primaryText = Color(0xFFD98E91),
    // En el modo oscuro la selección se resuelve con el rosa suave y texto oscuro encima.
    selection = Color(0xFFD98E91),
    textOnSelection = Color(0xFF211D1B),
    background = Color(0xFF1C1817),
    // La superficie elevada sostiene tarjetas y campos; la secundaria queda por debajo de ellos.
    surface = Color(0xFF32241C),
    surfaceSecondary = Color(0xFF271B15),
    textPrimary = Color(0xFFF6EEE8),
    textSecondary = Color(0xFFBCAFA9),
    textOnPrimary = Color(0xFFF6EEE8),
    border = Color(0xFF423B37),
    rating = Color(0xFFE1B55C),
    ratingTrack = Color(0xFF423B37),
    avatar = Color(0xFF4A423E),
    likeBadge = Color(0xFF770002),
    commentBadge = Color(0xFF7FA98B),
    followBadge = Color(0xFF8FB0CC),
    error = Color(0xFFFFB4AB)
)

/** Expone la paleta activa a todos los composables de la aplicación. */
val LocalDevicersColors = staticCompositionLocalOf { LightDevicersColors }
