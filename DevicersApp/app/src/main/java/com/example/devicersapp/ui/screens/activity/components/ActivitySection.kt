package com.example.devicersapp.ui.screens.activity.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ActivityContent
import com.example.devicersapp.ui.models.ActivityType
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.theme.DevicersAppTheme

/**
 * Agrupa los eventos de actividad que comparten el mismo periodo de tiempo.
 *
 * @param titleResId Recurso del encabezado del grupo.
 * @param activities Eventos que se mostrarán debajo del encabezado.
 * @param followedActivityIds Identificadores de los perfiles ya seguidos.
 * @param onFollow Solicita seguir al autor de un evento.
 * @param modifier Modificador aplicado al grupo.
 * @param isHighlighted Indica si los eventos del grupo se muestran como tarjetas elevadas.
 */
@Composable
fun ActivitySection(
    @StringRes titleResId: Int,
    activities: List<ActivityContent>,
    followedActivityIds: Set<String>,
    onFollow: (String) -> Unit,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(titleResId),
            modifier = Modifier.padding(start = 4.dp),
            style = SearchControlText,
            fontWeight = FontWeight.Bold,
            color = LocalDevicersColors.current.textSecondary
        )
        Spacer(modifier = Modifier.height(12.dp))
        // Los grupos sin tarjeta necesitan menos separación porque ya respiran sobre el fondo.
        Column(verticalArrangement = Arrangement.spacedBy(if (isHighlighted) 12.dp else 4.dp)) {
            activities.forEach { activity ->
                ActivityCard(
                    activity = activity,
                    isFollowed = activity.id in followedActivityIds,
                    onFollow = { onFollow(activity.id) },
                    isHighlighted = isHighlighted
                )
            }
        }
    }
}

/** Muestra una vista previa de una sección de actividad reciente. */
@Composable
@Preview(showBackground = true)
fun ActivitySectionPreview() {
    DevicersAppTheme {
        ActivitySection(
            titleResId = R.string.activity_group_today,
            activities = listOf(
                ActivityContent(
                    id = "preview",
                    type = ActivityType.LIKE,
                    avatarResId = R.drawable.profile_avatar_00,
                    authorResId = R.string.activity_author_camila,
                    actionResId = R.string.activity_action_liked_review,
                    detailResId = R.string.activity_detail_headphones,
                    time = System.currentTimeMillis() - 5 * 60_000L
                )
            ),
            followedActivityIds = emptySet(),
            onFollow = {},
            modifier = Modifier.padding(16.dp),
            isHighlighted = true
        )
    }
}
