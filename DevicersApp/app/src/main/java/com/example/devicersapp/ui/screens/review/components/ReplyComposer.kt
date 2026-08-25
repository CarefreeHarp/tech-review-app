package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra el compositor de respuesta controlado por la pantalla de detalle.
 *
 * @param value Texto actual de la respuesta.
 * @param onValueChange Acción que solicita actualizar el texto.
 * @param onSendClick Acción solicitada al enviar la respuesta.
 * @param modifier Modificador aplicado al compositor.
 */
@Composable
fun ReplyComposer(
    value: String,
    onValueChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .weight(1f)
                .height(48.dp)
                // La pastilla completa iguala la forma de los demás controles del diseño.
                .background(colors.surface, CircleShape)
                .border(1.dp, colors.border, CircleShape)
                .padding(horizontal = 18.dp),
            singleLine = true,
            textStyle = LocalTextStyle.current.copy(
                color = colors.textPrimary,
                fontSize = MaterialTheme.typography.bodyMedium.fontSize
            ),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (value.isEmpty()) {
                        Text(
                            text = stringResource(R.string.review_reply_placeholder),
                            style = MaterialTheme.typography.bodyMedium,
                            color = colors.textSecondary
                        )
                    }
                    innerTextField()
                }
            }
        )
        Box(
            modifier = Modifier
                .padding(start = 10.dp)
                .size(48.dp)
                .background(colors.primary, CircleShape)
                .clickable(onClick = onSendClick),
            contentAlignment = Alignment.Center
        ) {
            // El ícono de envío va en crema para contrastar con el burgundy del botón.
            Icon(
                painter = painterResource(R.drawable.send_icon),
                contentDescription = stringResource(R.string.review_send),
                tint = colors.textOnPrimary,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

/** Muestra una vista previa del compositor de respuesta. */
@Composable
@Preview(showBackground = true)
fun ReplyComposerPreview() {
    DevicersAppTheme {
        ReplyComposer(value = "", onValueChange = {}, onSendClick = {})
    }
}
