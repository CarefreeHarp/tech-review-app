package com.example.devicersapp

import android.graphics.Bitmap
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.isDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.ui.screens.home.HomeState
import com.example.devicersapp.ui.screens.home.HomeViewContent
import com.example.devicersapp.ui.screens.found_products.FoundProductsState
import com.example.devicersapp.ui.screens.found_products.FoundProductsViewContent
import com.example.devicersapp.ui.screens.create_review.CreateReviewState
import com.example.devicersapp.ui.screens.create_review.CreateReviewViewContent
import com.example.devicersapp.ui.screens.product.ProductState
import com.example.devicersapp.ui.screens.product.ProductViewContent
import com.example.devicersapp.ui.screens.review.ReviewState
import com.example.devicersapp.ui.screens.review.ReviewViewContent
import com.example.devicersapp.ui.screens.profile.ProfileState
import com.example.devicersapp.ui.screens.profile.ProfileViewContent
import com.example.devicersapp.ui.screens.own_profile.OwnProfileState
import com.example.devicersapp.ui.screens.own_profile.OwnProfileViewContent
import com.example.devicersapp.ui.screens.profile_saved_reviews.ProfileSavedReviewsState
import com.example.devicersapp.ui.screens.profile_saved_reviews.ProfileSavedReviewsViewContent
import com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsState
import com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsViewContent
import com.example.devicersapp.ui.screens.rate_product.RateProductState
import com.example.devicersapp.ui.screens.rate_product.RateProductViewContent
import com.example.devicersapp.ui.screens.edit_review.EditReviewState
import com.example.devicersapp.ui.screens.edit_review.EditReviewViewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.example.devicersapp.ui.utils.scaffold.DevicersScaffold
import java.io.File
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Comprueba la carga sin red, la navegación inmediata y la conservación del formulario al guardar. */
@RunWith(AndroidJUnit4::class)
class LoadingSkeletonUiTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun loadingScreensKeepNavigationInteractiveInBothThemes() {
        val screens = loadingScreens()
        val selected = mutableStateOf(0)
        val darkTheme = mutableStateOf(false)
        var navigation = ""
        var backClicks = 0
        compose.runOnUiThread {
            compose.activity.setContent {
                val screen = screens[selected.value]
                DevicersAppTheme(darkTheme = darkTheme.value) {
                    DevicersScaffold(
                        selectedItem = "home", topBarNumber = screen.topBarNumber,
                        showBottomBar = screen.hasBottomBar,
                        onNavigationItemClick = { navigation = it },
                        onTopBarBackClick = { backClicks++ }
                    ) { padding ->
                        screen.content(Modifier.padding(top = padding.calculateTopPadding()).fillMaxSize())
                    }
                }
            }
        }
        for (dark in listOf(false, true)) {
            for ((index, screen) in screens.withIndex()) {
                compose.runOnIdle { selected.value = index; darkTheme.value = dark }
                compose.onNodeWithContentDescription(text(R.string.screen_loading)).assertIsDisplayed()
                if (screen.hasBottomBar) {
                    navigationNode(R.string.navigation_home).assertIsDisplayed()
                    navigationNode(R.string.navigation_search).performClick()
                    compose.runOnIdle { assertEquals("search", navigation); navigation = "" }
                }
                if (screen.topBarNumber != 5 && screen.topBarNumber != 1) {
                    compose.onNodeWithContentDescription(text(R.string.top_bar_back)).performClick()
                    compose.runOnIdle { assertEquals(1, backClicks); backClicks = 0 }
                }
                saveScreenshot(screen.name + if (dark) "_dark" else "_light")
            }
        }
    }

    @Test
    fun feedReplacesSkeletonAndKeepsTabsAndNavigationVisible() {
        val state = mutableStateOf(HomeState(isLoading = true))
        var followingClicks = 0
        compose.runOnUiThread {
            compose.activity.setContent {
                DevicersAppTheme {
                    DevicersScaffold(topBarNumber = 5, showBottomBar = true) { padding ->
                        HomeViewContent(state.value, onForYouClick = {},
                            onFollowingClick = { followingClicks++ }, onRetryClick = {},
                            onProductClick = {}, onReviewClick = {}, onCommentClick = {}, onSendClick = {},
                            modifier = Modifier.padding(top = padding.calculateTopPadding()).fillMaxSize())
                    }
                }
            }
        }
        compose.onNodeWithText(text(R.string.home_tab_following)).performClick()
        compose.runOnIdle { assertEquals(1, followingClicks); state.value = HomeState() }
        compose.onNodeWithContentDescription(text(R.string.screen_loading)).assertDoesNotExist()
        compose.onNodeWithText(text(R.string.home_feed_empty)).assertIsDisplayed()
        navigationNode(R.string.navigation_home).assertIsDisplayed()
    }

    @Test
    fun profileSectionsCanNavigateBeforeProfileDataArrives() {
        var savedClicks = 0
        compose.runOnUiThread {
            compose.activity.setContent {
                DevicersAppTheme {
                    OwnProfileViewContent(state = OwnProfileState(loading = true),
                        onSavedClick = { savedClicks++ }, onReviewClick = {},
                        onEditProfileClick = {}, onEditAvatarClick = {}, modifier = Modifier.fillMaxSize())
                }
            }
        }
        compose.onNodeWithText(text(R.string.profile_saved)).performScrollTo().performClick()
        compose.runOnIdle { assertEquals(1, savedClicks) }
    }

    @Test
    fun savingKeepsDraftVisibleAndDisablesItsFields() {
        val editing = mutableStateOf(false)
        compose.runOnUiThread {
            compose.activity.setContent {
                DevicersAppTheme {
                    if (editing.value) {
                        EditReviewViewContent(
                            state = EditReviewState(product = LocalProductProvider.product, canEdit = true,
                                title = "Draft title", experience = "Draft body", saving = true),
                            onRetry = {}, onRatingChange = {}, onTitleChange = {}, onExperienceChange = {},
                            onAdvantageChange = {}, onDisadvantageChange = {}, onSave = {},
                            modifier = Modifier.fillMaxSize())
                    } else {
                        RateProductViewContent(
                            state = RateProductState(product = LocalProductProvider.product,
                                title = "Draft title", experience = "Draft body", saving = true),
                            onRatingChange = {}, onTitleChange = {}, onExperienceChange = {},
                            onAdvantageChange = {}, onDisadvantageChange = {}, onChangeProduct = {},
                            onPublishClick = {}, modifier = Modifier.fillMaxSize())
                    }
                }
            }
        }
        for (isEditing in listOf(false, true)) {
            compose.runOnIdle { editing.value = isEditing }
            compose.onNodeWithContentDescription(text(R.string.screen_loading)).assertDoesNotExist()
            compose.onNodeWithText("Draft title").performScrollTo().assertIsDisplayed().assertIsNotEnabled()
            compose.onNodeWithText("Draft body").performScrollTo().assertIsDisplayed().assertIsNotEnabled()
        }
    }

    // El menú lateral también contiene estas etiquetas; selecciona la instancia visible.
    private fun navigationNode(id: Int) = compose.onAllNodesWithText(text(id)).let { nodes ->
        nodes[nodes.fetchSemanticsNodes().indices.first { nodes[it].isDisplayed() }]
    }

    private fun text(id: Int): String = compose.activity.getString(id)

    /** Solo exporta capturas cuando se solicita explícitamente en los argumentos de instrumentación. */
    private fun saveScreenshot(name: String) {
        if (InstrumentationRegistry.getArguments().getString("skeletonScreenshots") != "true") return
        val directory = File(compose.activity.getExternalFilesDir(null), "skeleton-previews").apply { mkdirs() }
        File(directory, "$name.png").outputStream().use { output ->
            compose.onRoot().captureToImage().asAndroidBitmap().compress(Bitmap.CompressFormat.PNG, 100, output)
        }
    }

    private fun loadingScreens(): List<LoadingScreen> = listOf(
        LoadingScreen("home", 5) { modifier ->
            HomeViewContent(HomeState(isLoading = true), {}, {}, {}, {}, {}, {}, {}, modifier)
        },
        LoadingScreen("found_products", 8) { modifier ->
            FoundProductsViewContent(FoundProductsState(isLoading = true), {}, {}, {}, modifier)
        },
        LoadingScreen("create_review", 3) { modifier ->
            CreateReviewViewContent(CreateReviewState(isLoading = true), {}, {}, {}, {}, {}, modifier)
        },
        LoadingScreen("product", 6) { modifier ->
            ProductViewContent(ProductState(isLoading = true), {}, {}, {}, modifier)
        },
        LoadingScreen("review", 4, false) { modifier ->
            ReviewViewContent(ReviewState(loading = true), onReplyTextChange = {}, onProductClick = {},
                onSendReply = {}, modifier = modifier)
        },
        LoadingScreen("profile", 1) { modifier ->
            ProfileViewContent(ProfileState(loading = true), {}, {}, modifier = modifier)
        },
        LoadingScreen("own_profile", 1) { modifier ->
            OwnProfileViewContent(OwnProfileState(loading = true), {}, {}, {}, {}, modifier = modifier)
        },
        LoadingScreen("profile_saved_reviews", 1) { modifier ->
            ProfileSavedReviewsViewContent(ProfileSavedReviewsState(loading = true), {}, {}, {}, modifier = modifier)
        },
        LoadingScreen("profile_search_results", 7) { modifier ->
            ProfileSearchResultsViewContent(ProfileSearchResultsState(loading = true), onSearchTextChange = {},
                onFollow = {}, onProfileClick = {}, modifier = modifier)
        },
        LoadingScreen("rate_product", 2, false) { modifier ->
            RateProductViewContent(RateProductState(loading = true), onRatingChange = {}, onTitleChange = {},
                onExperienceChange = {}, onAdvantageChange = {}, onDisadvantageChange = {},
                onChangeProduct = {}, onPublishClick = {}, modifier = modifier)
        },
        LoadingScreen("edit_review", 11, false) { modifier ->
            EditReviewViewContent(EditReviewState(loading = true), {}, {}, {}, {}, {}, {}, {}, modifier)
        }
    )
}

/** Describe una pantalla de carga para comprobar la misma estructura compartida que usa la navegación. */
private data class LoadingScreen(
    val name: String,
    val topBarNumber: Int,
    val hasBottomBar: Boolean = true,
    val content: @Composable (Modifier) -> Unit
)
