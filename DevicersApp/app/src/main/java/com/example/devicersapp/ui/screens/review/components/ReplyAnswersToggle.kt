package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra el control que revela u oculta una rama, alineado donde aparecería el avatar hijo.
 *
 * @param depth Nivel de la respuesta cuyo hilo controla.
 * @param passThroughLevels Niveles de ancestros cuya línea debe atravesar este control.
 * @param answersCount Cantidad de respuestas directas que contiene la rama.
 * @param areAnswersVisible Indica si la rama está desplegada.
 * @param onClick Acción solicitada al alternar la visibilidad de la rama.
 * @param modifier Modificador aplicado al control.
 */
@Composable
fun ReplyAnswersToggle(
    depth: Int,
    passThroughLevels: Set<Int> = emptySet(),
    answersCount: Int,
    areAnswersVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Box(modifier = modifier.fillMaxWidth().height(42.dp)) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val indent = ReplyThreadIndent.toPx()
            val avatarRadius = ReplyAvatarSize.toPx() / 2f
            val parentX = depth * indent + avatarRadius
            val childX = (depth + 1) * indent
            val centerY = size.height / 2f
            val cornerRadius = indent / 3f

            // Los ancestros abiertos conservan su línea incluso mientras se muestra este control.
            passThroughLevels.forEach { level ->
                val branchX = level * indent + avatarRadius
                drawLine(
                    color = colors.border,
                    start = androidx.compose.ui.geometry.Offset(branchX, 0f),
                    end = androidx.compose.ui.geometry.Offset(branchX, size.height),
                    strokeWidth = 1.5.dp.toPx()
                )
            }

            drawPath(
                path = Path().apply {
                    moveTo(parentX, 0f)
                    lineTo(parentX, centerY - cornerRadius)
                    quadraticTo(parentX, centerY, parentX + cornerRadius, centerY)
                    lineTo(childX, centerY)
                },
                color = colors.border,
                style = Stroke(width = 1.5.dp.toPx())
            )
        }

        Text(
            text = stringResource(
                if (areAnswersVisible) R.string.review_reply_hide_answers
                else R.string.review_reply_view_answers_count,
                answersCount
            ),
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = ReplyThreadIndent * (depth + 1) + 12.dp)
                .clickable(onClick = onClick),
            style = CardMetadataText,
            color = colors.primaryText
        )
    }
}

/** Muestra una vista previa del control de una rama de respuestas. */
@Composable
@Preview(showBackground = true)
fun ReplyAnswersTogglePreview() {
    DevicersAppTheme {
        ReplyAnswersToggle(
            depth = 0,
            passThroughLevels = emptySet(),
            answersCount = 3,
            areAnswersVisible = false,
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
