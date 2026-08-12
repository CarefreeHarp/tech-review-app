package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReplyContent

/** Muestra una respuesta individual con su autor, fecha, contenido y acción. */
@Composable
fun ReplyItem(reply: ReplyContent, modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .background(colorResource(R.color.surface_secondary_light), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(reply.avatarInitial, style = MaterialTheme.typography.labelLarge, color = colorResource(R.color.text_primary_light))
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(reply.author, style = MaterialTheme.typography.labelLarge, color = colorResource(R.color.text_primary_light))
            Text(reply.timeAgo, style = MaterialTheme.typography.bodySmall, color = colorResource(R.color.text_secondary_light))
            Spacer(Modifier.height(9.dp))
            Text(reply.text, style = MaterialTheme.typography.bodyMedium, color = colorResource(R.color.text_primary_light))
            Spacer(Modifier.height(10.dp))
            Text(stringResource(R.string.review_reply_action), style = MaterialTheme.typography.labelLarge, color = colorResource(R.color.text_secondary_light))
        }
    }
}

/** Muestra una vista previa de una respuesta individual. */
@Composable
@Preview(showBackground = true)
fun ReplyItemPreview() {
    ReplyItem(
        ReplyContent(
            avatarInitial = stringResource(R.string.review_reply_first_initial),
            author = stringResource(R.string.review_reply_author_one),
            timeAgo = stringResource(R.string.review_reply_time),
            text = stringResource(R.string.review_reply_text_one)
        )
    )
}
