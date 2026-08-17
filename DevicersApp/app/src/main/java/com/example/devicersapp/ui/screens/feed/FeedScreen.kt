package com.example.devicersapp.ui.screens.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalFeedReviewsProvider
import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.screens.feed.components.ReviewBox
import com.example.devicersapp.ui.theme.SearchHeadingText
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar

/** Configura la estructura del Feed y la navegación inferior. */
@Composable
fun FeedScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = colorResource(R.color.background_light),
        bottomBar = {
            BottomNavigationBar(
                selectedItem = "home",
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        FeedScreenContent(
            reviews = LocalFeedReviewsProvider.reviews,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}

/**
 * Muestra el encabezado y la lista de reseñas del Feed.
 *
 * @param reviews Reseñas disponibles para mostrar.
 * @param modifier Modificador aplicado a la lista.
 */
@Composable
fun FeedScreenContent(
    reviews: List<FeedReviewContent>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
            item {

                Image(
                    painter = painterResource(R.drawable.logo_claro),
                    contentDescription = stringResource(R.string.devicers_logo_description),
                    modifier = Modifier.size(width = 186.dp, height = 50.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = stringResource(R.string.feed),
                    modifier = Modifier.fillMaxWidth(),
                    color = colorResource(R.color.text_primary_light),
                    style = SearchHeadingText
                )

                Spacer(modifier = Modifier.height(6.dp))

                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Box(
                        modifier = Modifier
                            .width(50.dp)
                            .height(2.dp)
                            .background(colorResource(R.color.primary_yellow))
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))
            }

            items(reviews) { review ->
                ReviewBox(review = review)

                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
    }
}

/** Muestra una vista previa de la composición completa del Feed. */
@Composable
@Preview(showBackground = true)
fun FeedScreenPreview() {
    FeedScreen()
}
