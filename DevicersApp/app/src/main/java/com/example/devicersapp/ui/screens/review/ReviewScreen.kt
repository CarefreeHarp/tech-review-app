package com.example.devicersapp.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.data.local.LocalReviewScreenProvider
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
    reviewId: Int = 0,
    isSavedReview: Boolean = false,
    onProductClick: (Int) -> Unit = {},
    onSendReply: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var replyText by rememberSaveable { mutableStateOf("") }

    ReviewScreenContent(
        product = if (isSavedReview) {
            LocalReviewScreenProvider.getSavedProductByReviewId(reviewId)
        } else {
            LocalReviewScreenProvider.getProductByReviewId(reviewId)
        },

        review = if (isSavedReview) {
            LocalReviewScreenProvider.getSavedReviewById(reviewId)
        } else {
            LocalReviewScreenProvider.getReviewById(reviewId)
        },
        replies = LocalReviewScreenProvider.replies,
        replyText = replyText,
        onReplyTextChange = { replyText = it },
        onProductClick = onProductClick,
        onSendReply = {
            if (replyText.isNotBlank()) {
                onSendReply(replyText)
                replyText = ""
            }
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
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
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

            ReplyList(replies = replies, modifier = Modifier.padding(top = 24.dp, bottom = 20.dp))
        }
        ReplyComposer(
            value = replyText,
            onValueChange = onReplyTextChange,
            onSendClick = onSendReply,
            modifier = Modifier.fillMaxWidth()
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
