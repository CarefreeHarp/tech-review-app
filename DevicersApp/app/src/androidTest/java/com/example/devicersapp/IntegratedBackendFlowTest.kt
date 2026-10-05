package com.example.devicersapp

import com.example.devicersapp.domain.usecase.*

import com.example.devicersapp.core.config.CURRENT_USER_ID

import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.devicersapp.data.datasource.implementations.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.screens.product.ProductViewModel
import com.example.devicersapp.ui.screens.profile.ProfileViewModel
import com.example.devicersapp.ui.screens.rate_product.RateProductViewModel
import com.example.devicersapp.ui.screens.review.ReviewViewModel
import com.example.devicersapp.ui.screens.edit_review.EditReviewViewModel
import com.example.devicersapp.ui.screens.own_profile.OwnProfileViewModel
import com.example.devicersapp.data.datasource.AuthRemoteDataSource
import com.example.devicersapp.data.datasource.StorageRemoteDataSource
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
        val productSource = ProductRetrofitDataSourceImplementation(retrofit.create(ProductRetrofitService::class.java))
        val reviews = ReviewRepository(ReviewRetrofitDataSourceImplementation(retrofit.create(ReviewRetrofitService::class.java)))
        val products = ProductRepository(productSource)
        val users = UsersRepository(UsersRetrofitDataSourceImplementation(retrofit.create(UsersRetrofitService::class.java)))
        val follows = FollowRepository(FollowRetrofitDataSourceImplementation(retrofit.create(FollowRetrofitService::class.java)))
        val content = ReviewContentUseCase(products, users,
            CommentRepository(CommentRetrofitDataSourceImplementation(retrofit.create(CommentRetrofitService::class.java))),
            ReviewLikeRepository(ReviewLikeRetrofitDataSourceImplementation(retrofit.create(ReviewLikeRetrofitService::class.java))),
            CommentLikeRepository(CommentLikeRetrofitDataSourceImplementation(retrofit.create(CommentLikeRetrofitService::class.java))))
        val originalIds = reviews.getReviewsByUser(CURRENT_USER_ID).getOrThrow().map { it.id }.toSet()
        val productId = products.getProducts().getOrThrow().first().id
        val marker = "Integration " + System.currentTimeMillis()
        var createdId: Int? = null
        val store = ViewModelStore()
        try {
            withContext(Dispatchers.Main) {
                withTimeout(30000) {
                    val profile = ProfileViewModel(users, reviews, content, follows).also { store.put("profile", it) }
                    profile.loadProfile("2")
                    val selected = profile.uiState.first { !it.loading }
                    assertNull(selected.error)
                    assertEquals("2", selected.profile?.id)
                    assertTrue(selected.reviews.isNotEmpty())
                    assertTrue(selected.reviews.all { it.authorId == "2" })

                    val product = ProductViewModel(products, reviews, content).also { store.put("product", it) }
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
                    val detail = ReviewViewModel(reviews, content).also { store.put("detail", it) }
                    detail.loadReview(created.id)
                    val detailState = detail.uiState.first { !it.loading }
                    assertNull(detailState.error)
                    assertEquals(productId, detailState.product?.id)
                    assertEquals(CURRENT_USER_ID.toString(), detailState.review?.authorId)

                    val edit = EditReviewViewModel(reviews, products).also { store.put("edit", it) }
                    edit.loadReview(created.id)
                    assertTrue(edit.uiState.first { !it.loading }.canEdit)
                    edit.onExperienceChange("Edited " + marker)
                    edit.onRatingChange(5)
                    edit.save()
                    val editState = edit.uiState.first { !it.saving }
                    assertNull(editState.errorResId)
                    assertTrue(editState.saved)
                    val updated = reviews.getReviewById(created.id).getOrThrow()
                    assertEquals(5, updated.rating)
                    assertEquals("Edited " + marker, updated.body)
                    product.loadProduct(productId)
                    assertTrue(product.uiState.first { !it.isLoading }.reviews.any { it.id == created.id && it.rating == 5 })
                    detail.loadReview(created.id)
                    assertTrue(detail.uiState.first { !it.loading }.canManage)
                    assertEquals(created.id, detail.requestDeletion())
                    val auth = AuthRepository(AuthRemoteDataSource(com.google.firebase.auth.FirebaseAuth.getInstance()),
                        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext)
                    val ownProfile = OwnProfileViewModel(auth,
                        StorageRepository(StorageRemoteDataSource(com.google.firebase.storage.FirebaseStorage.getInstance()), auth),
                        ProfileContentUseCase(users, follows), content, reviews, androidx.lifecycle.SavedStateHandle())
                        .also { store.put("own-profile", it) }
                    ownProfile.deleteReviewAndLoadProfile(created.id)
                    val deleted = ownProfile.uiState.first { !it.loading }
                    assertNull(deleted.errorMessageResId)
                    assertFalse(deleted.reviews.any { it.id == created.id })
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
