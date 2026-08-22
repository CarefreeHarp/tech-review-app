package com.example.devicersapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R

/**
 * Reúne los cinco pesos de Plus Jakarta Sans, la tipografía de la identidad de Devicers.
 *
 * Al declararlos en una sola familia, cada `FontWeight` de la escala resuelve automáticamente
 * el archivo correcto, sin que las pantallas tengan que elegir un peso concreto.
 */
val DevicersFontFamily = FontFamily(
    Font(R.font.plus_jakarta_sans_regular, FontWeight.Normal),
    Font(R.font.plus_jakarta_sans_medium, FontWeight.Medium),
    Font(R.font.plus_jakarta_sans_semibold, FontWeight.SemiBold),
    Font(R.font.plus_jakarta_sans_bold, FontWeight.Bold),
    Font(R.font.plus_jakarta_sans_extrabold, FontWeight.ExtraBold)
)

/** Define la escala tipográfica predeterminada y consistente de Devicers. */
val Typography = Typography(
    displaySmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Bold, fontSize = 43.2.sp, lineHeight = 50.4.sp),
    headlineSmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.8.sp, lineHeight = 36.sp),
    titleLarge = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 31.2.sp),
    titleMedium = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 21.6.sp, lineHeight = 28.8.sp),
    titleSmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 19.2.sp, lineHeight = 24.sp),
    bodyLarge = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Normal, fontSize = 19.2.sp, lineHeight = 26.4.sp),
    bodyMedium = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Normal, fontSize = 16.8.sp, lineHeight = 24.sp),
    bodySmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.4.sp, lineHeight = 19.2.sp),
    labelLarge = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 16.8.sp, lineHeight = 24.sp),
    labelMedium = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 14.4.sp, lineHeight = 19.2.sp),
    labelSmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Medium, fontSize = 13.2.sp, lineHeight = 16.8.sp)
)

/** Define el texto de contenido compartido por las tarjetas de reseña. */
val ReviewContentText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 19.2.sp,
    lineHeight = 26.4.sp
)

/** Define las etiquetas de los campos de autenticación. */
val AuthenticationFieldLabelText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 15.6.sp,
    lineHeight = 21.6.sp
)

/** Define los enlaces y textos auxiliares de autenticación. */
val AuthenticationSupportText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 15.6.sp,
    lineHeight = 21.6.sp
)

/** Define el título grande que encabeza una pantalla, como Search product o Activity. */
val ScreenTitleText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 28.8.sp,
    lineHeight = 36.sp
)

/** Define los títulos y controles del panel de búsqueda. */
val SearchHeadingText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 19.2.sp,
    lineHeight = 26.4.sp
)

/** Define etiquetas, campos y acciones secundarias del panel de búsqueda. */
val SearchControlText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.4.sp,
    lineHeight = 19.2.sp
)

/** Define el texto de acciones principales dentro de autenticación. */
val AuthenticationButtonText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.8.sp,
    lineHeight = 22.8.sp
)

/** Define el encabezado y descripción de las pantallas de autenticación. */
val AuthenticationTitleText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 27.6.sp,
    lineHeight = 33.6.sp
)

val AuthenticationDescriptionText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    // El interlineado supera al tamaño para que la descripción de dos líneas no se encime.
    lineHeight = 25.2.sp
)

/**
 * Define el tamaño de los símbolos de calificación.
 *
 * Estos estilos no declaran familia a propósito: dibujan el carácter ★, que Plus Jakarta
 * Sans no incluye, así que deben resolverse con la tipografía del sistema.
 */
val RatingStarsText = TextStyle(fontSize = 14.4.sp, lineHeight = 19.2.sp)

/** Define las estrellas cuando encabezan un contenido principal, como el detalle de una reseña. */
val RatingStarsLargeText = TextStyle(fontSize = 21.6.sp, lineHeight = 28.8.sp)
val ReactionCountText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 13.2.sp,
    lineHeight = 16.8.sp
)

/** Define las etiquetas que acompañan a los íconos de la barra de navegación. */
val NavigationLabelText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 11.4.sp,
    lineHeight = 14.4.sp
)

/** Define la metadata discreta de marca, categoría y antigüedad de las tarjetas. */
val CardMetadataText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 13.2.sp,
    lineHeight = 16.8.sp
)

/** Define el nombre de usuario y los valores numéricos destacados de una tarjeta. */
val CardHighlightText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 14.4.sp,
    lineHeight = 19.2.sp
)

/** Define el texto de la acción visual de la barra superior. */
val TopBarActionText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 26.4.sp,
    lineHeight = 31.2.sp
)
