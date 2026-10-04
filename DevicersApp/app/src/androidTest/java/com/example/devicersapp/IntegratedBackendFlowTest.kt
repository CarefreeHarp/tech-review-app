package com.example.devicersapp

import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.devicersapp.data.datasource.implementations.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.screens.product.ProductViewModel
import com.example.devicersapp.ui.screens.profile.ProfileViewModel
import com.example.devicersapp.ui.screens.rate_product.RateProductViewModel
import com.example.devicersapp.ui.screens.review.ReviewViewModel
import com.example.devicersapp.ui.screens.remote_reviews.RemoteReviewsViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert.*
import org.junit.Assume.assumeTrue
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Exercises the shared task 4 repositories through the task 6/7 screen models. */
@RunWith(AndroidJUnit4::class)
class IntegratedBackendFlowTest {
    @Test
    fun productProfileAndCrudShareBackendIds() = runBlocking {
        assumeTrue(BuildConfig.DEVICERS_API_BASE_URL.endsWith(":3001/"))
        val retrofit = Retrofit.Builder().baseUrl(BuildConfig.DEVICERS_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val reviews = ReviewRepository(ReviewRetrofitDataSourceImplementation(retrofit.create(ReviewRetrofitService::class.java)))
        val products = ProductRepository(ProductRetrofitDataSourceImplementation(retrofit.create(ProductRetrofitService::class.java)))
        val users = UsersRepository(UsersRetrofitDataSourceImplementation(retrofit.create(UsersRetrofitService::class.java)))
        val originalIds = reviews.getReviewsByUser(CURRENT_USER_ID).getOrThrow().map { it.id }.toSet()
        val productId = products.getProducts().getOrThrow().first().id
        val marker = "Integration " + System.currentTimeMillis()
        var createdId: Int? = null
        val store = ViewModelStore()
        try {
            withContext(Dispatchers.Main) {
                withTimeout(30000) {
                    val profile = ProfileViewModel(users, reviews).also { store.put("profile", it) }
                    profile.loadProfile("2")
                    val selected = profile.uiState.first { !it.loading }
                    assertNull(selected.error)
                    assertEquals("2", selected.profile?.id)
                    assertTrue(selected.reviews.isNotEmpty())
                    assertTrue(selected.reviews.all { it.authorId == "2" })

                    val product = ProductViewModel(products, reviews).also { store.put("product", it) }
                    product.loadProduct(productId)
                    val productState = product.uiState.first { !it.isLoading }
                    assertNull(productState.errorMessage)
                    assertEquals(productId, productState.product?.id)

                    val rate = RateProductViewModel(products, reviews).also { store.put("rate", it) }
                    rate.loadProduct(productState.product!!.id)
                    assertNull(rate.uiState.first { !it.loading }.error)
                    rate.onRatingChange(4)
                    rate.onTitleChange(marker)
                    rate.onExperienceChange(marker)
                    rate.publish()
                    val published = rate.uiState.first { !it.saving }
                    assertNull(published.error)
                    assertTrue(published.published)

                    val created = reviews.getReviewsByUser(CURRENT_USER_ID).getOrThrow().single { it.title == marker }
                    createdId = created.id
                    assertEquals(productId, created.articleId)
                    val detail = ReviewViewModel(reviews, products, users).also { store.put("detail", it) }
                    detail.loadReview(created.id)
                    val detailState = detail.uiState.first { !it.loading }
                    assertNull(detailState.error)
                    assertEquals(productId, detailState.product?.id)
                    assertEquals(CURRENT_USER_ID.toString(), detailState.review?.authorId)

                    val manage = RemoteReviewsViewModel(reviews, products).also { store.put("manage", it) }
                    manage.load()
                    assertNull(manage.uiState.first { !it.loading }.error)
                    manage.edit(manage.uiState.value.reviews.single { it.id == created.id })
                    manage.body("Edited " + marker)
                    manage.rating(5)
                    manage.save()
                    assertNull(manage.uiState.first { !it.saving }.error)
                    val updated = reviews.getReviewById(created.id).getOrThrow()
                    assertEquals(5, updated.rating)
                    assertEquals("Edited " + marker, updated.body)
                    product.loadProduct(productId)
                    assertTrue(product.uiState.first { !it.isLoading }.reviews.any { it.id == created.id && it.rating == 5 })
                    manage.requestDelete(updated)
                    manage.delete()
                    assertNull(manage.uiState.first { !it.saving }.error)
                    assertFalse(manage.uiState.value.reviews.any { it.id == created.id })
                    createdId = null
                    product.loadProduct(productId)
                    assertFalse(product.uiState.first { !it.isLoading }.reviews.any { it.id == created.id })
                }
            }
            assertEquals(originalIds, reviews.getReviewsByUser(CURRENT_USER_ID).getOrThrow().map { it.id }.toSet())
        } finally {
            withContext(Dispatchers.Main) { store.clear() }
            createdId?.let { reviews.deleteReview(it).getOrThrow() }
        }
    }
}
