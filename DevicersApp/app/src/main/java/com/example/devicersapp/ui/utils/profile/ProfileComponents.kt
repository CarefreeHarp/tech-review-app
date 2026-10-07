package com.example.devicersapp.ui.utils.profile

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import com.example.devicersapp.ui.theme.LocalDevicersColors
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
import com.example.devicersapp.ui.utils.images.localImageResIdFor
import com.example.devicersapp.R
import coil3.compose.AsyncImage

/**
 * Muestra una imagen de perfil circular y recortada para las entidades de usuario.
 *
 * @param avatarResId Recurso de imagen que representa al usuario.
 * @param imageUrl URL remota de la imagen de perfil; se muestra un marcador mientras carga.
 * @param modifier Modificador aplicado a la imagen de perfil.
 */
@Composable
fun ProfileAvatar(
    @DrawableRes avatarResId: Int,
    imageUrl: String? = null,
    modifier: Modifier = Modifier
) {
    val avatarModifier = modifier.clip(CircleShape)
    val localAvatar = localImageResIdFor(imageUrl)
    val fallbackPainter = painterResource(localAvatar ?: avatarResId)
    val placeholderPainter = fallbackPainter
    val errorPainter = fallbackPainter

    if (imageUrl.isNullOrBlank() || localAvatar != null) {
        Image(
            painter = fallbackPainter,
            contentDescription = null,
            modifier = avatarModifier,
            contentScale = ContentScale.Crop
        )
    } else {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            placeholder = placeholderPainter,
            error = errorPainter,
            modifier = avatarModifier,
            contentScale = ContentScale.Crop
        )
    }
}

/** Muestra un error del perfil con la acción de reintento, sin datos de muestra. */
@Composable
fun ProfileLoadStatus(
    @StringRes errorMessageResId: Int?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current
    Column(
        modifier = modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (errorMessageResId != null) {
            Text(stringResource(errorMessageResId), color = colors.textPrimary,
                style = MaterialTheme.typography.bodyMedium)
            TextButton(onClick = onRetry) {
                Text(stringResource(R.string.home_feed_retry), color = colors.primaryText)
            }
        }
    }
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

/** Muestra el mensaje de error y el reintento del perfil. */
@Composable
@Preview(showBackground = true)
fun ProfileLoadStatusPreview() {
    ProfileLoadStatus(R.string.own_profile_load_error, onRetry = {})
}
