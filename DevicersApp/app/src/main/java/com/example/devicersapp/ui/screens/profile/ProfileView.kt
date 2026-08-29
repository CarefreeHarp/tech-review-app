package com.example.devicersapp.ui.screens.profile

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
import androidx.compose.runtime.LaunchedEffect
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
import com.example.devicersapp.ui.utils.tabs.SectionTab

/** Renderiza el detalle de perfil y solicita su carga cuando cambia el identificador. */
@Composable
fun ProfileView(
    profileId: String? = null,
    onFollowClick: () -> Unit = {},
    onReviewClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadProfile(profileId)
    }

    uiState.profile?.let { profile ->
        ProfileViewContent(
            profile = profile,
            reviews = uiState.reviews,
            onFollowClick = onFollowClick,
            onReviewClick = onReviewClick,
            modifier = modifier
                .fillMaxSize()
                .background(LocalDevicersColors.current.background)
        )
    }
}

/** Ensambla la cabecera del perfil y la cuadrícula de productos que ya calificó. */
@Composable
fun ProfileViewContent(
    profile: ProfileContent,
    reviews: List<ReviewContent>,
    onFollowClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
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
                    actionLabelResId = R.string.profile_follow,
                    onActionClick = onFollowClick
                )
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
        itemsIndexed(reviews) { _, review ->
            ProfileProductCard(review = review, onClick = { onReviewClick(review.id) })
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Muestra el detalle de perfil en tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProfileViewPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            ProfileView(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                viewModel = ProfileViewModel()
            )
        }
    }
}

/** Muestra el detalle de perfil en tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProfileViewDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            ProfileView(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                viewModel = ProfileViewModel()
            )
        }
    }
}
