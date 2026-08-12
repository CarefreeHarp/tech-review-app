package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R

/** Muestra el resumen y los mensajes de respuesta asociados a la reseña. */
@Composable
fun ReplyList(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(stringResource(R.string.review_replies, 2), fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(22.dp))
        ReplyItem(R.string.review_reply_author_one, R.string.review_reply_text_one)
        Spacer(Modifier.height(28.dp))
        ReplyItem(R.string.review_reply_author_two, R.string.review_reply_text_two)
    }
}

/** Muestra una vista previa de las respuestas de una reseña. */
@Composable
@Preview(showBackground = true)
fun ReplyListPreview() { ReplyList() }
