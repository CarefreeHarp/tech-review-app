package com.example.devicersapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.devicersapp.data.datasource.services.ProductRetrofitService
import com.example.devicersapp.data.datasource.services.ReviewRetrofitService
import com.example.devicersapp.data.datasource.services.UsersRetrofitService
import com.example.devicersapp.data.dto.CreateReviewRequestDto
import com.example.devicersapp.data.dto.UpdateReviewRequestDto
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Temporary end-to-end check against the isolated Devicers API on port 3001. */
@RunWith(AndroidJUnit4::class)
class BackendFlowInstrumentedTest {
    @Test
    fun profileAndReviewCrudFromEmulator() = runBlocking {
        // Normal device tests do not depend on a locally running backend.
        assumeTrue(BuildConfig.DEVICERS_API_BASE_URL.endsWith(":3001/"))
        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.DEVICERS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val users = retrofit.create(UsersRetrofitService::class.java)
        val products = retrofit.create(ProductRetrofitService::class.java)
        val reviews = retrofit.create(ReviewRetrofitService::class.java)

        val user = users.getUserById(2)
        val selectedReviews = reviews.getReviewsByUser(2)
        assertEquals(2, user.id)
        assertTrue(selectedReviews.isNotEmpty())
        assertTrue(selectedReviews.all { it.user_id == 2 })

        val product = products.getProducts().first()
        val originalIds = reviews.getReviewsByUser(1).map { it.id }.toSet()
        var createdId: Int? = null
        try {
            val created = reviews.createReview(
                CreateReviewRequestDto(1, product.id, 4, "Prueba desde Android", "Temporal")
            )
            createdId = created.id
            assertEquals(1, created.user_id)
            assertEquals(product.id, created.article_id)
            assertTrue(reviews.getReviewsByUser(1).any { it.id == created.id })

            val updated = reviews.updateReview(
                created.id,
                UpdateReviewRequestDto(rating = 5, title = "Temporal editada", body = "Editada desde Android")
            )
            assertEquals(5, updated.rating)
            assertEquals("Editada desde Android", updated.body)
        } finally {
            createdId?.let { reviews.deleteReview(it) }
        }
        assertEquals(originalIds, reviews.getReviewsByUser(1).map { it.id }.toSet())
    }
}
