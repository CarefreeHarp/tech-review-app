package com.example.devicersapp

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.devicersapp.data.local.LocalReviewProvider
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeLocalReviewNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun showMoreOpensTheSelectedLocalReview() {
        assumeTrue(BuildConfig.DEVICERS_TEST_START_DESTINATION == "home")
        val showMore = compose.activity.getString(R.string.review_show_more)
        val review = LocalReviewProvider.reviews.first()
        val reviewText = compose.activity.getString(review.textResId)
        val detailTitle = compose.activity.getString(R.string.review_title)

        compose.waitUntil(15000) {
            compose.onAllNodesWithText(showMore).fetchSemanticsNodes().isNotEmpty()
        }
        compose.onAllNodesWithText(showMore).onFirst().performClick()
        compose.waitUntil(15000) {
            compose.onAllNodesWithText(detailTitle).fetchSemanticsNodes().isNotEmpty() &&
                compose.onAllNodesWithText(showMore).fetchSemanticsNodes().isEmpty()
        }
        compose.onNodeWithText(reviewText).assertExists()
    }
}
