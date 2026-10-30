package com.example.devicersapp.ui.screens.review

import com.example.devicersapp.ui.utils.profile.asUserHandle
import com.example.devicersapp.ui.screens.review.components.ReviewSkeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import androidx.compose.ui.res.stringResource
import com.example.devicersapp.ui.screens.review.components.ReplyComposer
import com.example.devicersapp.ui.screens.review.components.ReplyList
import com.example.devicersapp.ui.screens.review.components.ReviewActionsMenu
import com.example.devicersapp.ui.screens.review.components.ReviewDetail
import com.example.devicersapp.ui.screens.review.components.ReviewProductSummary
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.navigation.TopBarStyle

/** Configura el detalle de una reseña y observa su estado desde el ViewModel. */
@Composable
fun ReviewView(
    reviewId: Int,
    localReview: Boolean = false,
    onEditClick: (Int) -> Unit = {},
    onDeleteRequested: (Int) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    onProductClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ReviewViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(reviewId, localReview) {
        if (localReview) viewModel.loadLocalReview(reviewId) else viewModel.loadReview(reviewId)
    }

    ReviewViewContent(
            state = uiState,
            onRetry = { viewModel.loadReview(reviewId) },
            onEditClick = { id ->
                if (uiState.canManage && !uiState.deletionRequested) {
                    viewModel.setActionsMenuExpanded(false)
                    onEditClick(id)
                }
            },
            onDeleteClick = { viewModel.requestDeletion()?.let(onDeleteRequested) },
            onActionsMenuChange = viewModel::setActionsMenuExpanded,
            onLikeClick = viewModel::onLikeClick,
            onCommentLikeClick = viewModel::onCommentLikeClick,
            onAuthorClick = onAuthorClick,
            onReplyTextChange = viewModel::onReplyTextChange,
            onProductClick = onProductClick,
            onSendReply = viewModel::onSendReply,
            onViewAnswers = viewModel::onViewAnswers,
            modifier = modifier
                .fillMaxSize()
                .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla el producto, la reseña, las respuestas y el compositor.
 *
 * @param onLikeClick Acción solicitada al dar o quitar el like de la reseña.
 */
@Composable
fun ReviewViewContent(
    state: ReviewState,
    onEditClick: (Int) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onActionsMenuChange: (Boolean) -> Unit = {},
    onLikeClick: () -> Unit = {},
    onCommentLikeClick: (Int) -> Unit = {},
    onAuthorClick: (String) -> Unit = {},
    onReplyTextChange: (String) -> Unit,
    onProductClick: (Int) -> Unit,
    onSendReply: () -> Unit,
    onViewAnswers: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    onRetry: () -> Unit = {}
) {
    state.error?.let { error ->
        androidx.compose.foundation.layout.Column(modifier) {
            androidx.compose.material3.Text(stringResource(error))
            androidx.compose.material3.TextButton(onClick = onRetry) {
                androidx.compose.material3.Text(stringResource(R.string.home_feed_retry))
            }
        }
        return
    }
    if (state.loading) {
        ReviewSkeleton(modifier)
        return
    }
    val product = state.product ?: return
    val review = state.review ?: return
    Box(modifier = modifier) {
        ReplyList(
            onProfileClick = onAuthorClick,
            replies = state.replies,
            header = {
                item {
                    Spacer(modifier = Modifier.height(22.dp))
                    Spacer(modifier = Modifier.height(8.dp))

                    ReviewProductSummary(
                        product = product,
                        onClick = if (state.isLocal) null else ({ onProductClick(product.id ?: product.nameResId) })
                    )

                    Spacer(modifier = Modifier.height(22.dp))

                    ReviewDetail(
                        review = review,
                        actions = {
                            if (state.canManage && !state.isLocal) {
                                ReviewActionsMenu(
                                    expanded = state.actionsMenuExpanded,
                                    enabled = !state.deletionRequested,
                                    onExpandedChange = onActionsMenuChange,
                                    onEdit = { onEditClick(review.id) },
                                    onDelete = onDeleteClick
                                )
                            }
                        },
                        onLikeClick = onLikeClick,
                        onProfileClick = onAuthorClick
                    )

                    state.commentLikeError?.let { error ->
                        androidx.compose.material3.Text(stringResource(error), color = LocalDevicersColors.current.textSecondary)
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    HorizontalDivider(
                        modifier = Modifier.fillMaxWidth(),
                        thickness = 1.dp,
                        color = LocalDevicersColors.current.border
                    )
                }
            },
            onLikeClick = onCommentLikeClick,
            pendingLikeIds = state.pendingCommentLikeIds,
            expandedReplies = state.expandedReplies,
            onViewAnswers = onViewAnswers,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            // El aviso va junto al compositor para que se vea aunque el hilo esté desplazado.
            state.replyError?.let { error ->
                Text(
                    text = stringResource(error),
                    style = MaterialTheme.typography.bodySmall,
                    color = LocalDevicersColors.current.error,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(LocalDevicersColors.current.background)
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
            ReplyComposer(
                placeholder = if (state.isLocal) null else stringResource(R.string.remote_review_reply_to, (review.authorName.orEmpty()).asUserHandle()),
                value = state.replyText,
                onValueChange = onReplyTextChange,
                onSendClick = onSendReply,
                enabled = !state.replyInProgress,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/** Muestra una vista previa del detalle de una reseña en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ReviewViewPreview() {
    val review = com.example.devicersapp.ui.models.ReviewContent(authorId = "1", productNameResId = R.string.product_title, productImageResId = R.drawable.no_pfp_icon, productMetadataResId = R.string.product_brand_label, rating = 4, textResId = R.string.review_reply_text_two, likes = 0)
    val product = com.example.devicersapp.ui.models.ProductContent(R.string.product_title, R.string.product_brand_label, R.drawable.no_pfp_icon, R.string.review_product_image)

    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(topBarStyle = TopBarStyle.Review) { innerPadding ->
            ReviewViewContent(
                state = ReviewState(
                    product = product,
                    review = review,
                    replies = review.comments
                ),
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

/** Muestra una vista previa del detalle de una reseña en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ReviewViewDarkPreview() {
    val review = com.example.devicersapp.ui.models.ReviewContent(authorId = "1", productNameResId = R.string.product_title, productImageResId = R.drawable.no_pfp_icon, productMetadataResId = R.string.product_brand_label, rating = 4, textResId = R.string.review_reply_text_two, likes = 0)
    val product = com.example.devicersapp.ui.models.ProductContent(R.string.product_title, R.string.product_brand_label, R.drawable.no_pfp_icon, R.string.review_product_image)

    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(topBarStyle = TopBarStyle.Review) { innerPadding ->
            ReviewViewContent(
                state = ReviewState(
                    product = product,
                    review = review,
                    replies = review.comments
                ),
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
