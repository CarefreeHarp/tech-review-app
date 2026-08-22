package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.theme.SearchHeadingText

/**
 * Muestra el hilo completo de respuestas asociado a una reseña.
 *
 * Las respuestas llegan en orden de lectura, así que desde aquí se calcula qué líneas del hilo
 * debe dibujar cada una y se le entregan ya resueltas.
 *
 * @param replies Respuestas del hilo, en el orden en que se leen.
 * @param modifier Modificador aplicado al hilo.
 */
@Composable
fun ReplyList(replies: List<ReplyContent>, modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.review_replies, replies.size),
            style = SearchHeadingText,
            color = LocalDevicersColors.current.textPrimary
        )
        Spacer(Modifier.height(18.dp))

        replies.forEachIndexed { index, reply ->
            val nextReplyDepth = replies.getOrNull(index + 1)?.depth

            ReplyItem(
                reply = reply,
                // Un nivel superior solo sigue bajando si aún le queda otra respuesta por alcanzar.
                passThroughLevels = (0 until reply.depth)
                    .filter { level -> hasPendingReplyAtLevel(replies, index, level) }
                    .toSet(),
                hasNestedReply = hasPendingReplyAtLevel(replies, index, reply.depth),
                // Una respuesta y la que cuelga de ella van juntas; los hilos distintos se separan.
                bottomSpacing = when {
                    nextReplyDepth == null -> 0.dp
                    nextReplyDepth > reply.depth -> 12.dp
                    else -> 26.dp
                }
            )
        }
    }
}

/**
 * Indica si por debajo de una respuesta queda otra que cuelgue directamente del nivel indicado.
 *
 * El recorrido se detiene en cuanto aparece una respuesta de ese nivel o más superficial, porque
 * ahí termina la rama: a partir de ese punto el hilo ya pertenece a otro comentario.
 *
 * @param replies Respuestas del hilo en orden de lectura.
 * @param index Posición de la respuesta desde la que se mira hacia abajo.
 * @param level Nivel cuya línea se está evaluando.
 * @return `true` solo si esa línea todavía debe alcanzar a una respuesta más abajo.
 */
private fun hasPendingReplyAtLevel(
    replies: List<ReplyContent>,
    index: Int,
    level: Int
): Boolean {
    for (position in index + 1 until replies.size) {
        val depth = replies[position].depth
        if (depth <= level) return false
        if (depth == level + 1) return true
    }
    return false
}

/** Muestra una vista previa de un hilo con respuestas hermanas y anidadas. */
@Composable
@Preview(showBackground = true, heightDp = 620)
fun ReplyListPreview() {
    DevicersAppTheme {
        ReplyList(
            listOf(
                ReplyContent(
                    avatarResId = R.drawable.profile_avatar_03,
                    authorResId = R.string.review_reply_author_one,
                    timeAgoResId = R.string.review_reply_time,
                    textResId = R.string.review_reply_text_one
                ),
                ReplyContent(
                    avatarResId = R.drawable.profile_avatar_04,
                    authorResId = R.string.review_reply_author_two,
                    timeAgoResId = R.string.review_reply_time,
                    textResId = R.string.review_reply_text_two,
                    depth = 1,
                    replyingToResId = R.string.review_reply_author_one
                ),
                ReplyContent(
                    avatarResId = R.drawable.profile_avatar_01,
                    authorResId = R.string.review_reply_author_three,
                    timeAgoResId = R.string.review_reply_time_three,
                    textResId = R.string.review_reply_text_three,
                    depth = 2,
                    replyingToResId = R.string.review_reply_author_two
                ),
                ReplyContent(
                    avatarResId = R.drawable.profile_avatar_05,
                    authorResId = R.string.review_reply_author_four,
                    timeAgoResId = R.string.review_reply_time_four,
                    textResId = R.string.review_reply_text_four,
                    depth = 1,
                    replyingToResId = R.string.review_reply_author_one
                )
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
