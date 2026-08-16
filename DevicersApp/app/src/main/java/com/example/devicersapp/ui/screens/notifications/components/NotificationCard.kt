package com.example.devicersapp.ui.screens.notifications.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.NotificationContent

private const val MINUTE_IN_MILLIS = 60_000L
private const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
private const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS

/**
 * Muestra una notificación con el avatar, el detalle de actividad y una acción opcional de seguimiento.
 *
 * @param notification Información visible de la notificación.
 * @param isFollowed Indica si el usuario de la notificación ya ha sido seguido.
 * @param onFollow Solicita cambiar el estado de seguimiento de la notificación.
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun NotificationCard(
    notification: NotificationContent,
    isFollowed: Boolean,
    onFollow: () -> Unit,
    modifier: Modifier = Modifier
) {
    // El tiempo se deriva al renderizar para evitar guardar textos como "Hace 5 m" en los datos.
    val elapsedTime = formatElapsedTime(notification.time)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(colorResource(R.color.surface_light), RoundedCornerShape(14.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(notification.avatarResId),
            contentDescription = null,
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notification.author,
                style = MaterialTheme.typography.labelLarge,
                color = colorResource(R.color.text_primary_light)
            )
            Text(
                text = notification.action,
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_primary_light)
            )
            if (notification.detail.isNotBlank()) {
                Text(
                    text = notification.detail,
                    style = MaterialTheme.typography.bodySmall,
                    color = colorResource(R.color.text_secondary_light)
                )
            }
            Text(
                text = stringResource(R.string.notifications_time_ago, elapsedTime),
                style = MaterialTheme.typography.bodySmall,
                color = colorResource(R.color.text_secondary_light)
            )
        }

        if (notification.showFollowAction) {
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onFollow,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.background_light),
                    contentColor = colorResource(R.color.text_primary_light)
                ),
                contentPadding = ButtonDefaults.ContentPadding,
                modifier = Modifier
            ) {
                Text(
                    text = stringResource(
                        if (isFollowed) R.string.notifications_following else R.string.notifications_follow
                    ),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

/**
 * Calcula el tiempo transcurrido desde la llegada de una notificación en minutos, horas o días.
 *
 * @param time Instante de llegada de la notificación en milisegundos desde época Unix.
 * @return Texto compacto con la unidad más apropiada, como `5 m`, `1 h` o `3 d`.
 */
private fun formatElapsedTime(time: Long): String {
    val elapsedMillis = (System.currentTimeMillis() - time).coerceAtLeast(0L)
    return when {
        elapsedMillis < HOUR_IN_MILLIS -> "${(elapsedMillis / MINUTE_IN_MILLIS).coerceAtLeast(1L)} m"
        elapsedMillis < DAY_IN_MILLIS -> "${elapsedMillis / HOUR_IN_MILLIS} h"
        else -> "${elapsedMillis / DAY_IN_MILLIS} d"
    }
}

/** Muestra una vista previa de una notificación que permite seguir al usuario. */
@Composable
@Preview(showBackground = true)
fun NotificationCardPreview() {
    NotificationCard(
        notification = NotificationContent(
            id = "preview",
            avatarResId = R.drawable.profile_avatar_00,
            author = "@usuario.reviews",
            action = "Comenzó a seguirte",
            detail = "",
            time = System.currentTimeMillis() - DAY_IN_MILLIS,
            showFollowAction = true
        ),
        isFollowed = false,
        onFollow = {}
    )
}
