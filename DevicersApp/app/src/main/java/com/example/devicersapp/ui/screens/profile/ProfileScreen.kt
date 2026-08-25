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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.utils.profile.ProfileHeader
import com.example.devicersapp.ui.utils.profile.ProfileProductCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTab

/** Configura el perfil con las entidades locales que la pantalla necesita mostrar. */
@Composable
fun ProfileScreen(
    profileId: String? = null,
    onFollowClick: () -> Unit = {},
    onReviewClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val selectedProfile = profileId?.let {
        LocalProfileProvider.getPublicProfileById(it)
    } ?: LocalProfileProvider.profile

    ProfileScreenContent(
        profile = selectedProfile,
        reviews = LocalReviewProvider.reviewsForProfile(selectedProfile.id),
        onFollowClick = onFollowClick,
        onReviewClick = onReviewClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la cabecera del perfil y la cuadrícula de productos que ya calificó.
 *
 * @param profile Información visible del perfil.
 * @param reviews Reseñas creadas por el perfil.
 * @param onFollowClick Acción solicitada al seguir a la persona del perfil.
 * @param modifier Modificador aplicado a la cuadrícula raíz.
 */
@Composable
fun ProfileScreenContent(
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
        // El encabezado y las pestañas ocupan el ancho completo sobre la cuadrícula.
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column() {
                Spacer(modifier = Modifier.height(15.dp))
                ProfileHeader(
                    profile = profile,
                    actionLabelResId = R.string.profile_follow,
                    onActionClick = onFollowClick
                )
            }
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            // El perfil ajeno solo expone sus reseñas, así que la sección se muestra acentuada.
            SectionTab(
                labelResId = R.string.profile_reviews,
                isSelected = true,
                onClick = {},
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
            // Deja aire para que la barra flotante no tape la última fila.
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Muestra una vista previa del perfil en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProfileScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            ProfileScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}

/** Muestra una vista previa del perfil en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun ProfileScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            ProfileScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}
