package com.example.devicersapp.ui.screens.review

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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

    ReviewViewContent(
        state = uiState,
        onReplyTextChange = viewModel::onReplyTextChange,
        onProductClick = onProductClick,
        onSendReply = {
            val replyText = uiState.replyText

            if (replyText.isNotBlank()) {
                onSendReply(replyText)
                viewModel.clearReplyText()
            }
        },
        onViewAnswers = viewModel::onViewAnswers,
        onRetry = {
            viewModel.loadReview(reviewId)
        },
        modifier = modifier
            .fillMaxSize()
            .background(
                LocalDevicersColors.current.background
            )
    )
}

/** Ensambla el producto, la reseña, las respuestas y el compositor. */
@Composable
fun ReviewViewContent(
    state: ReviewState,
    onReplyTextChange: (String) -> Unit,
    onProductClick: (Int) -> Unit,
    onSendReply: () -> Unit,
    onViewAnswers: (Int) -> Unit = {},
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    if (state.isLoading) {

        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        return
    }

    if (state.errorMessage != null) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "No se pudo cargar la reseña",
                color = colors.textPrimary,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = state.errorMessage,
                color = colors.textSecondary,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(
                onClick = onRetry
            ) {
                Text(
                    text = "Reintentar"
                )
            }
        }

        return
    }

    val product = state.product ?: return
    val review = state.review ?: return
    Box(modifier = modifier) {
        ReplyList(
            replies = state.replies,
            header = {
                item {
                    Spacer(modifier = Modifier.height(22.dp))
                    Spacer(modifier = Modifier.height(8.dp))

                    ReviewProductSummary(
                        product = product,
                        onClick = {
                            onProductClick(product.id)
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
            expandedReplies = state.expandedReplies,
            onViewAnswers = onViewAnswers,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        )

        ReplyComposer(
            value = state.replyText,
            onValueChange = onReplyTextChange,
            onSendClick = onSendReply,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}
