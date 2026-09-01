package com.example.devicersapp.ui.screens.home

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.home.components.FeedReviewItem
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/**
 * Configura la pantalla principal y observa su estado desde el ViewModel.
 *
 * @param onReviewClick Acción solicitada al abrir el detalle de una reseña.
 * @param onCommentClick Acción solicitada al abrir los comentarios de una reseña.
 * @param onSendClick Acción solicitada al compartir una reseña.
 * @param modifier Modificador aplicado al contenedor de la pantalla.
 * @param viewModel ViewModel que conserva el estado de la pantalla.
 */
@Composable
fun HomeView(
    onReviewClick: (Int) -> Unit = {},
    onCommentClick: (Int) -> Unit = {},
    onSendClick: (ReviewContent) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    HomeViewContent(
        feedItems = uiState.feedItems,
        isForYouSelected = uiState.isForYouSelected,
        onForYouClick = viewModel::onForYouClick,
        onFollowingClick = viewModel::onFollowingClick,
        onReviewClick = onReviewClick,
        onCommentClick = onCommentClick,
        onSendClick = onSendClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Muestra las pestañas del feed y la lista de reseñas publicadas.
 */
@Composable
fun HomeViewContent(
    feedItems: List<HomeFeedItem>,
    isForYouSelected: Boolean,
    onForYouClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    onCommentClick: (Int) -> Unit,
    onSendClick: (ReviewContent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    Column(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        SectionTabsRow(
            startLabelResId = R.string.home_tab_for_you,
            endLabelResId = R.string.home_tab_following,
            isStartSelected = isForYouSelected,
            onStartClick = onForYouClick,
            onEndClick = onFollowingClick
        )

        Spacer(modifier = Modifier.height(12.dp))

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 3.dp,
            color = colors.border
        )

        LazyColumn {
            item {
                Spacer(modifier = Modifier.height(20.dp))
            }

            itemsIndexed(feedItems) { index, feedItem ->
                val review = feedItem.review

                FeedReviewItem(
                    review = review,
                    author = feedItem.author,
                    onViewMoreClick = {
                        onReviewClick(review.id)
                    },
                    onCommentClick = {
                        onCommentClick(review.id)
                    },
                    onSendClick = {
                        onSendClick(review)
                    }
                )

                if (index < feedItems.lastIndex) {
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
            HomeView(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                viewModel = HomeViewModel()
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
            HomeView(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                viewModel = HomeViewModel()
            )
        }
    }
}