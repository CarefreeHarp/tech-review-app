package com.example.devicersapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/** Define la escala tipográfica predeterminada y consistente de Devicers. */
val Typography = Typography(
    displaySmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 43.2.sp, lineHeight = 50.4.sp),
    headlineSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 28.8.sp, lineHeight = 36.sp),
    titleLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 31.2.sp),
    titleMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 21.6.sp, lineHeight = 28.8.sp),
    titleSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 19.2.sp, lineHeight = 24.sp),
    bodyLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 19.2.sp, lineHeight = 26.4.sp),
    bodyMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 16.8.sp, lineHeight = 24.sp),
    bodySmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Normal, fontSize = 14.4.sp, lineHeight = 19.2.sp),
    labelLarge = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 16.8.sp, lineHeight = 24.sp),
    labelMedium = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.SemiBold, fontSize = 14.4.sp, lineHeight = 19.2.sp),
    labelSmall = TextStyle(fontFamily = FontFamily.Default, fontWeight = FontWeight.Medium, fontSize = 13.2.sp, lineHeight = 16.8.sp)
)

/** Define el texto de contenido compartido por las tarjetas de reseña. */
val ReviewContentText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 19.2.sp,
    lineHeight = 26.4.sp
)

/** Define las etiquetas de los campos de autenticación. */
val AuthenticationFieldLabelText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 15.6.sp,
    lineHeight = 21.6.sp
)

/** Define los enlaces y textos auxiliares de autenticación. */
val AuthenticationSupportText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 15.6.sp,
    lineHeight = 21.6.sp
)

/** Define el título principal de la pantalla de búsqueda. */
val SearchScreenTitleText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 28.8.sp,
    lineHeight = 36.sp
)

/** Define los títulos y controles del panel de búsqueda. */
val SearchHeadingText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 19.2.sp,
    lineHeight = 26.4.sp
)

/** Define etiquetas, campos y acciones secundarias del panel de búsqueda. */
val SearchControlText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Medium,
    fontSize = 14.4.sp,
    lineHeight = 19.2.sp
)

/** Define el texto de acciones principales dentro de autenticación. */
val AuthenticationButtonText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 16.8.sp,
    lineHeight = 22.8.sp
)

/** Define el encabezado y descripción de las pantallas de autenticación. */
val AuthenticationTitleText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Bold,
    fontSize = 27.6.sp,
    lineHeight = 33.6.sp
)

val AuthenticationDescriptionText = TextStyle(
    fontFamily = FontFamily.Default,
    fontWeight = FontWeight.Normal,
    fontSize = 18.sp,
    lineHeight = 18.sp
)

/** Define el tamaño de los símbolos de calificación y conteos de reacción. */
val RatingStarsText = TextStyle(fontSize = 14.4.sp, lineHeight = 19.2.sp)
val ReactionCountText = TextStyle(fontWeight = FontWeight.Black, fontSize = 13.2.sp, lineHeight = 16.8.sp)

/** Define el texto de la acción visual de la barra superior. */
val TopBarActionText = TextStyle(fontWeight = FontWeight.Bold, fontSize = 26.4.sp, lineHeight = 31.2.sp)
