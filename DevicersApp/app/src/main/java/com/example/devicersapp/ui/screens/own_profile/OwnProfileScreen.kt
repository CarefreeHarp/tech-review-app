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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileHeader
import com.example.devicersapp.ui.utils.profile.ProfileProductCard
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/**
 * Configura el perfil propio, donde además de las reseñas se puede editar la cuenta.
 *
 * @param onEditProfileClick Acción solicitada al editar el perfil.
 * @param onEditAvatarClick Acción solicitada al cambiar la foto de perfil.
 * @param modifier Modificador aplicado a la pantalla.
 */
@Composable
fun OwnProfileScreen(
    onEditProfileClick: () -> Unit = {},
    onEditAvatarClick: () -> Unit = {},
    onReviewClick: (Int) -> Unit = {},
    onSavedReviewsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var isReviewsSelected by remember { mutableStateOf(true) }

    OwnProfileScreenContent(
        profile = LocalProfileProvider.profile,
        reviews = LocalReviewProvider.reviewsForProfile(LocalProfileProvider.profile.id),
        isReviewsSelected = isReviewsSelected,

        onReviewsClick = {
            isReviewsSelected = true
        },

        onSavedClick = {
            onSavedReviewsClick()
        },

        onReviewClick = onReviewClick,
        onEditProfileClick = onEditProfileClick,
        onEditAvatarClick = onEditAvatarClick,

        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la cabecera del perfil propio, sus secciones y la cuadrícula de productos calificados.
 *
 * @param profile Información visible del perfil.
 * @param reviews Reseñas creadas por el perfil.
 * @param isReviewsSelected Indica si la sección activa es la de reseñas.
 * @param onReviewsClick Acción que solicita mostrar la sección de reseñas.
 * @param onSavedClick Acción que solicita mostrar la sección de guardados.
 * @param onEditProfileClick Acción solicitada al editar el perfil.
 * @param onEditAvatarClick Acción solicitada al cambiar la foto de perfil.
 * @param modifier Modificador aplicado a la cuadrícula raíz.
 */
@Composable
fun OwnProfileScreenContent(
    profile: ProfileContent,
    reviews: List<ReviewContent>,
    isReviewsSelected: Boolean,
    onReviewsClick: () -> Unit,
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
        // El encabezado y las secciones ocupan el ancho completo sobre la cuadrícula.
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column()  {
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
                isStartSelected = isReviewsSelected,
                onStartClick = onReviewsClick,
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
            // Deja aire para que la barra flotante no tape la última fila.
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Muestra una vista previa del perfil propio en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun OwnProfileScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            OwnProfileScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}

/** Muestra una vista previa del perfil propio en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1100)
fun OwnProfileScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1,
            topBarUserHandleResId = LocalProfileProvider.profile.handleResId
        ) { innerPadding ->
            OwnProfileScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}
