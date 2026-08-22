package com.example.devicersapp.ui.screens.activity.components

import android.content.res.Resources
import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ActivityContent
import com.example.devicersapp.ui.models.ActivityType
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.SearchControlText

private const val MINUTE_IN_MILLIS = 60_000L
private const val HOUR_IN_MILLIS = 60 * MINUTE_IN_MILLIS
private const val DAY_IN_MILLIS = 24 * HOUR_IN_MILLIS

/**
 * Muestra un evento de actividad con su insignia, su descripción y una acción opcional de seguimiento.
 *
 * @param activity Información visible del evento.
 * @param isFollowed Indica si el autor del evento ya ha sido seguido.
 * @param onFollow Solicita cambiar el estado de seguimiento del autor.
 * @param modifier Modificador aplicado a la tarjeta.
 * @param isHighlighted Indica si el evento se dibuja como tarjeta elevada en vez de sobre el fondo.
 */
@Composable
fun ActivityCard(
    activity: ActivityContent,
    isFollowed: Boolean,
    onFollow: () -> Unit,
    modifier: Modifier = Modifier,
    isHighlighted: Boolean = false
) {
    val colors = LocalDevicersColors.current
    // El tiempo se deriva al renderizar para evitar guardar textos como "Hace 5 m" en los datos.
    val elapsedTime = formatElapsedTime(LocalContext.current.resources, activity.time)

    // Solo la actividad más reciente se eleva sobre una tarjeta; la anterior descansa en el fondo.
    val containerModifier = if (isHighlighted) {
        Modifier
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp))
            .background(colors.surface, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 18.dp)
    } else {
        Modifier.padding(horizontal = 4.dp, vertical = 10.dp)
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .then(containerModifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ActivityAvatar(
            avatarResId = activity.avatarResId,
            type = activity.type,
            ringColor = if (isHighlighted) colors.surface else colors.background
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(activity.authorResId),
                style = MaterialTheme.typography.titleSmall,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = stringResource(activity.actionResId),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textPrimary
            )
            Spacer(modifier = Modifier.height(5.dp))
            // El detalle y la antigüedad comparten una sola línea de metadata.
            Text(
                text = activity.detailResId
                    ?.let { stringResource(R.string.activity_detail_format, stringResource(it).trim(), elapsedTime) }
                    ?: stringResource(R.string.activity_time_ago, elapsedTime),
                style = MaterialTheme.typography.bodySmall,
                color = colors.textSecondary
            )
        }

        if (activity.showFollowAction) {
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = onFollow,
                modifier = Modifier.height(42.dp),
                shape = RoundedCornerShape(percent = 50),
                colors = ButtonDefaults.buttonColors(
                    // Ya seguido, el botón invierte sus colores: acento sobre fondo crema.
                    containerColor = if (isFollowed) colors.background else colors.primary,
                    contentColor = if (isFollowed) colors.primaryText else colors.textOnPrimary
                ),
                border = if (isFollowed) BorderStroke(1.dp, colors.primaryText) else null,
                contentPadding = PaddingValues(horizontal = 20.dp)
            ) {
                Text(
                    text = stringResource(
                        if (isFollowed) R.string.activity_following else R.string.activity_follow
                    ),
                    style = SearchControlText,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

/**
 * Calcula el tiempo transcurrido desde la llegada de un evento en minutos, horas o días.
 *
 * @param resources Recursos usados para resolver la unidad de tiempo.
 * @param time Instante de llegada del evento en milisegundos desde época Unix.
 * @return Texto compacto con la unidad más apropiada, como `5 m`, `1 h` o `3 d`.
 */
private fun formatElapsedTime(resources: Resources, time: Long): String {
    val elapsedMillis = (System.currentTimeMillis() - time).coerceAtLeast(0L)
    return when {
        elapsedMillis < HOUR_IN_MILLIS -> resources.getString(
            R.string.activity_elapsed_minutes,
            (elapsedMillis / MINUTE_IN_MILLIS).coerceAtLeast(1L)
        )
        elapsedMillis < DAY_IN_MILLIS -> resources.getString(
            R.string.activity_elapsed_hours,
            elapsedMillis / HOUR_IN_MILLIS
        )
        else -> resources.getString(
            R.string.activity_elapsed_days,
            elapsedMillis / DAY_IN_MILLIS
        )
    }
}

/** Muestra una vista previa de un evento que permite seguir a su autor. */
@Composable
@Preview(showBackground = true)
fun ActivityCardPreview() {
    DevicersAppTheme {
        ActivityCard(
            activity = ActivityContent(
                id = "preview",
                type = ActivityType.FOLLOW,
                avatarResId = R.drawable.profile_avatar_00,
                authorResId = R.string.activity_author_lina,
                actionResId = R.string.activity_action_followed,
                detailResId = R.string.activity_detail_profile,
                time = System.currentTimeMillis() - DAY_IN_MILLIS,
                showFollowAction = true
            ),
            isFollowed = false,
            onFollow = {},
            modifier = Modifier.padding(16.dp),
            isHighlighted = true
        )
    }
}
