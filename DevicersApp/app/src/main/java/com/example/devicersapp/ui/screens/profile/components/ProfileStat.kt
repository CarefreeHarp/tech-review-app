package com.example.devicersapp.ui.screens.profile.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Muestra una estadística individual dentro del resumen de un perfil.
 *
 * @param stat Valor y etiqueta de la estadística.
 * @param modifier Modificador aplicado a la estadística.
 */
@Composable
fun ProfileStat(
    stat: ProfileStatContent,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(stat.numberResId),
            style = MaterialTheme.typography.titleSmall,
            color = LocalDevicersColors.current.textPrimary
        )
        Text(
            text = stringResource(stat.labelResId),
            style = MaterialTheme.typography.labelSmall,
            color = LocalDevicersColors.current.textSecondary
        )
    }
}

/** Muestra una vista previa de una estadística individual de perfil. */
@Composable
@Preview(showBackground = true)
fun ProfileStatPreview() {
    DevicersAppTheme {
        ProfileStat(ProfileStatContent(R.string.profile_followers_count, R.string.profile_followers))
    }
}
