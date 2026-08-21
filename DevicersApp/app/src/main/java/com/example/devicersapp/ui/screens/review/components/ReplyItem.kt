package com.example.devicersapp.ui.screens.review.components

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.theme.CardHighlightText
import com.example.devicersapp.ui.theme.CardMetadataText
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.profile.ProfileAvatar

/**
 * Muestra una respuesta individual con avatar, autor, fecha, contenido y acción.
 *
 * @param reply Información de la respuesta, incluido el recurso de avatar de su autor.
 * @param modifier Modificador aplicado a la respuesta.
 */
@Composable
fun ReplyItem(reply: ReplyContent, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        ProfileAvatar(
            avatarResId = reply.avatarResId,
            modifier = Modifier.size(34.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(reply.authorResId),
                style = CardHighlightText,
                color = LocalDevicersColors.current.textPrimary
            )
            Text(
                text = stringResource(reply.timeAgoResId),
                style = CardMetadataText,
                color = LocalDevicersColors.current.textSecondary
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = stringResource(reply.textResId),
                style = MaterialTheme.typography.bodyMedium,
                color = LocalDevicersColors.current.textSecondary
            )
            Spacer(Modifier.height(8.dp))
            // La acción de responder se lee como enlace de acento.
            Text(
                text = stringResource(R.string.review_reply_action),
                style = CardMetadataText,
                color = LocalDevicersColors.current.primaryText
            )
        }
    }
}

/** Muestra una vista previa de una respuesta individual. */
@Composable
@Preview(showBackground = true)
fun ReplyItemPreview() {
    DevicersAppTheme {
    ReplyItem(
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_03,
            authorResId = R.string.review_reply_author_one,
            timeAgoResId = R.string.review_reply_time,
            textResId = R.string.review_reply_text_one
        ),
        modifier = Modifier.padding(16.dp)
    )
    }
}
