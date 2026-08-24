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
    displaySmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Bold, fontSize = 38.88.sp, lineHeight = 45.36.sp),
    headlineSmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Bold, fontSize = 25.92.sp, lineHeight = 32.4.sp),
    titleLarge = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Bold, fontSize = 21.6.sp, lineHeight = 28.08.sp),
    titleMedium = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 19.44.sp, lineHeight = 25.92.sp),
    titleSmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 17.28.sp, lineHeight = 21.6.sp),
    bodyLarge = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Normal, fontSize = 17.28.sp, lineHeight = 23.76.sp),
    bodyMedium = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Normal, fontSize = 15.12.sp, lineHeight = 21.6.sp),
    bodySmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Normal, fontSize = 12.96.sp, lineHeight = 17.28.sp),
    labelLarge = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 15.12.sp, lineHeight = 21.6.sp),
    labelMedium = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.SemiBold, fontSize = 12.96.sp, lineHeight = 17.28.sp),
    labelSmall = TextStyle(fontFamily = DevicersFontFamily, fontWeight = FontWeight.Medium, fontSize = 11.88.sp, lineHeight = 15.12.sp)
)

/** Define el texto de contenido compartido por las tarjetas de reseña. */
val ReviewContentText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.28.sp,
    lineHeight = 23.76.sp
)

/** Define las etiquetas de los campos de autenticación. */
val AuthenticationFieldLabelText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.04.sp,
    lineHeight = 19.44.sp
)

/** Define los enlaces y textos auxiliares de autenticación. */
val AuthenticationSupportText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 14.04.sp,
    lineHeight = 19.44.sp
)

/** Define el título grande que encabeza una pantalla, como Search product o Activity. */
val ScreenTitleText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 25.92.sp,
    lineHeight = 32.4.sp
)

/** Define los títulos y controles del panel de búsqueda. */
val SearchHeadingText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 17.28.sp,
    lineHeight = 23.76.sp
)

/** Define etiquetas, campos y acciones secundarias del panel de búsqueda. */
val SearchControlText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.96.sp,
    lineHeight = 17.28.sp
)

/** Define el texto de acciones principales dentro de autenticación. */
val AuthenticationButtonText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 15.12.sp,
    lineHeight = 20.52.sp
)

/** Define el encabezado y descripción de las pantallas de autenticación. */
val AuthenticationTitleText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 24.84.sp,
    lineHeight = 30.24.sp
)

val AuthenticationDescriptionText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 16.2.sp,
    // El interlineado supera al tamaño para que la descripción de dos líneas no se encime.
    lineHeight = 22.68.sp
)

/**
 * Define el tamaño de los símbolos de calificación.
 *
 * Estos estilos no declaran familia a propósito: dibujan el carácter ★, que Plus Jakarta
 * Sans no incluye, así que deben resolverse con la tipografía del sistema.
 */
val RatingStarsText = TextStyle(fontSize = 12.96.sp, lineHeight = 17.28.sp)

/** Define las estrellas cuando encabezan un contenido principal, como el detalle de una reseña. */
val RatingStarsLargeText = TextStyle(fontSize = 19.44.sp, lineHeight = 25.92.sp)
val ReactionCountText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.ExtraBold,
    fontSize = 11.88.sp,
    lineHeight = 15.12.sp
)

/** Define las etiquetas que acompañan a los íconos de la barra de navegación. */
val NavigationLabelText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 10.26.sp,
    lineHeight = 12.96.sp
)

/** Define la metadata discreta de marca, categoría y antigüedad de las tarjetas. */
val CardMetadataText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 11.88.sp,
    lineHeight = 15.12.sp
)

/** Define el nombre de usuario y los valores numéricos destacados de una tarjeta. */
val CardHighlightText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 12.96.sp,
    lineHeight = 17.28.sp
)

/** Define los conteos de interacción ampliados para las reseñas del feed. */
val FeedReviewActionCountText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 16.848.sp,
    lineHeight = 22.464.sp
)

/** Define el texto de la acción visual de la barra superior. */
val TopBarActionText = TextStyle(
    fontFamily = DevicersFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 23.76.sp,
    lineHeight = 28.08.sp
)
