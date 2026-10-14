package com.example.devicersapp.ui.screens.follow_list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.FollowListPreviewProvider
import com.example.devicersapp.ui.models.FollowListUser
import com.example.devicersapp.ui.session.LocalSessionState
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/** Muestra la identidad pública y comunica el ID pulsado para abrir el perfil desde navegación. */
@Composable
fun FollowListUserItem(
    user: FollowListUser,
    modifier: Modifier = Modifier,
    onProfileClick: (String) -> Unit = {}
) {
    val colors = LocalDevicersColors.current
    Row(modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(colors.surface)
        .clickable { onProfileClick(user.id.toString()) }.padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)) {
        ProfileAvatar(avatarResId = R.drawable.no_pfp_icon, imageUrl = user.imageUrl,
            userId = user.id, modifier = Modifier.size(48.dp))
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(LocalSessionState.current.userNameFor(user.id, user.username),
                style = MaterialTheme.typography.titleSmall, color = colors.textPrimary)
            if (user.biography.isNotBlank()) {
                Text(
                    text = if (user.isBiographyTruncated) {
                        stringResource(R.string.follow_list_truncated_biography, user.biography)
                    } else user.biography,
                    style = MaterialTheme.typography.bodyMedium, color = colors.textSecondary
                )
            }
        }
    }
}

/** Previsualiza un usuario con la misma política de avatar de los perfiles. */
@Preview(showBackground = true)
@Composable
fun FollowListUserItemPreview() {
    val provider = FollowListPreviewProvider(stringResource(R.string.username_placeholder), stringResource(R.string.profile_biography))
    DevicersAppTheme { FollowListUserItem(provider.user) }
}
