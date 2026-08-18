package com.example.devicersapp.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalProfileScreenProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.profile.components.ProfileReviewCard
import com.example.devicersapp.ui.screens.profile.components.ProfileStats
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.theme.LocalDevicersColors
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold

/** Configura el perfil con las entidades locales que la pantalla necesita mostrar. */
@Composable
fun ProfileScreen(
    onEditProfileClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ProfileScreenContent(
        profile = LocalProfileScreenProvider.profile,
        reviews = LocalProfileScreenProvider.reviews,
        onEditProfileClick = onEditProfileClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Ensambla la cabecera del perfil y su colección de reseñas.
 *
 * @param profile Información visible del perfil.
 * @param reviews Reseñas locales publicadas por el perfil.
 * @param onEditProfileClick Acción solicitada al editar el perfil.
 * @param modifier Modificador aplicado a la lista raíz.
 */
@Composable
fun ProfileScreenContent(
    profile: ProfileContent,
    reviews: List<ReviewContent>,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 12.dp)
    ) {
        item {
            // Conserva la separación visual entre la barra compartida y el bloque de perfil.
            Spacer(modifier = Modifier.height(16.dp))
            ProfileStats(
                profile = profile,
                onEditProfileClick = onEditProfileClick
            )
            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = stringResource(R.string.profile_my_reviews),
                style = MaterialTheme.typography.titleMedium,
                color = LocalDevicersColors.current.textPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
        items(reviews) { review ->
            ProfileReviewCard(review)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

/** Muestra una vista previa de la composición completa del perfil. */
@Composable
@Preview(showBackground = true)
fun ProfileScreenPreview() {
    DevicersAppTheme {
        DevicersScaffold(selectedItem = "profile", showBottomBar = true, topBarNumber = 1) { innerPadding ->
            ProfileScreen(modifier = Modifier.padding(innerPadding))
        }
    }
}
