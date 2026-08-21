package com.example.devicersapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileScreenProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.RatedProductContent
import com.example.devicersapp.ui.screens.profile.components.ProfileHeader
import com.example.devicersapp.ui.screens.profile.components.ProfileProductCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/** Configura el perfil con las entidades locales que la pantalla necesita mostrar. */
@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // La sección activa es estado de la pantalla, no de la fila de pestañas.
    var isReviewsSelected by remember { mutableStateOf(true) }

    ProfileScreenContent(
        profile = LocalProfileScreenProvider.profile,
        ratedProducts = LocalProfileScreenProvider.ratedProducts,
        isReviewsSelected = isReviewsSelected,
        onReviewsClick = { isReviewsSelected = true },
        onSavedClick = { isReviewsSelected = false },
        onEditProfileClick = onEditProfileClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la cabecera del perfil y la cuadrícula de productos que ya calificó.
 *
 * @param profile Información visible del perfil.
 * @param ratedProducts Productos calificados por el perfil.
 * @param isReviewsSelected Indica si la sección activa es la de reseñas.
 * @param onReviewsClick Acción que solicita mostrar la sección de reseñas.
 * @param onSavedClick Acción que solicita mostrar la sección de guardados.
 * @param onEditProfileClick Acción solicitada al editar el perfil.
 * @param modifier Modificador aplicado a la cuadrícula raíz.
 */
@Composable
fun ProfileScreenContent(
    profile: ProfileContent,
    ratedProducts: List<RatedProductContent>,
    isReviewsSelected: Boolean,
    onReviewsClick: () -> Unit,
    onSavedClick: () -> Unit,
    onEditProfileClick: () -> Unit,
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
            ProfileHeader(
                profile = profile,
                onEditProfileClick = onEditProfileClick
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            SectionTabsRow(
                startLabelResId = R.string.profile_reviews,
                endLabelResId = R.string.profile_saved,
                isStartSelected = isReviewsSelected,
                onStartClick = onReviewsClick,
                onEndClick = onSavedClick
            )
        }

        items(ratedProducts) { product ->
            ProfileProductCard(product)
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
            topBarNumber = 1
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
            topBarNumber = 1
        ) { innerPadding ->
            ProfileScreen(modifier = Modifier.padding(top = innerPadding.calculateTopPadding()))
        }
    }
}
