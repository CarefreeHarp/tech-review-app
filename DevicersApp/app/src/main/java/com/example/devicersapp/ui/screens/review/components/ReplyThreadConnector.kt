package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/** Ancho que ocupa cada nivel de anidación dentro del hilo de respuestas. */
val ReplyThreadIndent: Dp = 38.dp

/**
 * Dibuja las líneas que unen una respuesta con el comentario del que cuelga.
 *
 * El conector cubre toda la altura de la respuesta, incluido el espacio que la separa de la
 * siguiente, de modo que los trazos de respuestas consecutivas se encadenan y la línea se lea
 * como un recorrido continuo desde el avatar del comentario original hasta el de cada respuesta.
 *
 * Se dibujan tres clases de trazo:
 * - Las verticales de los niveles superiores que aún deben alcanzar a otra respuesta más abajo.
 * - El codo que entra al avatar de esta respuesta desde el nivel que la contiene.
 * - La vertical que sale del avatar de esta respuesta cuando alguien le contesta.
 *
 * @param depth Nivel de anidación de la respuesta.
 * @param passThroughLevels Niveles superiores cuya línea debe atravesar esta respuesta entera.
 * @param hasNestedReply Indica si de esta respuesta cuelga al menos una contestación.
 * @param avatarSize Diámetro del avatar, usado para alinear los trazos con su centro.
 * @param modifier Modificador aplicado al lienzo del conector.
 */
@Composable
fun ReplyThreadConnector(
    depth: Int,
    passThroughLevels: Set<Int>,
    hasNestedReply: Boolean,
    avatarSize: Dp,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Canvas(modifier = modifier) {
        val indent = ReplyThreadIndent.toPx()
        val avatarRadius = avatarSize.toPx() / 2f
        val cornerRadius = indent / 3f
        val stroke = Stroke(width = 1.5.dp.toPx())

        // Centro horizontal del avatar de cada nivel: por ahí baja la línea de ese hilo.
        fun branchX(level: Int) = level * indent + avatarRadius

        fun drawBranch(path: Path) = drawPath(path = path, color = colors.border, style = stroke)

        // Los niveles superiores solo siguen bajando si todavía les queda otra respuesta por alcanzar.
        passThroughLevels.forEach { level ->
            drawBranch(
                Path().apply {
                    moveTo(branchX(level), 0f)
                    lineTo(branchX(level), size.height)
                }
            )
        }

        // Codo que entra al avatar de esta respuesta desde el nivel inmediatamente superior.
        if (depth > 0) {
            val parentX = branchX(depth - 1)
            drawBranch(
                Path().apply {
                    moveTo(parentX, 0f)
                    lineTo(parentX, avatarRadius - cornerRadius)
                    quadraticTo(parentX, avatarRadius, parentX + cornerRadius, avatarRadius)
                    lineTo(depth * indent, avatarRadius)
                }
            )
        }

        // Vertical que sale del avatar de esta respuesta hacia quienes le contestan.
        if (hasNestedReply) {
            drawBranch(
                Path().apply {
                    moveTo(branchX(depth), avatarSize.toPx())
                    lineTo(branchX(depth), size.height)
                }
            )
        }
    }
}

/** Muestra una vista previa del conector de una respuesta anidada que además tiene contestación. */
@Composable
@Preview(showBackground = true)
fun ReplyThreadConnectorPreview() {
    DevicersAppTheme {
        ReplyThreadConnector(
            depth = 1,
            passThroughLevels = setOf(0),
            hasNestedReply = true,
            avatarSize = 34.dp,
            modifier = Modifier
                .padding(16.dp)
                .width(120.dp)
                .height(110.dp)
        )
    }
}
