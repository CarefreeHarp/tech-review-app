package com.example.devicersapp.ui.screens.product.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

@Composable
fun ProductImageCard(
    modifier: Modifier = Modifier
) {

    val darkColor = colorResource(R.color.text_primary_light)
    val yellowColor = colorResource(R.color.primary_yellow)
    val cardColor = colorResource(R.color.surface_secondary_light)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(190.dp)
            .background(
                color = cardColor,
                shape = RoundedCornerShape(14.dp)
            ),
        contentAlignment = Alignment.Center
    ) {

        Canvas(
            modifier = Modifier.size(145.dp)
        ) {

            val stroke = 13.dp.toPx()

            // Banda superior de los auriculares
            drawArc(
                color = darkColor,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(
                    x = size.width * 0.17f,
                    y = size.height * 0.13f
                ),
                size = Size(
                    width = size.width * 0.66f,
                    height = size.height * 0.68f
                ),
                style = Stroke(
                    width = stroke,
                    cap = StrokeCap.Round
                )
            )

            // Auricular izquierdo
            drawRoundRect(
                color = darkColor,
                topLeft = Offset(
                    x = size.width * 0.14f,
                    y = size.height * 0.53f
                ),
                size = Size(
                    width = size.width * 0.23f,
                    height = size.height * 0.32f
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                    16.dp.toPx()
                )
            )

            // Auricular derecho
            drawRoundRect(
                color = darkColor,
                topLeft = Offset(
                    x = size.width * 0.63f,
                    y = size.height * 0.53f
                ),
                size = Size(
                    width = size.width * 0.23f,
                    height = size.height * 0.32f
                ),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                    16.dp.toPx()
                )
            )

            // Detalle amarillo izquierdo
            drawCircle(
                color = yellowColor,
                radius = 5.dp.toPx(),
                center = Offset(
                    size.width * 0.255f,
                    size.height * 0.69f
                )
            )

            // Detalle amarillo derecho
            drawCircle(
                color = yellowColor,
                radius = 5.dp.toPx(),
                center = Offset(
                    size.width * 0.745f,
                    size.height * 0.69f
                )
            )

            // Micrófono / extensión inferior
            drawLine(
                color = darkColor,
                start = Offset(
                    size.width * 0.74f,
                    size.height * 0.79f
                ),
                end = Offset(
                    size.width * 0.58f,
                    size.height * 0.87f
                ),
                strokeWidth = 7.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Punta amarilla
            drawLine(
                color = yellowColor,
                start = Offset(
                    size.width * 0.58f,
                    size.height * 0.87f
                ),
                end = Offset(
                    size.width * 0.49f,
                    size.height * 0.87f
                ),
                strokeWidth = 7.dp.toPx(),
                cap = StrokeCap.Round
            )
        }
    }
}