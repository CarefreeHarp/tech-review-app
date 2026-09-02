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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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

/** Configura el detalle de una reseña y observa su estado desde el ViewModel. */
@Composable
fun ReviewView(
    reviewId: Int,
    onProductClick: (Int) -> Unit = {},
    onSendReply: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(reviewId) {
        viewModel.loadReview(reviewId)
    }

    val product = uiState.product
    val review = uiState.review

    if (product != null && review != null) {
        ReviewViewContent(
            product = product,
            review = review,
            replies = uiState.replies,
            replyText = uiState.replyText,
            onReplyTextChange = viewModel::onReplyTextChange,
            onProductClick = onProductClick,
            onSendReply = {
                val replyText = uiState.replyText

                if (replyText.isNotBlank()) {
                    onSendReply(replyText)
                    viewModel.clearReplyText()
                }
            },
            expandedReplies = uiState.expandedReplies,
            onViewAnswers = viewModel::onViewAnswers,
            modifier = modifier
                .fillMaxSize()
                .background(LocalDevicersColors.current.background)
        )
    }
}

/** Ensambla el producto, la reseña, las respuestas y el compositor. */
@Composable
fun ReviewViewContent(
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
                    Spacer(modifier = Modifier.height(8.dp))

                    ReviewProductSummary(
                        product = product,
                        onClick = {
                            onProductClick(product.nameResId)
                        }
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    ReviewDetail(
                        review = review
                    )

                    Spacer(modifier = Modifier.height(24.dp))

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
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

/** Muestra una vista previa del detalle de una reseña en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ReviewViewPreview() {
    val review = LocalReviewProvider.productReviews.first()
    val product = LocalReviewProvider.findProductByReviewId(review.id)

    if (product != null) {
        DevicersAppTheme(darkTheme = false) {
            DevicersScaffold(topBarNumber = 4) { innerPadding ->
                ReviewViewContent(
                    product = product,
                    review = review,
                    replies = review.comments,
                    replyText = "",
                    onReplyTextChange = {},
                    onProductClick = {},
                    onSendReply = {},
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(
                            LocalDevicersColors.current.background
                        )
                )
            }
        }
    }
}

/** Muestra una vista previa del detalle de una reseña en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ReviewViewDarkPreview() {
    val review = LocalReviewProvider.productReviews.first()
    val product = LocalReviewProvider.findProductByReviewId(review.id)

    if (product != null) {
        DevicersAppTheme(darkTheme = true) {
            DevicersScaffold(topBarNumber = 4) { innerPadding ->
                ReviewViewContent(
                    product = product,
                    review = review,
                    replies = review.comments,
                    replyText = "",
                    onReplyTextChange = {},
                    onProductClick = {},
                    onSendReply = {},
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .background(
                            LocalDevicersColors.current.background
                        )
                )
            }
        }
    }
}