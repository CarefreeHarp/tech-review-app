package com.example.devicersapp.ui.screens.edit_profile.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/** Muestra la foto del borrador con el lápiz exclusivo del formulario de edición. */
@Composable
fun EditProfilePhoto(imageUrl: String?, onChoosePhoto: () -> Unit, modifier: Modifier = Modifier) {
    val colors = LocalDevicersColors.current
    Box(modifier = modifier) {
        // Sin ID de navegación, el avatar muestra la selección local en vez de sustituirla por la foto de sesión.
        ProfileAvatar(avatarResId = R.drawable.no_pfp_icon, imageUrl = imageUrl, modifier = Modifier.size(104.dp))
        IconButton(onClick = onChoosePhoto,
            modifier = Modifier.align(Alignment.BottomEnd).size(36.dp).background(colors.primary, CircleShape)) {
            Icon(painterResource(R.drawable.edit_icon), stringResource(R.string.profile_edit_avatar),
                tint = colors.textOnPrimary, modifier = Modifier.size(18.dp))
        }
    }
}

/** Previsualiza el único avatar de la aplicación que permite editar la foto. */
@Preview(showBackground = true)
@Composable
fun EditProfilePhotoPreview() {
    DevicersAppTheme { EditProfilePhoto(imageUrl = null, onChoosePhoto = {}) }
}
