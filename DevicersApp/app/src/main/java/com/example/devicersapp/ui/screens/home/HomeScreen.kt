package com.example.devicersapp.ui.screens.home

import com.example.devicersapp.ui.theme.LocalDevicersColors

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalHomeScreenProvider
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.screens.home.components.FeedReviewItem
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import com.example.devicersapp.ui.utils.tabs.SectionTabsRow

/**
 * Configura la pantalla principal, donde se leen las últimas reseñas de la comunidad.
 *
 * @param onReviewClick Acción solicitada al abrir el detalle de una reseña.
 * @param onCommentClick Acción solicitada al abrir los comentarios de una reseña.
 * @param onSendClick Acción solicitada al compartir una reseña.
 * @param modifier Modificador aplicado al contenedor de la pantalla.
 */
@Composable
fun HomeScreen(
    onReviewClick: (FeedReviewContent) -> Unit = {},
    onCommentClick: (FeedReviewContent) -> Unit = {},
    onSendClick: (FeedReviewContent) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // La vertiente activa del feed es estado de la pantalla, no de la fila de pestañas.
    var isForYouSelected by remember { mutableStateOf(true) }

    HomeScreenContent(
        reviews = LocalHomeScreenProvider.reviews,
        isForYouSelected = isForYouSelected,
        onForYouClick = { isForYouSelected = true },
        onFollowingClick = { isForYouSelected = false },
        onReviewClick = onReviewClick,
        onCommentClick = onCommentClick,
        onSendClick = onSendClick,
        modifier = modifier
            .fillMaxSize()
            .background(LocalDevicersColors.current.background)
    )
}

/**
 * Muestra las pestañas del feed y la lista de reseñas publicadas.
 *
 * @param reviews Reseñas disponibles para mostrar.
 * @param isForYouSelected Indica si la vertiente activa es la de reseñas sugeridas.
 * @param onForYouClick Acción que solicita mostrar las reseñas sugeridas.
 * @param onFollowingClick Acción que solicita mostrar las reseñas de las cuentas seguidas.
 * @param onReviewClick Acción solicitada al abrir una reseña del feed.
 * @param onCommentClick Acción solicitada al abrir los comentarios de una reseña del feed.
 * @param onSendClick Acción solicitada al compartir una reseña del feed.
 * @param modifier Modificador aplicado a la lista.
 */
@Composable
fun HomeScreenContent(
    reviews: List<FeedReviewContent>,
    isForYouSelected: Boolean,
    onForYouClick: () -> Unit,
    onFollowingClick: () -> Unit,
    onReviewClick: (FeedReviewContent) -> Unit,
    onCommentClick: (FeedReviewContent) -> Unit,
    onSendClick: (FeedReviewContent) -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalDevicersColors.current

    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(8.dp))

            SectionTabsRow(
                startLabelResId = R.string.home_tab_for_you,
                endLabelResId = R.string.home_tab_following,
                isStartSelected = isForYouSelected,
                onStartClick = onForYouClick,
                onEndClick = onFollowingClick
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        itemsIndexed(reviews) { index, review ->

            FeedReviewItem(
                review = review,
                onViewMoreClick = {
                    onReviewClick(review)
                },
                onCommentClick = { onCommentClick(review) },
                onSendClick = { onSendClick(review) }
            )

            // Un divisor separa cada reseña de la siguiente sin encerrarlas en tarjetas.
            if (index < reviews.lastIndex) {
                Spacer(modifier = Modifier.height(20.dp))

                HorizontalDivider(
                    modifier = Modifier.fillMaxWidth(),
                    thickness = 1.dp,
                    color = colors.border
                )

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        item {
            // Deja aire para que la barra flotante no tape la última reseña.
            Spacer(modifier = Modifier.height(110.dp))
        }
    }
}

/** Muestra una vista previa de la pantalla principal en el tema claro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun HomeScreenPreview() {
    DevicersAppTheme(darkTheme = false) {
        DevicersScaffold(
            selectedItem = "home",
            showBottomBar = true,
            topBarNumber = 5
        ) { innerPadding ->
            HomeScreen(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            )
        }
    }
}

/** Muestra una vista previa de la pantalla principal en el tema oscuro. */
@Composable
@Preview(showBackground = true, heightDp = 900)
fun HomeScreenDarkPreview() {
    DevicersAppTheme(darkTheme = true) {
        DevicersScaffold(
            selectedItem = "home",
            showBottomBar = true,
            topBarNumber = 5
        ) { innerPadding ->
            HomeScreen(
                modifier = Modifier.padding(
                    top = innerPadding.calculateTopPadding()
                )
            )
        }
    }
}
