package com.example.devicersapp.ui.utils.profile

import androidx.compose.foundation.clickable
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
import coil3.compose.AsyncImage
import com.example.devicersapp.ui.session.LocalSessionState

/**
 * Muestra una imagen de perfil circular y recortada para las entidades de usuario.
 *
 * @param avatarResId Recurso local mostrado mientras se carga una foto remota.
 * @param imageUrl URL obtenida del perfil de Firestore; no se resuelven nombres de drawables.
 * @param modifier Modificador aplicado a la imagen de perfil.
 * @param userId Identificador del usuario; el usuario actual utiliza la foto de su sesión.
 * @param profileId Identificador del perfil al que conduce la foto, incluidos los perfiles locales.
 * @param onProfileClick Acción del padre al pulsar la foto; recibe el identificador del perfil.
 */
@Composable
fun ProfileAvatar(
    @DrawableRes avatarResId: Int,
    imageUrl: String? = null,
    modifier: Modifier = Modifier,
    userId: Int? = null,
    profileId: String? = userId?.toString(),
    onProfileClick: ((String) -> Unit)? = null
) {
    val resolvedImageUrl = LocalSessionState.current.userImageFor(userId, imageUrl)
    val avatarModifier = modifier.clip(CircleShape).then(
        if (!profileId.isNullOrBlank() && onProfileClick != null) {
            Modifier.clickable { onProfileClick(profileId) }
        } else Modifier
    )
    val fallbackPainter = painterResource(R.drawable.no_pfp_icon)
    val placeholderPainter = painterResource(avatarResId)
    val errorPainter = painterResource(R.drawable.pfp_error)

    AsyncImage(
        model = resolvedImageUrl?.takeIf { it.isNotBlank() },
        contentDescription = null,
        placeholder = placeholderPainter,
        fallback = fallbackPainter,
        error = errorPainter,
        modifier = avatarModifier,
        contentScale = ContentScale.Crop
    )
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
        avatarResId = R.drawable.no_pfp_icon,
        modifier = Modifier.size(64.dp)
    )
}

/** Muestra el mensaje de error y el reintento del perfil. */
@Composable
@Preview(showBackground = true)
fun ProfileLoadStatusPreview() {
    ProfileLoadStatus(R.string.own_profile_load_error, onRetry = {})
}
