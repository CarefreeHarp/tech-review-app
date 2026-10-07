package com.example.devicersapp

import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.platform.testTag
import com.example.devicersapp.ui.screens.own_profile.OwnProfileState
import com.example.devicersapp.ui.screens.own_profile.OwnProfileViewContent
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.devicersapp.data.local.LocalProductProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.ui.screens.edit_review.EditReviewState
import com.example.devicersapp.ui.screens.edit_review.EditReviewViewContent
import com.example.devicersapp.ui.screens.review.ReviewState
import com.example.devicersapp.ui.screens.review.ReviewViewContent
import com.example.devicersapp.ui.theme.DevicersAppTheme
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Comprueba las acciones visibles de la reseña propia y el formulario de edición en Compose. */
@RunWith(AndroidJUnit4::class)
class ReviewEditingUiTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun ownReviewMenuDispatchesSelectedReviewAndIsHiddenForOtherAuthors() {
        val review = LocalReviewProvider.productReviews.first().copy(id = 42, authorId = "1", authorName = "Owner", timeAgoResId = null)
        val state = mutableStateOf(ReviewState(
            product = LocalProductProvider.product, review = review, canManage = true
        ))
        var editedId: Int? = null
        var deleteCalls = 0
        compose.runOnUiThread {
            compose.activity.setContent {
                DevicersAppTheme {
                    ReviewViewContent(
                        state = state.value,
                        onActionsMenuChange = { state.value = state.value.copy(actionsMenuExpanded = it) },
                        onEditClick = { editedId = it; state.value = state.value.copy(actionsMenuExpanded = false) },
                        onDeleteClick = { deleteCalls++; state.value = state.value.copy(actionsMenuExpanded = false) },
                        onReplyTextChange = {}, onProductClick = {}, onSendReply = {},
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        val options = compose.activity.getString(R.string.review_actions_menu)
        val edit = compose.activity.getString(R.string.review_action_edit)
        val delete = compose.activity.getString(R.string.review_action_delete)
        compose.onNodeWithContentDescription(options).performScrollTo()
        val authorCenter = compose.onNodeWithText("Owner").fetchSemanticsNode().boundsInRoot.center.y
        val optionsCenter = compose.onNodeWithContentDescription(options).fetchSemanticsNode().boundsInRoot.center.y
        assertEquals(authorCenter, optionsCenter, 1f)
        compose.onNodeWithContentDescription(options).performClick()
        compose.onNodeWithText(edit).assertExists().performClick()
        compose.runOnIdle { assertEquals(42, editedId) }
        compose.onNodeWithContentDescription(options).performClick()
        compose.onNodeWithText(delete).assertExists().performClick()
        compose.runOnIdle {
            assertEquals(1, deleteCalls)
            state.value = state.value.copy(review = review.copy(authorId = "2"), canManage = false)
        }
        compose.onNodeWithContentDescription(options).assertDoesNotExist()
    }

    @Test
    fun editFormShowsExistingValuesAndSavesTheEditedText() {
        val state = mutableStateOf(EditReviewState(
            reviewId = 42, product = LocalProductProvider.product, canEdit = true,
            rating = 4, title = "Existing title", experience = "Existing experience",
            advantage = "Existing advantage", disadvantage = "Existing disadvantage"
        ))
        var saved: EditReviewState? = null
        compose.runOnUiThread {
            compose.activity.setContent {
                DevicersAppTheme {
                    EditReviewViewContent(
                        state = state.value, onRetry = {},
                        onRatingChange = { state.value = state.value.copy(rating = it) },
                        onTitleChange = { state.value = state.value.copy(title = it) },
                        onExperienceChange = { state.value = state.value.copy(experience = it) },
                        onAdvantageChange = { state.value = state.value.copy(advantage = it) },
                        onDisadvantageChange = { state.value = state.value.copy(disadvantage = it) },
                        onSave = { saved = state.value }, modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        compose.onNodeWithText("Existing title").performScrollTo().assertExists()
        compose.onNodeWithText("Existing experience").performScrollTo().performTextReplacement("Updated experience")
        compose.onNodeWithText("Existing advantage").performScrollTo().assertExists()
        compose.onNodeWithText("Existing disadvantage").assertExists()
        compose.onNodeWithText(compose.activity.getString(R.string.rate_product_publish)).assertDoesNotExist()
        compose.onNodeWithText(compose.activity.getString(R.string.edit_review_save)).performScrollTo().performClick()
        compose.runOnIdle {
            assertEquals("Updated experience", saved?.experience)
            assertEquals("Existing title", saved?.title)
            assertEquals(4, saved?.rating)
            assertEquals(42, saved?.reviewId)
        }
    }
    @Test
    fun reviewAndOwnProfileAndEditingLoadUseSkeletonsInsteadOfSpinners() {
        fun render(content: @androidx.compose.runtime.Composable () -> Unit) {
            compose.runOnUiThread {
                compose.activity.setContent {
                    DevicersAppTheme { Box(Modifier.fillMaxSize().testTag("loading-area")) { content() } }
                }
            }
            compose.onNodeWithContentDescription(compose.activity.getString(R.string.screen_loading))
                .assertIsDisplayed()
            compose.onAllNodes(hasProgressBarRangeInfo(androidx.compose.ui.semantics.ProgressBarRangeInfo.Indeterminate))
                .assertCountEquals(0)
        }
        render {
            ReviewViewContent(state = ReviewState(loading = true),
                onReplyTextChange = {}, onProductClick = {}, onSendReply = {})
        }
        render {
            OwnProfileViewContent(state = OwnProfileState(loading = true, deleting = true),
                onSavedClick = {}, onReviewClick = {}, onEditProfileClick = {}, onEditAvatarClick = {})
        }
        render {
            EditReviewViewContent(state = EditReviewState(loading = true, canEdit = true),
                onRetry = {}, onRatingChange = {}, onTitleChange = {}, onExperienceChange = {},
                onAdvantageChange = {}, onDisadvantageChange = {}, onSave = {})
        }
    }

}
