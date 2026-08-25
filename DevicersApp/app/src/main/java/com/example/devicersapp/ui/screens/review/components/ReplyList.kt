package com.example.devicersapp.ui.screens.review.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
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
 * @param header Contenido mostrado antes del título y de las respuestas.
 * @param expandedReplies Estado que indica qué respuestas muestran sus contestaciones.
 * @param onViewAnswers Acción que solicita revelar las contestaciones de una respuesta.
 * @param modifier Modificador aplicado al hilo.
 */
@Composable
fun ReplyList(
    replies: List<ReplyContent>,
    header: LazyListScope.() -> Unit = {},
    expandedReplies: Map<Int, Boolean> = emptyMap(),
    onViewAnswers: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        // El espacio final permite que la última respuesta suba por encima del compositor flotante.
        contentPadding = PaddingValues(bottom = 112.dp)
    ) {
        header()
        item {
            Spacer(Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.review_replies, replies.size),
                style = SearchHeadingText,
                color = LocalDevicersColors.current.textPrimary
            )
            Spacer(Modifier.height(18.dp))
        }

        itemsIndexed(replies) { index, reply ->
            val ancestorIndexes = ancestorIndexesFor(replies, index)
            val areAncestorsExpanded = ancestorIndexes.all { expandedReplies[it] == true }
            val nextReplyDepth = replies.getOrNull(index + 1)?.depth
            val hasNestedReplies = hasPendingReplyAtLevel(replies, index, reply.depth)
            val areAnswersVisible = expandedReplies[index] == true
            val passThroughLevels = (0 until reply.depth)
                .filter { level ->
                    val ancestorIndex = ancestorIndexes.getOrNull(level)
                    hasPendingReplyAtLevel(replies, index, level) ||
                        (ancestorIndex != null && expandedReplies[ancestorIndex] == true)
                }
                .toSet()

            Column(modifier = Modifier.fillMaxWidth()) {
                if (areAncestorsExpanded) {
                    ReplyItem(
                        reply = reply,
                        // Un ancestro expandido mantiene su línea hasta que alcance su control de ocultar.
                        passThroughLevels = passThroughLevels,
                        // La rama se conecta al control tanto cerrada como desplegada.
                        hasNestedReply = hasNestedReplies,
                        // Una respuesta y la que cuelga de ella van juntas; los hilos distintos se separan.
                        bottomSpacing = when {
                            nextReplyDepth == null -> 0.dp
                            nextReplyDepth > reply.depth -> 12.dp
                            else -> 26.dp
                        }
                    )
                    if (hasNestedReplies && !areAnswersVisible) {
                        ReplyAnswersToggle(
                            depth = reply.depth,
                            passThroughLevels = passThroughLevels,
                            answersCount = directNestedReplyCount(replies, index, reply.depth),
                            areAnswersVisible = false,
                            onClick = { onViewAnswers(index) }
                        )
                        // El control ya ocupa su propio alto y dibuja la línea de los ancestros.
                        // No se agrega un espacio externo para que ese trazo llegue al siguiente hilo.
                    }
                }

                if (areAncestorsExpanded) {
                    replies.indices
                        .filter { parentIndex ->
                            parentIndex < index &&
                                expandedReplies[parentIndex] == true &&
                                hasPendingReplyAtLevel(replies, parentIndex, replies[parentIndex].depth) &&
                                lastNestedReplyIndex(replies, parentIndex) == index
                        }
                        .sortedDescending()
                        .forEach { parentIndex ->
                            ReplyAnswersToggle(
                                depth = replies[parentIndex].depth,
                                passThroughLevels = (0 until replies[parentIndex].depth)
                                    .filter { level ->
                                        val ancestorIndex = ancestorIndexes.getOrNull(level)
                                        ancestorIndex != null && expandedReplies[ancestorIndex] == true
                                    }
                                    .toSet(),
                                answersCount = directNestedReplyCount(
                                    replies,
                                    parentIndex,
                                    replies[parentIndex].depth
                                ),
                                areAnswersVisible = true,
                                onClick = { onViewAnswers(parentIndex) }
                            )
                        }
                }
            }
        }
    }
}

/** Calcula los ancestros de una respuesta sin depender del orden de composición perezosa. */
private fun ancestorIndexesFor(replies: List<ReplyContent>, index: Int): List<Int> {
    val ancestorIndexes = mutableListOf<Int>()
    for (position in 0 until index) {
        while (ancestorIndexes.size > replies[position].depth) ancestorIndexes.removeLast()
        ancestorIndexes.add(position)
    }
    while (ancestorIndexes.size > replies[index].depth) ancestorIndexes.removeLast()
    return ancestorIndexes
}

/** Cuenta las contestaciones directas de una respuesta dentro de su misma rama. */
private fun directNestedReplyCount(replies: List<ReplyContent>, index: Int, level: Int): Int =
    replies.drop(index + 1).takeWhile { it.depth > level }.count { it.depth == level + 1 }

/** Devuelve el último índice que pertenece a la rama anidada de una respuesta. */
private fun lastNestedReplyIndex(replies: List<ReplyContent>, index: Int): Int {
    val level = replies[index].depth
    return (index + 1 until replies.size).firstOrNull { replies[it].depth <= level }?.minus(1)
        ?: replies.lastIndex
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
                    authorId = "reply_one",
                    timeAgoResId = R.string.review_reply_time,
                    textResId = R.string.review_reply_text_one
                ),
                ReplyContent(
                    authorId = "reply_two",
                    timeAgoResId = R.string.review_reply_time,
                    textResId = R.string.review_reply_text_two,
                    depth = 1
                ),
                ReplyContent(
                    authorId = "reply_three",
                    timeAgoResId = R.string.review_reply_time_three,
                    textResId = R.string.review_reply_text_three,
                    depth = 2
                ),
                ReplyContent(
                    authorId = "reply_four",
                    timeAgoResId = R.string.review_reply_time_four,
                    textResId = R.string.review_reply_text_four,
                    depth = 1
                )
            ),
            modifier = Modifier.padding(16.dp)
        )
    }
}
