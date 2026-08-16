package com.example.devicersapp.ui.utils.profile

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

/**
 * Muestra una imagen de perfil circular y recortada para las entidades de usuario.
 *
 * @param avatarResId Recurso de imagen que representa al usuario.
 * @param modifier Modificador aplicado a la imagen de perfil.
 */
@Composable
fun ProfileAvatar(@DrawableRes avatarResId: Int, modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(avatarResId),
        contentDescription = null,
        modifier = modifier.clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}

/** Muestra una vista previa de un avatar circular reutilizable. */
@Composable
@Preview(showBackground = true)
fun ProfileAvatarPreview() {
    ProfileAvatar(
        avatarResId = R.drawable.profile_avatar_00,
        modifier = Modifier.size(64.dp)
    )
}
