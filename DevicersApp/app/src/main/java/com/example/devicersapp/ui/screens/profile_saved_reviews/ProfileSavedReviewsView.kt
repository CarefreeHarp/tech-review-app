package com.example.devicersapp.ui.screens.profile_saved_reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.profile_saved_reviews.components.SavedReviewCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/** Renderiza las reseñas guardadas a partir del estado administrado por el ViewModel. */
@Composable
fun ProfileSavedReviewsView(
    onReviewClick: (Int) -> Unit = {},
    onReviewsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileSavedReviewsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    ProfileSavedReviewsViewContent(
        state = uiState,
        onReviewsClick = {
            viewModel.onReviewsSelected()
            onReviewsClick()
        },
        onSavedClick = viewModel::onSavedSelected,
        onReviewClick = onReviewClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/** Ensambla el encabezado y la cuadrícula de reseñas guardadas del perfil propio. */
@Composable
fun ProfileSavedReviewsViewContent(
    state: ProfileSavedReviewsState,
    onReviewsClick: () -> Unit,
    onSavedClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                ProfileAvatar(avatarResId = state.profile.avatarResId, modifier = Modifier.size(84.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(state.profile.handleResId),
                    color = LocalDevicersColors.current.textPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                SectionTabsRow(
                    startLabelResId = R.string.profile_reviews,
                    endLabelResId = R.string.profile_saved,
                    isStartSelected = state.isReviewsSelected,
                    onStartClick = onReviewsClick,
                    onEndClick = onSavedClick,
                    selectedColor = LocalDevicersColors.current.primaryText
                )
                Spacer(modifier = Modifier.height(18.dp))
            }
        }
        itemsIndexed(state.savedReviews) { _, savedReview ->
            SavedReviewCard(review = savedReview, onClick = { onReviewClick(savedReview.id) })
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Muestra las reseñas guardadas en tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ProfileSavedReviewsViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            ProfileSavedReviewsViewContent(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                state = ProfileSavedReviewsState(
                    profile = LocalProfileProvider.profile,
                    savedReviews = LocalProfileProvider.savedReviews.mapNotNull { savedReview ->
                        LocalReviewProvider.findById(savedReview.reviewId)
                    }
                ),
                onReviewsClick = {},
                onSavedClick = {},
                onReviewClick = {}
            )
        }
    }
}

/** Muestra las reseñas guardadas en tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ProfileSavedReviewsViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            ProfileSavedReviewsViewContent(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                state = ProfileSavedReviewsState(
                    profile = LocalProfileProvider.profile,
                    savedReviews = LocalProfileProvider.savedReviews.mapNotNull { savedReview ->
                        LocalReviewProvider.findById(savedReview.reviewId)
                    }
                ),
                onReviewsClick = {},
                onSavedClick = {},
                onReviewClick = {}
            )
        }
    }
}
