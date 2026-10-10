package com.example.devicersapp.ui.screens.home

import com.example.devicersapp.ui.screens.home.components.HomeSkeleton

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalFeedReviewProvider
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.screens.home.components.FeedReviewItem
import com.example.devicersapp.ui.screens.home.components.HomeFeedErrorMessage
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/**
 * Configura la pantalla principal y observa su estado desde el ViewModel.
 *
 * @param onProductClick Acción solicitada al abrir el detalle de un artículo reseñado.
 * @param onReviewClick Acción solicitada al abrir el detalle de una reseña.
 * @param onCommentClick Acción solicitada al abrir los comentarios de una reseña.
 * @param onSendClick Acción solicitada al compartir una reseña.
 * @param modifier Modificador aplicado al contenedor de la pantalla.
 * @param viewModel ViewModel que conserva el estado de la pantalla.
 */
@Composable
fun HomeView(
    onProductClick: (Int) -> Unit = {},
    onReviewClick: (Int) -> Unit = {},
    onCommentClick: (Int) -> Unit = {},
    onSendClick: (FeedReviewContent) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel,
    onProfileClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeViewContent(
        onProfileClick = onProfileClick,
        state = uiState,
        onForYouClick = viewModel::onForYouClick,
        onFollowingClick = viewModel::onFollowingClick,
        onRetryClick = viewModel::onRetryClick,
        onProductClick = onProductClick,
        onReviewClick = onReviewClick,
        onCommentClick = onCommentClick,
        onSendClick = onSendClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Muestra las pestañas del feed y, según el estado, la carga, el error o la lista de reseñas.
 */
@Composable
fun HomeViewContent(
    state: HomeState,
    onForYouClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onRetryClick: () -> Unit,
    onProductClick: (Int) -> Unit,
    onReviewClick: (Int) -> Unit,
    onCommentClick: (Int) -> Unit,
    onSendClick: (FeedReviewContent) -> Unit,
    modifier: Modifier = Modifier,
    onProfileClick: (String) -> Unit = {}
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        SectionTabsRow(
            startLabelResId = R.string.home_tab_for_you,
            endLabelResId = R.string.home_tab_following,
            isStartSelected = state.isForYouSelected,
            onStartClick = onForYouClick,
            onEndClick = onFollowingClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 3.dp,
            color = colors.border
        )

        when {
            state.isLoading -> HomeSkeleton(modifier = Modifier.weight(1f))

            state.errorMessageResId != null -> {
                HomeFeedErrorMessage(
                    messageResId = state.errorMessageResId,
                    onRetryClick = onRetryClick
                )
            }

            state.feedReviews.isEmpty() -> {
                Text(
                    text = stringResource(R.string.home_feed_empty),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 32.dp),
                    color = colors.textSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            else -> {
                LazyColumn {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }

                    itemsIndexed(
                        items = state.feedReviews,
                        key = { _, review -> review.reviewId }
                    ) { index, review ->
                        FeedReviewItem(
                            onProfileClick = onProfileClick,
                            review = review,
                            onProductClick = {
                                onProductClick(review.productId)
                            },
                            onViewMoreClick = {
                                onReviewClick(review.reviewId)
                            },
                            onCommentClick = {
                                onCommentClick(review.reviewId)
                            },
                            onSendClick = {
                                onSendClick(review)
                            }
                        )

                        if (index < state.feedReviews.lastIndex) {
                            Spacer(modifier = Modifier.height(20.dp))

                            HorizontalDivider(
                                modifier = Modifier.fillMaxWidth(),
                                thickness = 1.dp,
                                color = colors.border
                            )

                            Spacer(modifier = Modifier.height(20.dp))
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(110.dp))
                    }
                }
            }
        }
    }
}

/** Muestra una vista previa de la pantalla principal en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun HomeViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "home",
            showBottomBar = true,
            topBarNumber = 5
        ) { innerPadding ->
            HomeViewContent(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                state = HomeState(feedReviews = LocalFeedReviewProvider.reviews),
                onForYouClick = {},
                onFollowingClick = {},
                onRetryClick = {},
                onProductClick = {},
                onReviewClick = {},
                onCommentClick = {},
                onSendClick = {}
            )
        }
    }
}

/** Muestra una vista previa de la pantalla principal en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun HomeViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "home",
            showBottomBar = true,
            topBarNumber = 5
        ) { innerPadding ->
            HomeViewContent(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                state = HomeState(feedReviews = LocalFeedReviewProvider.reviews),
                onForYouClick = {},
                onFollowingClick = {},
                onRetryClick = {},
                onProductClick = {},
                onReviewClick = {},
                onCommentClick = {},
                onSendClick = {}
            )
        }
    }
}
