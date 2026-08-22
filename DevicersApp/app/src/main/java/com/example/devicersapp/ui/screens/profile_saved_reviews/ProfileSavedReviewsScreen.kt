package com.example.devicersapp.ui.screens.profile_saved_reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.SavedReviewContent
import com.example.devicersapp.ui.screens.profile_saved_reviews.components.SavedReviewCard
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.profile.ProfileHeader
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/**
 * Configura la sección de reseñas guardadas dentro del perfil propio.
 *
 * @param onEditProfileClick Acción solicitada al editar el perfil.
 * @param onEditAvatarClick Acción solicitada al cambiar la foto de perfil.
 * @param modifier Modificador aplicado a la pantalla.
 */
@Composable
fun ProfileSavedReviewsScreen(
    onEditProfileClick: () -> Unit = {},
    onEditAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    // La sección activa es estado de la pantalla; aquí se entra directamente en los guardados.
    var isReviewsSelected by remember { mutableStateOf(false) }

    ProfileSavedReviewsScreenContent(
        profile = LocalProfileProvider.profile,
        savedReviews = LocalProfileProvider.savedReviews,
        isReviewsSelected = isReviewsSelected,
        onReviewsClick = { isReviewsSelected = true },
        onSavedClick = { isReviewsSelected = false },
        onEditProfileClick = onEditProfileClick,
        onEditAvatarClick = onEditAvatarClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la cabecera del perfil, sus secciones y la lista de reseñas guardadas.
 *
 * El encabezado prescinde de las métricas porque esta sección se concentra en el contenido
 * que la persona guardó, no en su alcance dentro de la comunidad.
 *
 * @param profile Información visible del perfil.
 * @param savedReviews Reseñas que el perfil guardó para leer más adelante.
 * @param isReviewsSelected Indica si la sección activa es la de reseñas.
 * @param onReviewsClick Acción que solicita mostrar la sección de reseñas.
 * @param onSavedClick Acción que solicita mostrar la sección de guardados.
 * @param onEditProfileClick Acción solicitada al editar el perfil.
 * @param onEditAvatarClick Acción solicitada al cambiar la foto de perfil.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun ProfileSavedReviewsScreenContent(
    profile: ProfileContent,
    savedReviews: List<SavedReviewContent>,
    isReviewsSelected: Boolean,
    onReviewsClick: () -> Unit,
    onSavedClick: () -> Unit,
    onEditProfileClick: () -> Unit,
    onEditAvatarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier.padding(horizontal = 20.dp)) {
        item {
            ProfileHeader(
                profile = profile,
                actionLabelResId = R.string.profile_edit,
                showEditBadge = true,
                showStats = false,
                onActionClick = onEditProfileClick,
                onEditAvatarClick = onEditAvatarClick
            )
            Spacer(modifier = Modifier.height(24.dp))
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

        items(savedReviews) { savedReview ->
            SavedReviewCard(savedReview = savedReview)
            Spacer(modifier = Modifier.height(14.dp))
        }

        item {
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
