package com.example.devicersapp.ui.screens.notifications.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.annotation.StringRes
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.NotificationContent

/**
 * Agrupa las notificaciones que comparten el mismo periodo de tiempo.
 *
 * @param titleResId Recurso del encabezado del grupo.
 * @param notifications Notificaciones que se mostrarán debajo del encabezado.
 * @param followedNotificationIds Identificadores de los perfiles seguidos.
 * @param onFollow Solicita seguir el perfil de una notificación.
 * @param modifier Modificador aplicado al grupo.
 */
@Composable
fun NotificationSection(
    @StringRes titleResId: Int,
    notifications: List<NotificationContent>,
    followedNotificationIds: Set<String>,
    onFollow: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(titleResId),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = LocalDevicersColors.current.textSecondary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            notifications.forEach { notification ->
                NotificationCard(
                    notification = notification,
                    isFollowed = notification.id in followedNotificationIds,
                    onFollow = { onFollow(notification.id) }
                )
            }
        }
    }
}

/** Muestra una vista previa de una sección de notificaciones recientes. */
@Composable
@Preview(showBackground = true)
fun NotificationSectionPreview() {
    NotificationSection(
        titleResId = R.string.notifications_group_today,
        notifications = listOf(
            NotificationContent(
                id = "preview",
                avatarResId = R.drawable.profile_avatar_00,
                authorResId = R.string.notification_author_camila,
                actionResId = R.string.notification_action_liked_review,
                detailResId = R.string.notification_detail_headphones,
                time = System.currentTimeMillis() - 5 * 60_000L
            )
        ),
        followedNotificationIds = emptySet(),
        onFollow = {}
    )
}
