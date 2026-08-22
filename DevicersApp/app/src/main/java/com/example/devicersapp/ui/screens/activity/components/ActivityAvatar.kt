package com.example.devicersapp.ui.screens.activity.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ActivityType
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/**
 * Muestra el avatar del autor con la insignia que identifica el tipo de evento.
 *
 * Cada tipo de actividad tiene su propio color e ícono, de modo que el evento se reconozca
 * de un vistazo sin necesidad de leer el texto de la tarjeta.
 *
 * @param avatarResId Imagen de perfil del autor del evento.
 * @param type Tipo de evento que determina el color y el ícono de la insignia.
 * @param modifier Modificador aplicado al conjunto de avatar e insignia.
 * @param ringColor Color del anillo que separa la insignia de la superficie que hay detrás.
 */
@Composable
fun ActivityAvatar(
    avatarResId: Int,
    type: ActivityType,
    modifier: Modifier = Modifier,
    ringColor: Color? = null
) {
    val colors = LocalDevicersColors.current

    val badgeColor = when (type) {
        ActivityType.LIKE -> colors.likeBadge
        ActivityType.COMMENT -> colors.commentBadge
        ActivityType.FOLLOW -> colors.followBadge
    }
    val badgeIconResId = when (type) {
        ActivityType.LIKE -> R.drawable.like_icon
        ActivityType.COMMENT -> R.drawable.comment_icon
        ActivityType.FOLLOW -> R.drawable.profile_icon
    }
    val badgeDescriptionResId = when (type) {
        ActivityType.LIKE -> R.string.activity_badge_like
        ActivityType.COMMENT -> R.string.activity_badge_comment
        ActivityType.FOLLOW -> R.string.activity_badge_follow
    }

    Box(modifier = modifier.size(62.dp)) {
        ProfileAvatar(
            avatarResId = avatarResId,
            modifier = Modifier
                .size(52.dp)
                .align(Alignment.TopStart)
        )
        Box(
            modifier = Modifier
                .size(26.dp)
                .align(Alignment.BottomEnd)
                .background(badgeColor, CircleShape)
                // El anillo del color de la superficie separa la insignia del avatar.
                .border(2.dp, ringColor ?: colors.surface, CircleShape)
                .padding(5.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(badgeIconResId),
                contentDescription = stringResource(badgeDescriptionResId),
                tint = colors.textOnPrimary,
                modifier = Modifier.size(13.dp)
            )
        }
    }
}

/** Muestra una vista previa del avatar con insignia de reacción. */
@Composable
@Preview(showBackground = true)
fun ActivityAvatarPreview() {
    DevicersAppTheme {
        ActivityAvatar(
            avatarResId = R.drawable.profile_avatar_00,
            type = ActivityType.LIKE,
            modifier = Modifier.padding(16.dp)
        )
    }
}
