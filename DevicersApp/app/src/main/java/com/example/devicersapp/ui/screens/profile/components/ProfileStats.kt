package com.example.devicersapp.ui.screens.profile.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
 * @param modifier Modificador aplicado al contenedor del perfil.
 */
@Composable
fun ProfileStats(profile: ProfileContent, modifier: Modifier = Modifier) {
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
            text = profile.name,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LocalDevicersColors.current.textPrimary
        )

        Text(
            text = profile.handle,
            fontSize = 13.sp,
            color = LocalDevicersColors.current.textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = profile.biography,
            modifier = Modifier.padding(horizontal = 24.dp),
            fontSize = 13.sp,
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
            onClick = {},
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
                text = "Editar perfil",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/** Muestra una estadística individual dentro del resumen del perfil. */
@Composable
private fun ProfileStat(stat: ProfileStatContent) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stat.number,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = LocalDevicersColors.current.textPrimary
        )

        Text(
            text = stat.label,
            fontSize = 11.sp,
            color = LocalDevicersColors.current.textSecondary
        )
    }
}

/** Muestra una vista previa de las estadísticas del perfil. */
@Composable
@Preview(showBackground = true)
fun ProfileStatsPreview() {
    ProfileStats(SampleProfileContent)
}

/** Muestra una vista previa de una estadística individual de perfil. */
@Composable
@Preview(showBackground = true)
fun ProfileStatPreview() {
    ProfileStat(ProfileStatContent("245", stringResource(R.string.profile_followers)))
}

/** Contiene datos de ejemplo para las vistas previas del perfil. */
private val SampleProfileContent
    @Composable get() = ProfileContent(
        avatarResId = R.drawable.profile_avatar_00,
        name = stringResource(R.string.profile_name),
        handle = stringResource(R.string.profile_handle),
        biography = stringResource(R.string.profile_biography),
        stats = listOf(
            ProfileStatContent("12", stringResource(R.string.profile_reviews)),
            ProfileStatContent("245", stringResource(R.string.profile_followers)),
            ProfileStatContent("180", stringResource(R.string.profile_following))
        )
    )
