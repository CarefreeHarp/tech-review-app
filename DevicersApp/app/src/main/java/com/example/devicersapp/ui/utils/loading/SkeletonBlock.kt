package com.example.devicersapp.ui.utils.loading

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Dibuja una silueta estática de contenido pendiente con la paleta activa.
 *
 * Las pantallas definen su propia composición; este bloque solo comparte el dibujo básico.
 * @param modifier Tamaño y posición de la silueta, sin interacción ni datos de pantalla.
 * @param shape Recorte del bloque, circular para avatares o redondeado para texto e imágenes.
 */
@Composable
fun SkeletonBlock(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Box(modifier = modifier.background(LocalDevicersColors.current.border, shape))
}

/** Permite revisar el contraste de las siluetas en ambos temas. */
@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SkeletonBlockPreview() {
    DevicersAppTheme {
        SkeletonBlock(Modifier.width(180.dp).height(16.dp))
    }
}
