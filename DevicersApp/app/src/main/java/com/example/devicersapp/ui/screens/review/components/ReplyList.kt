package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReplyContent

/** Muestra el resumen y los mensajes de respuesta asociados a la reseña. */
@Composable
fun ReplyList(replies: List<ReplyContent>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(stringResource(R.string.review_replies, replies.size), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(18.dp))
        replies.forEachIndexed { index, reply ->
            ReplyItem(reply)
            if (index < replies.lastIndex) Spacer(Modifier.height(28.dp))
        }
    }
}

/** Muestra una vista previa de las respuestas de una reseña. */
@Composable
@Preview(showBackground = true)
fun ReplyListPreview() {
    ReplyList(
        listOf(
            ReplyContent(R.drawable.profile_avatar_03, stringResource(R.string.review_reply_author_one), stringResource(R.string.review_reply_time), stringResource(R.string.review_reply_text_one)),
            ReplyContent(R.drawable.profile_avatar_04, stringResource(R.string.review_reply_author_two), stringResource(R.string.review_reply_time), stringResource(R.string.review_reply_text_two))
        )
    )
}
