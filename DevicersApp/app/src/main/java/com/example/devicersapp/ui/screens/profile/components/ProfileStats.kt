package com.example.devicersapp.ui.screens.profile.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/**
 * Muestra el avatar, la información pública, las estadísticas y la acción principal del perfil.
 *
 * @param profile Datos visibles del perfil, incluido el recurso de su avatar.
 * @param onEditProfileClick Acción solicitada al seleccionar editar perfil.
 * @param modifier Modificador aplicado al contenedor del perfil.
 */
@Composable
fun ProfileStats(
    profile: ProfileContent,
    onEditProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        ProfileAvatar(
            avatarResId = profile.avatarResId,
            modifier = Modifier.size(88.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = stringResource(profile.nameResId),
            style = MaterialTheme.typography.titleLarge,
            color = LocalDevicersColors.current.textPrimary
        )

        Text(
            text = stringResource(profile.handleResId),
            style = MaterialTheme.typography.bodySmall,
            color = LocalDevicersColors.current.textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(profile.biographyResId),
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.bodySmall,
            color = LocalDevicersColors.current.textSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Estadísticas
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            profile.stats.forEach { stat -> ProfileStat(stat) }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = onEditProfileClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(44.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = LocalDevicersColors.current.primaryYellow,
                contentColor = LocalDevicersColors.current.textOnPrimary
            )
        ) {
            Text(
                text = stringResource(R.string.profile_edit),
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

/** Muestra una vista previa de las estadísticas del perfil. */
@Composable
@Preview(showBackground = true)
fun ProfileStatsPreview() {
    ProfileStats(SampleProfileContent)
}

/** Contiene datos de ejemplo para las vistas previas del perfil. */
private val SampleProfileContent = ProfileContent(
        avatarResId = R.drawable.profile_avatar_00,
        nameResId = R.string.profile_name,
        handleResId = R.string.profile_handle,
        biographyResId = R.string.profile_biography,
        stats = listOf(
            ProfileStatContent(R.string.profile_reviews_count, R.string.profile_reviews),
            ProfileStatContent(R.string.profile_followers_count, R.string.profile_followers),
            ProfileStatContent(R.string.profile_following_count, R.string.profile_following)
        )
    )
