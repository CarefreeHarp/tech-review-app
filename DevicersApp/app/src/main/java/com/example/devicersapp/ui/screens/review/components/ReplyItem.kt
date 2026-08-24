package com.example.devicersapp.ui.screens.review.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.theme.CardHighlightText
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/** Diámetro del avatar de una respuesta, compartido con el conector para alinear sus trazos. */
val ReplyAvatarSize: Dp = 34.dp

/**
 * Muestra una respuesta del hilo con su avatar, su autor, su contenido y su acción.
 *
 * Cuando la respuesta contesta a otra, aparece sangrada y conectada por las líneas del hilo.
 *
 * La separación con la siguiente respuesta se aplica dentro de la propia respuesta, en vez de
 * como un espacio externo, porque las líneas del hilo deben atravesarla sin interrumpirse.
 *
 * @param reply Información de la respuesta, incluido su nivel dentro del hilo.
 * @param modifier Modificador aplicado a la respuesta.
 * @param passThroughLevels Niveles superiores cuya línea debe atravesar esta respuesta.
 * @param hasNestedReply Indica si de esta respuesta cuelga al menos una contestación.
 * @param bottomSpacing Espacio que separa esta respuesta de la siguiente.
 */
@Composable
fun ReplyItem(
    reply: ReplyContent,
    modifier: Modifier = Modifier,
    passThroughLevels: Set<Int> = emptySet(),
    hasNestedReply: Boolean = false,
    bottomSpacing: Dp = 0.dp
) {
    val colors = LocalDevicersColors.current

    Box(
        // La altura intrínseca deja que el conector abarque exactamente lo que ocupa la respuesta.
        modifier = modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        ReplyThreadConnector(
            depth = reply.depth,
            passThroughLevels = passThroughLevels,
            hasNestedReply = hasNestedReply,
            avatarSize = ReplyAvatarSize,
            modifier = Modifier.matchParentSize()
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = bottomSpacing)
        ) {
            // La sangría deja libre el espacio por el que bajan las líneas del hilo.
            Spacer(Modifier.width(ReplyThreadIndent * reply.depth))

            ProfileAvatar(
                avatarResId = reply.avatarResId,
                modifier = Modifier.size(ReplyAvatarSize)
            )

            Spacer(Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(reply.authorResId),
                    style = CardHighlightText,
                    color = colors.textPrimary
                )
                Text(
                    text = stringResource(reply.timeAgoResId),
                    style = CardMetadataText,
                    color = colors.textSecondary
                )

                Spacer(Modifier.height(8.dp))
                Text(
                    text = stringResource(reply.textResId),
                    style = MaterialTheme.typography.bodyMedium,
                    color = colors.textSecondary
                )
                Spacer(Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.like_icon),
                        contentDescription = stringResource(R.string.review_action_like),
                        modifier = Modifier.size(17.dp),
                        tint = colors.textSecondary
                    )
                    Text(
                        text = reply.likes.toString(),
                        style = CardMetadataText,
                        color = colors.textSecondary
                    )
                    // La reacción y su contador se alimentarán desde los datos remotos de la respuesta.
                    Text(
                        text = stringResource(R.string.review_reply_action),
                        style = CardMetadataText,
                        color = colors.primaryText
                    )
                }
            }
        }
    }
}

/** Muestra una vista previa de una respuesta anidada que además tiene contestación. */
@Composable
@Preview(showBackground = true)
fun ReplyItemPreview() {
    DevicersAppTheme {
        ReplyItem(
            ReplyContent(
                avatarResId = R.drawable.profile_avatar_03,
                authorResId = R.string.review_reply_author_two,
                timeAgoResId = R.string.review_reply_time,
                textResId = R.string.review_reply_text_two,
                depth = 1,
                replyingToResId = R.string.review_reply_author_one
            ),
            modifier = Modifier.padding(16.dp),
            passThroughLevels = setOf(0),
            hasNestedReply = true,
            bottomSpacing = 12.dp
        )
    }
}
