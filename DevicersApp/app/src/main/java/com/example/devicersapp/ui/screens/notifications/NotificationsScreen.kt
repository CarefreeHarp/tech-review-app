package com.example.devicersapp.ui.screens.notifications

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalNotificationsProvider
import com.example.devicersapp.ui.screens.notifications.components.NotificationSection

/** Configura la estructura principal de la pantalla de notificaciones y su navegación inferior. */
@Composable
fun NotificationsScreen(modifier: Modifier = Modifier) {
    // Estado elevado que determina qué tarjetas deben mostrar la acción "Siguiendo".
    var followedNotificationIds by remember { mutableStateOf(emptySet<String>()) }

    NotificationsScreenContent(
        followedNotificationIds = followedNotificationIds,
        onFollow = { notificationId -> followedNotificationIds = followedNotificationIds + notificationId },
        modifier = modifier.fillMaxSize().background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla el encabezado y la lista agrupada de notificaciones.
 *
 * @param followedNotificationIds Identificadores de notificaciones cuyos autores ya se siguen.
 * @param onFollow Acción solicitada al seleccionar seguir.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun NotificationsScreenContent(
    followedNotificationIds: Set<String>,
    onFollow: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Una sola referencia de tiempo mantiene coherentes los valores relativos de toda la lista.
    val notificationGroups = LocalNotificationsProvider.notificationGroups(
        currentTimeMillis = System.currentTimeMillis()
    )

    LazyColumn(
        modifier = modifier.background(LocalDevicersColors.current.background),
        contentPadding = PaddingValues(
            start = 18.dp,
            end = 18.dp,
            top = 0.dp,
            bottom = 20.dp
        ),
        verticalArrangement = Arrangement.spacedBy(22.dp)
    ) {
        item {
            Column {
                Image(
                    painter = painterResource(R.drawable.logo_claro),
                    contentDescription = stringResource(R.string.devicers_logo_description),
                    modifier = Modifier
                        .size(width = 172.dp, height = 46.dp)
                        .align(Alignment.Start)
                )
                Text(
                    text = stringResource(R.string.notifications),
                    modifier = Modifier.padding(top = 20.dp),
                    style = MaterialTheme.typography.headlineSmall,
                    color = LocalDevicersColors.current.textPrimary
                )
                Text(
                    text = stringResource(R.string.notifications_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalDevicersColors.current.textSecondary
                )
            }
        }
        items(notificationGroups, key = { it.first }) { (title, notifications) ->
            NotificationSection(
                title = title,
                notifications = notifications,
                followedNotificationIds = followedNotificationIds,
                onFollow = onFollow
            )
        }
        item {
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

/** Muestra una vista previa de la pantalla completa de notificaciones. */
@Composable
@Preview(showBackground = true, heightDp = 720)
fun NotificationsScreenPreview() {
    NotificationsScreen()
}
