package com.example.devicersapp.ui.screens.profile.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/**
 * Muestra el avatar, el nombre de usuario, las métricas, la acción principal y la biografía.
 *
 * El orden sigue al diseño editorial: primero la identidad, luego las métricas centradas,
 * después la acción y por último la biografía, separada del botón.
 *
 * @param profile Datos visibles del perfil, incluido el recurso de su avatar.
 * @param modifier Modificador aplicado al contenedor del perfil.
 * @param onEditProfileClick Acción solicitada al seleccionar editar perfil.
 */
@Composable
fun ProfileHeader(
    profile: ProfileContent,
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ProfileAvatar(
            avatarResId = profile.avatarResId,
            modifier = Modifier.size(104.dp)
        )

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = stringResource(profile.handleResId),
            style = MaterialTheme.typography.titleMedium,
            color = colors.textPrimary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Métricas repartidas por igual para que queden centradas bajo el nombre.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            profile.stats.forEach { stat -> ProfileStat(stat) }
        }

        Spacer(modifier = Modifier.height(22.dp))

        Button(
            onClick = onEditProfileClick,
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
                text = stringResource(R.string.profile_edit),
                style = MaterialTheme.typography.labelLarge
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = stringResource(profile.biographyResId),
            modifier = Modifier.padding(horizontal = 8.dp),
            style = MaterialTheme.typography.bodySmall,
            color = colors.textSecondary,
            textAlign = TextAlign.Center
        )
    }
}

/** Muestra una vista previa del encabezado del perfil. */
@Composable
@Preview(showBackground = true, heightDp = 460)
fun ProfileHeaderPreview() {
    DevicersAppTheme {
        ProfileHeader(
            SampleProfileContent,
            modifier = Modifier.padding(20.dp)
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
