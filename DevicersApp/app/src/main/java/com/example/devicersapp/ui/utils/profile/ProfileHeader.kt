package com.example.devicersapp.ui.utils.profile

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.theme.DevicersAppTheme

/**
 * Muestra el avatar, el nombre de usuario, las métricas, la acción principal y la biografía.
 *
 * El orden sigue al diseño editorial: primero la identidad, luego las métricas centradas,
 * después la acción y por último la biografía, separada del botón.
 *
 * @param profile Datos visibles del perfil, incluido el recurso de su avatar.
 * @param actionLabelResId Texto de la acción principal, que cambia según de quién sea el perfil.
 * @param modifier Modificador aplicado al contenedor del perfil.
 * @param showEditBadge Indica si la foto ofrece la insignia para cambiarla, en el perfil propio.
 * @param showStats Indica si se muestran las métricas de reseñas, seguidores y seguidos.
 * @param showAction Indica si se muestra el botón de acción principal.
 * @param showBiography Indica si se muestra la biografía del perfil.
 * @param onActionClick Acción solicitada al pulsar el botón principal.
 * @param onEditAvatarClick Acción solicitada al pulsar la insignia de edición de la foto.
 */
@Composable
fun ProfileHeader(
    profile: ProfileContent,
    @StringRes actionLabelResId: Int,
    modifier: Modifier = Modifier,
    showEditBadge: Boolean = false,
    showStats: Boolean = true,
    showAction: Boolean = true,
    showBiography: Boolean = true,
    onActionClick: () -> Unit = {},
    onEditAvatarClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            ProfileAvatar(
                avatarResId = profile.avatarResId,
                modifier = Modifier.size(84.dp)
            )
            // Solo el perfil propio ofrece cambiar la foto, con la insignia sobre su borde.
            if (showEditBadge) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .align(Alignment.BottomEnd)
                        .background(colors.primary, CircleShape)
                        .clickable { onEditAvatarClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.edit_icon),
                        contentDescription = stringResource(R.string.profile_edit_avatar),
                        tint = colors.textOnPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(profile.handleResId),
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary
        )

        // Las secciones que se concentran en el contenido guardado prescinden de las métricas.
        if (showStats) {
            Spacer(modifier = Modifier.height(20.dp))
            // Métricas repartidas por igual para que queden centradas bajo el nombre.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                profile.stats.forEach { stat -> ProfileStat(stat) }
            }
        }

        if (showAction) {
            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onActionClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colors.primary,
                    contentColor = colors.textOnPrimary
                )
            ) {
                Text(
                    text = stringResource(actionLabelResId),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }

        if (showBiography) {
            Spacer(modifier = Modifier.height(if (showAction) 18.dp else 22.dp))
            Text(
                text = stringResource(profile.biographyResId),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
        }
    }
}

/** Muestra una vista previa del encabezado del perfil propio, con su insignia de edición. */
@Composable
@Preview(showBackground = true, heightDp = 460)
fun ProfileHeaderPreview() {
    DevicersAppTheme {
        ProfileHeader(
            profile = SampleProfileContent,
            actionLabelResId = R.string.profile_edit,
            modifier = Modifier.padding(20.dp),
            showEditBadge = true
        )
    }
}

/** Contiene datos de ejemplo para las vistas previas del perfil. */
private val SampleProfileContent = ProfileContent(
    avatarResId = R.drawable.profile_avatar_00,
    handleResId = R.string.profile_handle,
    biographyResId = R.string.profile_biography,
    stats = listOf(
        ProfileStatContent(R.string.profile_reviews_count, R.string.profile_reviews),
        ProfileStatContent(R.string.profile_followers_count, R.string.profile_followers),
        ProfileStatContent(R.string.profile_following_count, R.string.profile_following)
    )
)
