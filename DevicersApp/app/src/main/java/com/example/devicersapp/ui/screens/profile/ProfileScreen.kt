package com.example.devicersapp.ui.screens.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.devicersapp.R
import com.example.devicersapp.data.local.LocalUserReviewsProvider
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.screens.profile.components.ProfileReviewCard
import com.example.devicersapp.ui.screens.profile.components.ProfileStats
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.navigation.AppTopBar
import com.example.devicersapp.ui.utils.navigation.BottomNavigationBar

/**
 * Pantalla principal del perfil.
 *
 * Obtiene las reseñas directamente desde FakeDatabase.
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    onEditProfileClick: () -> Unit = {},
    onCreateReviewClick: () -> Unit = {}
) {

    val profileHandle = stringResource(R.string.profile_handle)

    val reviews = LocalUserReviewsProvider.reviews.map { storedReview ->
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            author = profileHandle,
            rating = storedReview.rating,
            text = storedReview.text,
            likes = 0
        )
    }

    val profile = ProfileContent(
        avatarResId = R.drawable.profile_avatar_00,
        name = stringResource(R.string.profile_name),
        handle = profileHandle,
        biography = stringResource(R.string.profile_biography),
        stats = listOf(
            ProfileStatContent(
                number = reviews.size.toString(),
                label = stringResource(R.string.profile_reviews)
            ),
            ProfileStatContent(
                number = "245",
                label = stringResource(R.string.profile_followers)
            ),
            ProfileStatContent(
                number = "180",
                label = stringResource(R.string.profile_following)
            )
        )
    )

    ProfileScreenContent(
        profile = profile,
        reviews = reviews,
        onEditProfileClick = onEditProfileClick,
        modifier = modifier
    )
}

/**
 * Contenido visual de la pantalla de perfil.
 */
@Composable
fun ProfileScreenContent(
    profile: ProfileContent,
    reviews: List<ReviewContent>,
    onEditProfileClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets.safeDrawing,

        topBar = {
            AppTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
            )
        },

        bottomBar = {
            BottomNavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding(),
                selectedItem = "profile",
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 12.dp)
        ) {

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            ProfileStats(
                profile = profile,
                onEditProfileClick = onEditProfileClick
            )

            Spacer(
                modifier = Modifier.height(28.dp)
            )

            Text(
                text = stringResource(R.string.profile_my_reviews),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            if (reviews.isEmpty()) {

                Text(
                    text = "Aún no has publicado reseñas.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(
                    modifier = Modifier.height(24.dp)
                )

            } else {

                reviews.forEach { review ->

                    ProfileReviewCard(
                        review = review
                    )

                    Spacer(
                        modifier = Modifier.height(12.dp)
                    )
                }
            }

            Spacer(
                modifier = Modifier.height(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenLightPreview() {
    DevicersAppTheme(
        darkTheme = false
    ) {
        ProfileScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenDarkPreview() {
    DevicersAppTheme(
        darkTheme = true
    ) {
        ProfileScreen()
    }
}