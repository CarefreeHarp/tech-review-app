package com.example.devicersapp.ui.utils.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors

/**
 * Presenta las opciones laterales del perfil; navegación controla qué acciones están disponibles.
 * Las acciones de edición y guardados solo se ofrecen para el perfil propio.
 */
@Composable
fun ProfileOptionsContent(
    onSignOutClick: () -> Unit,
    modifier: Modifier = Modifier,
    onEditProfileClick: (() -> Unit)? = null,
    onSavedReviewsClick: (() -> Unit)? = null,
    onUserPreferencesClick: (() -> Unit)? = null
) {
    val colors = LocalDevicersColors.current
    ModalDrawerSheet(modifier = modifier.width(300.dp).fillMaxHeight(),
        drawerContainerColor = colors.surface, drawerContentColor = colors.textPrimary) {
        Column(Modifier.padding(horizontal = 20.dp, vertical = 24.dp)) {
            Text(stringResource(R.string.profile_options), style = MaterialTheme.typography.titleMedium,
                color = colors.textPrimary)
            Spacer(Modifier.height(24.dp))
            onEditProfileClick?.let { edit ->
                TextButton(onClick = edit, modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(contentColor = colors.textPrimary)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(R.drawable.edit_icon2),
                            contentDescription = null, modifier = Modifier.size(24.dp))
                        Text(stringResource(R.string.profile_edit), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
            onSavedReviewsClick?.let { saved ->
                TextButton(onClick = saved, modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(contentColor = colors.textPrimary)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(R.drawable.bookmark_icon),
                            contentDescription = null, modifier = Modifier.size(24.dp))
                        Text(stringResource(R.string.profile_saved), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
            onUserPreferencesClick?.let { preferences ->
                TextButton(onClick = preferences, modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.textButtonColors(contentColor = colors.textPrimary)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically) {
                        Icon(painter = painterResource(R.drawable.preferences_icon),
                            contentDescription = null, modifier = Modifier.size(24.dp))
                        Text(stringResource(R.string.user_preferences_title), style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            HorizontalDivider(color = colors.border)
            Spacer(Modifier.height(12.dp))
            TextButton(onClick = onSignOutClick, modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.textButtonColors(contentColor = colors.error)) {
                Row(modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically) {
                    Icon(painter = painterResource(R.drawable.logout_icon),
                        contentDescription = null, modifier = Modifier.size(24.dp))
                    Text(stringResource(R.string.sign_out), style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}

/** Previsualiza los accesos y el cierre de sesión del menú lateral propio. */
@Preview(showBackground = true)
@Composable
fun ProfileOptionsContentPreview() {
    DevicersAppTheme { ProfileOptionsContent(onSignOutClick = {}, onEditProfileClick = {}, onSavedReviewsClick = {}, onUserPreferencesClick = {}) }
}
