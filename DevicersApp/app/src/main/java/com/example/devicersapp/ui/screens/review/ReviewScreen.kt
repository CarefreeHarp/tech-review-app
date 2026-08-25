package com.example.devicersapp.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.review.components.ReplyComposer
import com.example.devicersapp.ui.screens.review.components.ReplyList
import com.example.devicersapp.ui.screens.review.components.ReviewDetail
import com.example.devicersapp.ui.screens.review.components.ReviewProductSummary
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Configura el estado de respuesta y los datos locales del detalle de una reseña. */
@Composable
fun ReviewScreen(
    reviewId: Int? = null,
    onProductClick: (Int) -> Unit = {},
    onSendReply: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var replyText by rememberSaveable { mutableStateOf("") }
    val expandedReplies = remember { mutableStateMapOf<Int, Boolean>() }
    val selectedReviewId = reviewId ?: LocalReviewProvider.productReviews.first().id
    val review = requireNotNull(LocalReviewProvider.findById(selectedReviewId))

    ReviewScreenContent(
        product = requireNotNull(LocalReviewProvider.findProductByReviewId(selectedReviewId)),
        review = review,
        replies = review.comments,
        replyText = replyText,
        onReplyTextChange = { replyText = it },
        onProductClick = onProductClick,
        onSendReply = {
            if (replyText.isNotBlank()) {
                onSendReply(replyText)
                replyText = ""
            }
        },
        expandedReplies = expandedReplies,
        onViewAnswers = { replyIndex ->
            expandedReplies[replyIndex] = expandedReplies[replyIndex] != true
        },
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla el producto, la reseña, las respuestas y el compositor controlado por la pantalla.
 *
 * @param product Producto asociado a la reseña.
 * @param review Reseña mostrada en el detalle.
 * @param replies Respuestas locales asociadas a la reseña.
 * @param replyText Texto actual del campo de respuesta.
 * @param onReplyTextChange Acción que solicita actualizar la respuesta.
 * @param onSendReply Acción solicitada al enviar la respuesta.
 * @param expandedReplies Estado que indica qué respuestas muestran sus contestaciones.
 * @param onViewAnswers Acción que solicita revelar las contestaciones de una respuesta.
 * @param modifier Modificador aplicado al contenido.
 */
@Composable
fun ReviewScreenContent(
    product: ProductContent,
    review: ReviewContent,
    replies: List<ReplyContent>,
    replyText: String,
    onReplyTextChange: (String) -> Unit,
    onProductClick: (Int) -> Unit,
    onSendReply: () -> Unit,
    expandedReplies: Map<Int, Boolean> = emptyMap(),
    onViewAnswers: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        ReplyList(
            replies = replies,
            header = {
                item {
                    Spacer(modifier = Modifier.height(22.dp))
                    // El margen interno evita que el borde de la lista recorte la sombra superior.
                    Spacer(modifier = Modifier.height(8.dp))

                    ReviewProductSummary(
                        product = product,
                        onClick = {
                            onProductClick(product.nameResId)
                        }
                    )

                    Spacer(modifier = Modifier.height(22.dp))
                    ReviewDetail(review = review)

                    Spacer(modifier = Modifier.height(24.dp))
                    // El divisor separa la reseña de la conversación que generó.
                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = LocalDevicersColors.current.border
                    )
                }
            },
            expandedReplies = expandedReplies,
            onViewAnswers = onViewAnswers,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        )
        ReplyComposer(
            value = replyText,
            onValueChange = onReplyTextChange,
            onSendClick = onSendReply,
            // El compositor flota sobre las respuestas para conservar la continuidad visual del hilo.
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

/** Muestra una vista previa del detalle de una reseña en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ReviewScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(topBarNumber = 4) { innerPadding ->
            ReviewScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}

/** Muestra una vista previa del detalle de una reseña en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ReviewScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(topBarNumber = 4) { innerPadding ->
            ReviewScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
