package com.example.devicersapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.devicersapp.data.datasource.services.*
import kotlinx.coroutines.runBlocking
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(AndroidJUnit4::class)
class IntegratedNavigationTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun profileReviewProductAndRateUseTheSameArticle() {
        assumeTrue(BuildConfig.DEVICERS_API_BASE_URL.endsWith(":3001/"))
        assumeTrue(BuildConfig.DEVICERS_TEST_START_DESTINATION == "profile-search-results")
        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.DEVICERS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val users = retrofit.create(UsersRetrofitService::class.java)
        val reviews = retrofit.create(ReviewRetrofitService::class.java)
        val products = retrofit.create(ProductRetrofitService::class.java)
        val user = runBlocking { users.getUserById(2) }
        val review = runBlocking { reviews.getReviewsByUser(user.id).first() }
        val product = runBlocking { products.getProductById(review.article_id) }
        awaitText(user.username)
        compose.onNodeWithText(user.username).performClick()
        awaitText(product.name)
        compose.onNodeWithText(product.name).performScrollTo().performClick()
        awaitText(review.body)
        compose.onNodeWithText(product.name).performClick()
        val rateText = compose.activity.getString(R.string.product_rate)
        awaitText(rateText)
        compose.onNodeWithText(rateText).performScrollTo().performClick()
        val titleText = compose.activity.getString(R.string.rate_product_review_title)
        awaitText(titleText)
        compose.onNodeWithText(product.name).assertExists()
    }

    private fun awaitText(text: String) {
        compose.waitUntil(20000) {
            compose.onAllNodesWithText(text).fetchSemanticsNodes().isNotEmpty()
        }
    }
}
