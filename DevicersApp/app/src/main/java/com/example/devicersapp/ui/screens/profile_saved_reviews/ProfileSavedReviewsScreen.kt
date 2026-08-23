package com.example.devicersapp.ui.screens.profile_saved_reviews

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.SavedReviewContent
import com.example.devicersapp.ui.screens.profile_saved_reviews.components.SavedReviewCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileAvatar
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow
import androidx.compose.foundation.lazy.grid.itemsIndexed

/**
 * Configura la sección de reseñas guardadas dentro del perfil propio.
 *
 * @param modifier Modificador aplicado a la pantalla.
 */
@Composable
fun ProfileSavedReviewsScreen(
    onReviewClick: (Int) -> Unit = {},
    onReviewsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProfileSavedReviewsScreenContent(
        profile = LocalProfileProvider.profile,
        savedReviews = LocalProfileProvider.savedReviews,
        isReviewsSelected = false,
        onReviewsClick = onReviewsClick,
        onSavedClick = {},
        onReviewClick = onReviewClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la cabecera del perfil, sus secciones y la cuadrícula de reseñas guardadas.
 *
 * El encabezado prescinde de las métricas porque esta sección se concentra en el contenido
 * que la persona guardó, no en su alcance dentro de la comunidad.
 *
 * @param profile Información visible del perfil.
 * @param savedReviews Reseñas que el perfil guardó para leer más adelante.
 * @param isReviewsSelected Indica si la sección activa es la de reseñas.
 * @param onReviewsClick Acción que solicita mostrar la sección de reseñas.
 * @param onSavedClick Acción que solicita mostrar la sección de guardados.
 * @param modifier Modificador aplicado a la cuadrícula raíz.
 */
@Composable
fun ProfileSavedReviewsScreenContent(
    profile: ProfileContent,
    savedReviews: List<SavedReviewContent>,
    isReviewsSelected: Boolean,
    onReviewsClick: () -> Unit,
    onSavedClick: () -> Unit,
    onReviewClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = modifier.padding(horizontal = 20.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ProfileAvatar(
                    avatarResId = profile.avatarResId,
                    modifier = Modifier.size(84.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(profile.handleResId),
                    color = LocalDevicersColors.current.textPrimary,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(16.dp))
                SectionTabsRow(
                    startLabelResId = R.string.profile_reviews,
                    endLabelResId = R.string.profile_saved,
                    isStartSelected = isReviewsSelected,
                    onStartClick = onReviewsClick,
                    onEndClick = onSavedClick,
                    selectedColor = LocalDevicersColors.current.primaryText
                )

                Spacer(modifier = Modifier.height(18.dp))
            }
        }

        itemsIndexed(savedReviews) { index, savedReview ->
            SavedReviewCard(
                savedReview = savedReview,
                onClick = {
                    onReviewClick(index)
                }
            )
        }

        item(span = { GridItemSpan(maxLineSpan) }) {
            // Deja aire para que la barra flotante no tape la última tarjeta.
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Muestra una vista previa de las reseñas guardadas en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ProfileSavedReviewsScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1
        ) { innerPadding ->
            ProfileSavedReviewsScreen(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}

/** Muestra una vista previa de las reseñas guardadas en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 1000)
fun ProfileSavedReviewsScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "profile",
            showBottomBar = true,
            topBarNumber = 1
        ) { innerPadding ->
            ProfileSavedReviewsScreen(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding())
            )
        }
    }
}
