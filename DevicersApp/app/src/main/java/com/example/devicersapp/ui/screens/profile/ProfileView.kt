package com.example.devicersapp.ui.screens.profile

import com.example.devicersapp.ui.screens.profile.components.ProfileSkeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileHeader
import com.example.devicersapp.ui.utils.profile.ProfileProductCard
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTab
import com.example.devicersapp.ui.utils.navigation.TopBarStyle

/** Renderiza el detalle de perfil y solicita su carga cuando cambia el identificador. */
@Composable
fun ProfileView(
    profileId: String? = null,
    onReviewClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel,
    onProfileClick: (String) -> Unit = {},
    onFollowersClick: (String) -> Unit = {},
    onFollowingClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.loadProfile(profileId)
    }

    ProfileViewContent(
        onProfileClick = onProfileClick,
        onFollowersClick = onFollowersClick,
        onFollowingClick = onFollowingClick,
        state = uiState,
        onRetry = { viewModel.loadProfile(profileId) },
        onFollowClick = viewModel::onFollowClick,
        onReviewClick = onReviewClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/** Ensambla la cabecera del perfil y la cuadrícula de productos que ya calificó. */
@Composable
fun ProfileViewContent(
    state: ProfileState,
    onFollowClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier,
    onProfileClick: (String) -> Unit = {},
    onFollowersClick: (String) -> Unit = {},
    onFollowingClick: (String) -> Unit = {}
) {
    if (state.loading) { ProfileSkeleton(modifier); return }
    state.error?.let { error ->
        Column(modifier.padding(20.dp)) {
            androidx.compose.material3.Text(stringResource(error), color = LocalDevicersColors.current.textPrimary)
            androidx.compose.material3.TextButton(onClick = onRetry) { androidx.compose.material3.Text(stringResource(R.string.home_feed_retry), color = LocalDevicersColors.current.primaryText) }
        }
        return
    }
    val profile = state.profile ?: return
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column {
                Spacer(modifier = Modifier.height(15.dp))
                ProfileHeader(
                    onProfileClick = onProfileClick,
                    onFollowersClick = onFollowersClick,
                    onFollowingClick = onFollowingClick,
                    profile = profile,
                    actionLabelResId = when {
                        state.isFollowed -> R.string.activity_following
                        state.followsYou -> R.string.profile_follow_back
                        else -> R.string.profile_follow
                    },
                    actionEnabled = !state.followInProgress,
                    actionIsSecondary = state.isFollowed,
                    onActionClick = onFollowClick
                )
            }
        }
        state.followError?.let { error ->
            item(span = { GridItemSpan(maxLineSpan) }) {
                androidx.compose.material3.Text(stringResource(error), color = LocalDevicersColors.current.textSecondary)
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            SectionTab(
                labelResId = R.string.profile_reviews,
                isSelected = true,
                onClick = {},
                selectedColor = LocalDevicersColors.current.primaryText
            )
        }
        if (state.reviews.isEmpty()) item(span = { GridItemSpan(maxLineSpan) }) {
            androidx.compose.material3.Text(stringResource(R.string.public_profile_empty_reviews),
                color = LocalDevicersColors.current.textSecondary, style = androidx.compose.material3.MaterialTheme.typography.bodyMedium)
        }
        itemsIndexed(state.reviews) { _, review ->
            ProfileProductCard(review = review, onClick = { onReviewClick(review.id) })
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Previsualiza el contenido sin autenticar usuarios ni consultar servicios remotos. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProfileViewPreview() {
    val username = stringResource(R.string.username_placeholder)
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "profile", showBottomBar = true, topBarStyle = TopBarStyle.Profile,
            topBarUserHandle = username) { padding ->
            ProfileViewContent(state = ProfileState(loading = true), onFollowClick = {}, onReviewClick = {}, modifier = Modifier.padding(padding))
        }
    }
}

/** Previsualiza el contenido sin autenticar usuarios ni consultar servicios remotos. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProfileViewDarkPreview() {
    val username = stringResource(R.string.username_placeholder)
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "profile", showBottomBar = true, topBarStyle = TopBarStyle.Profile,
            topBarUserHandle = username) { padding ->
            ProfileViewContent(state = ProfileState(loading = true), onFollowClick = {}, onReviewClick = {}, modifier = Modifier.padding(padding))
        }
    }
}
