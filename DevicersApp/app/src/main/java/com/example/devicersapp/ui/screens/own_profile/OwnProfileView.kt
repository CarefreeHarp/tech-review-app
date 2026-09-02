package com.example.devicersapp.ui.screens.own_profile

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent
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
    onEditProfileClick: () -> Unit = {},
    onEditAvatarClick: () -> Unit = {},
    onReviewClick: (Int) -> Unit = {},
    onSavedReviewsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: OwnProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    uiState.profile?.let { profile ->
        OwnProfileViewContent(
            profile = profile,
            reviews = uiState.reviews,
            onSavedClick = onSavedReviewsClick,
            onReviewClick = onReviewClick,
            onEditProfileClick = onEditProfileClick,
            onEditAvatarClick = onEditAvatarClick,
            modifier = modifier
                .fillMaxSize()
                .background(LocalDevicersColors.current.background)
        )
    }
}

/**
 * Ensambla la cabecera del perfil propio, sus secciones y la cuadrícula de reseñas.
 */
@Composable
fun OwnProfileViewContent(
    profile: ProfileContent,
    reviews: List<ReviewContent>,
    onSavedClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    onEditProfileClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    profile = profile,
                    actionLabelResId = R.string.profile_edit,
                    showEditBadge = true,
                    onActionClick = onEditProfileClick,
                    onEditAvatarClick = onEditAvatarClick
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

        itemsIndexed(reviews) { _, review ->
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

/** Muestra una vista previa del perfil propio en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun OwnProfileViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            OwnProfileView(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                viewModel = OwnProfileViewModel()
            )
        }
    }
}

/** Muestra una vista previa del perfil propio en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun OwnProfileViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            OwnProfileView(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                ),
                viewModel = OwnProfileViewModel()
            )
        }
    }
}
