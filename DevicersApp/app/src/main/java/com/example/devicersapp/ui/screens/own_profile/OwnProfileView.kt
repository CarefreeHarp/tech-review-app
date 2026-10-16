package com.example.devicersapp.ui.screens.own_profile

import com.example.devicersapp.ui.screens.own_profile.components.OwnProfileSkeleton
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
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.Lifecycle
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import com.example.devicersapp.ui.utils.profile.ProfileLoadStatus
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileHeader
import com.example.devicersapp.ui.utils.profile.ProfileProductCard
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/**
 * Configura el perfil propio y observa su estado desde el ViewModel.
 */
@Composable
fun OwnProfileView(
    onReviewClick: (Int) -> Unit = {},
    onSavedReviewsClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: OwnProfileViewModel,
    onProfileClick: (String) -> Unit = {},
    onFollowersClick: (String) -> Unit = {},
    onFollowingClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.onResume() }

    OwnProfileViewContent(
        onProfileClick = onProfileClick,
        onFollowersClick = onFollowersClick,
        onFollowingClick = onFollowingClick,
        state = uiState,
        onRetry = viewModel::loadProfile,
        onSavedClick = onSavedReviewsClick,
        onEditProfileClick = onEditProfileClick,
        onReviewClick = onReviewClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la cabecera del perfil propio, sus secciones y la cuadrícula de reseñas.
 */
@Composable
fun OwnProfileViewContent(
    state: OwnProfileState,
    onSavedClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    onEditProfileClick: () -> Unit = {},
    onRetry: () -> Unit = {},
    modifier: Modifier = Modifier,
    onProfileClick: (String) -> Unit = {},
    onFollowersClick: (String) -> Unit = {},
    onFollowingClick: (String) -> Unit = {}
) {
    if (state.loading) {
        OwnProfileSkeleton(onSavedClick = onSavedClick, modifier = modifier)
        return
    }
    if (state.errorMessageResId != null) {
        ProfileLoadStatus(state.errorMessageResId, onRetry, modifier)
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
                    actionLabelResId = R.string.profile_edit,
                    displayName = state.displayName,
                    email = state.email,
                    profileImageUrl = state.profileImageUrl,
                    onActionClick = onEditProfileClick,
                    showDisplayName = false
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            SectionTabsRow(
                startLabelResId = R.string.profile_reviews,
                endLabelResId = R.string.profile_saved,

                // Esta pantalla siempre representa la sección de reseñas.
                // "Guardados" navega a otro destino.
                isStartSelected = true,

                onStartClick = {},
                onEndClick = onSavedClick,
                selectedColor = LocalDevicersColors.current.primaryText
            )
        }

        if (state.reviews.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                Text(stringResource(R.string.own_profile_empty_reviews),
                    color = LocalDevicersColors.current.textSecondary,
                    style = MaterialTheme.typography.bodyMedium)
            }
        }
        itemsIndexed(state.reviews) { _, review ->
            ProfileProductCard(
                review = review,
                onClick = {
                    onReviewClick(review.id)
                }
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Previsualiza el contenido sin autenticar usuarios ni consultar servicios remotos. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun OwnProfileViewPreview() {
    val username = stringResource(R.string.username_placeholder)
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(selectedItem = "profile", showBottomBar = true, topBarNumber = 1,
            topBarUserHandle = username) { padding ->
            OwnProfileViewContent(state = OwnProfileState(loading = true), onSavedClick = {}, onReviewClick = {}, modifier = Modifier.padding(padding))
        }
    }
}

/** Previsualiza el contenido sin autenticar usuarios ni consultar servicios remotos. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun OwnProfileViewDarkPreview() {
    val username = stringResource(R.string.username_placeholder)
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(selectedItem = "profile", showBottomBar = true, topBarNumber = 1,
            topBarUserHandle = username) { padding ->
            OwnProfileViewContent(state = OwnProfileState(loading = true), onSavedClick = {}, onReviewClick = {}, modifier = Modifier.padding(padding))
        }
    }
}
