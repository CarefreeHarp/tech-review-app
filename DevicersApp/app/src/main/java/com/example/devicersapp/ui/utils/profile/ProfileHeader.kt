package com.example.devicersapp.ui.utils.profile



import com.example.devicersapp.ui.utils.profile.asUserHandle
import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.theme.DevicersAppTheme

/**
 * Muestra el avatar, el nombre de usuario, las métricas, la acción principal y la biografía.
 *
 * El orden sigue al diseño editorial: primero la identidad, luego las métricas centradas,
 * después la acción y por último la biografía, separada del botón.
 *
 * @param profile Datos visibles del perfil, incluido el recurso de su avatar.
 * @param actionLabelResId Texto de la acción principal, que cambia según de quién sea el perfil.
 * @param displayName Nombre que se muestra en la cabecera cuando corresponde al perfil actual.
 * @param email Correo de la cuenta autenticada que se muestra debajo de la foto cuando está disponible.
 * @param profileImageUrl URL remota de la foto del perfil propio, si existe.
 * @param modifier Modificador aplicado al contenedor del perfil.
 * @param showDisplayName Indica si debe mostrarse el nombre debajo de la foto.
 * @param showStats Indica si se muestran las métricas de reseñas, seguidores y seguidos.
 * @param showAction Indica si se muestra el botón de acción principal.
 * @param showBiography Indica si se muestra la biografía del perfil.
 * @param onActionClick Acción solicitada al pulsar el botón principal.
 * @param onFollowersClick Abre los seguidores del ID representado en la cabecera.
 * @param onFollowingClick Abre los seguidos del ID representado en la cabecera.
 */
@Composable
fun ProfileHeader(
    profile: ProfileContent,
    @StringRes actionLabelResId: Int,
    modifier: Modifier = Modifier,
    displayName: String = "",
    email: String = "",
    profileImageUrl: String? = null,
    showDisplayName: Boolean = true,
    showStats: Boolean = true,
    showAction: Boolean = true,
    actionEnabled: Boolean = true,
    showBiography: Boolean = true,
    onActionClick: () -> Unit = {},
    onProfileClick: (String) -> Unit = {},
    onFollowersClick: ((String) -> Unit)? = null,
    onFollowingClick: ((String) -> Unit)? = null
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileAvatar(
            onProfileClick = onProfileClick,
            avatarResId = profile.avatarResId,
            userId = profile.id.toIntOrNull(),
            profileId = profile.id,
            imageUrl = profileImageUrl ?: profile.imageUrl,
            modifier = Modifier.size(84.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (showDisplayName) {
            Text(
                modifier = Modifier.clickable { onProfileClick(profile.id) },
                text = (displayName.ifBlank { profile.username ?: stringResource(profile.handleResId) }).asUserHandle(),
                style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary
            )
        }

        if (email.isNotBlank()) {
            Spacer(modifier = Modifier.height(if (showDisplayName) 4.dp else 0.dp))
            Text(
                text = email,
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
        }

        // Las secciones que se concentran en el contenido guardado prescinden de las métricas.
        if (showStats) {
            Spacer(modifier = Modifier.height(20.dp))
            // Métricas repartidas por igual para que queden centradas bajo el nombre.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                profile.stats.forEach { stat ->
                    val action = when (stat.labelResId) {
                        R.string.profile_followers -> onFollowersClick
                        R.string.profile_following -> onFollowingClick
                        else -> null
                    }
                    ProfileStat(stat, onClick = action?.let { callback -> { callback(profile.id) } })
                }
            }
        }

        if (showAction) {
            Spacer(modifier = Modifier.height(22.dp))

            Button(
                onClick = onActionClick,
                enabled = actionEnabled,
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
                text = profile.biography ?: stringResource(profile.biographyResId),
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium,
                color = colors.textSecondary
            )
        }
    }
}

/** Muestra una vista previa del encabezado del perfil sin controles de edición. */
@Composable
@Preview(showBackground = true, heightDp = 460)
fun ProfileHeaderPreview() {
    DevicersAppTheme {
        ProfileHeader(
            profile = com.example.devicersapp.ui.models.ProfileContent(id = "1", avatarResId = R.drawable.no_pfp_icon, handleResId = R.string.profile_handle, biographyResId = R.string.profile_biography, stats = emptyList()),
            actionLabelResId = R.string.profile_follow,
            actionEnabled = true,
            modifier = Modifier.padding(20.dp)
        )
    }
}
