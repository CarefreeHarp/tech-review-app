package com.example.devicersapp.ui.screens.profile_search_results.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileSearchResultContent
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchControlText
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/**
 * Muestra un perfil encontrado con su avatar, sus intereses, sus reseñas y la acción de seguirlo.
 *
 * @param result Perfil devuelto por la búsqueda.
 * @param isFollowed Indica si el perfil ya ha sido seguido.
 * @param onFollow Solicita cambiar el estado de seguimiento del perfil.
 * @param modifier Modificador aplicado a la tarjeta.
 */
@Composable
fun ProfileResultCard(
    result: ProfileSearchResultContent,
    isFollowed: Boolean,
    onFollow: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            // La sombra suave despega la tarjeta del fondo, como en el diseño editorial.
            .shadow(elevation = 4.dp, shape = RoundedCornerShape(18.dp))
            .background(colors.surface, RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileAvatar(
            avatarResId = result.avatarResId,
            modifier = Modifier.size(52.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(result.handleResId),
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(result.interestsResId),
                color = colors.textSecondary,
                style = CardMetadataText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = stringResource(result.reviewCountResId),
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = onFollow,
            modifier = Modifier.height(40.dp),
            shape = RoundedCornerShape(percent = 50),
            colors = ButtonDefaults.buttonColors(
                // La acción se presenta delineada para no competir con el contenido de la tarjeta.
                containerColor = colors.background,
                contentColor = colors.primaryText
            ),
            border = BorderStroke(1.dp, colors.primaryText),
            contentPadding = PaddingValues(horizontal = 18.dp)
        ) {
            Text(
                text = stringResource(
                    if (isFollowed) R.string.activity_following else R.string.profile_follow
                ),
                style = SearchControlText,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

/** Muestra una vista previa de un perfil encontrado. */
@Composable
@Preview(showBackground = true)
fun ProfileResultCardPreview() {
    DevicersAppTheme {
        ProfileResultCard(
            result = ProfileSearchResultContent(
                id = "preview",
                avatarResId = R.drawable.profile_avatar_01,
                handleResId = R.string.profile_result_first_handle,
                interestsResId = R.string.profile_result_first_interests,
                reviewCountResId = R.string.profile_result_first_reviews
            ),
            isFollowed = false,
            onFollow = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
