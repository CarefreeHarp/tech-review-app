package com.example.devicersapp

import androidx.activity.compose.setContent
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModelStore
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.dto.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.domain.usecase.*
import com.example.devicersapp.ui.screens.own_profile.OwnProfileView
import com.example.devicersapp.ui.screens.own_profile.OwnProfileViewModel
import com.example.devicersapp.ui.screens.review.ReviewView
import com.example.devicersapp.ui.screens.review.ReviewViewModel
import com.example.devicersapp.ui.theme.DevicersAppTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CompletableDeferred
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/** Mantiene el DELETE pendiente para comprobar la salida inmediata y el reintento desde el perfil. */
@RunWith(AndroidJUnit4::class)
class OwnProfileDeletionUiTest {
    @get:Rule val compose = createAndroidComposeRule<MainActivity>()

    @Test
    fun leavingDetailDoesNotCancelDeletionAndProfileWaitsUntilRefreshCompletes() {
        val api = DeletionApi()
        val repositories = ReviewRepository(api)
        val users = UsersRepository(api)
        val content = ReviewContentUseCase(ProductRepository(api), users,
            CommentRepository(api), ReviewLikeRepository(api), CommentLikeRepository(api))
        val store = ViewModelStore()
        val detailStore = ViewModelStore()
        val profile = mutableStateOf<OwnProfileViewModel?>(null)
        compose.runOnUiThread {
            val detail = ReviewViewModel(repositories, content).also { detailStore.put("detail", it) }
            compose.activity.setContent {
                DevicersAppTheme {
                    val ownProfile = profile.value
                    if (ownProfile == null) {
                        ReviewView(reviewId = 42, viewModel = detail, onDeleteRequested = { id ->
                            val auth = AuthRepository(AuthRemoteDataSource(FirebaseAuth.getInstance()),
                                compose.activity.applicationContext)
                            profile.value = OwnProfileViewModel(auth,
                                StorageRepository(StorageRemoteDataSource(FirebaseStorage.getInstance()), auth),
                                ProfileContentUseCase(users, FollowRepository(api)), content, repositories,
                                SavedStateHandle(mapOf("deleteReviewId" to id)))
                                .also { store.put("profile", it) }
                            detailStore.clear()
                        })
                    } else {
                        OwnProfileView(viewModel = ownProfile, deleteReviewId = 42)
                    }
                }
            }
        }
        try {
            val options = compose.activity.getString(R.string.review_actions_menu)
            compose.waitUntil(10000) { compose.onAllNodesWithContentDescription(options).fetchSemanticsNodes().isNotEmpty() }
            compose.onNodeWithContentDescription(options).performScrollTo().performClick()
            compose.onNodeWithText(compose.activity.getString(R.string.review_action_delete)).performClick()
            compose.waitUntil(10000) { api.deleteCalls == 1 }
            compose.onNodeWithContentDescription(compose.activity.getString(R.string.screen_loading)).assertExists()
            compose.runOnIdle {
                assertEquals(FirebaseAuth.getInstance().currentUser?.uid, profile.value!!.uiState.value.userId)
                assertTrue(profile.value!!.uiState.value.loading)
                assertTrue(profile.value!!.uiState.value.deleting)
                assertEquals(1, api.records.size)
            }
            api.deleteGate.complete(Unit)
            compose.waitUntil(10000) { profile.value?.uiState?.value?.loading == false }
            compose.onNodeWithText(compose.activity.getString(R.string.own_profile_empty_reviews)).assertExists()
            compose.runOnIdle {
                assertTrue(api.records.isEmpty())
                assertNull(profile.value!!.uiState.value.errorMessageResId)
                profile.value!!.deleteReviewAndLoadProfile(42)
            }
            compose.waitUntil(10000) { !profile.value!!.uiState.value.loading }
            assertEquals(1, api.deleteCalls)
        } finally {
            compose.runOnUiThread { detailStore.clear(); store.clear() }
        }
    }

    @Test
    fun failedDeletionIsRetriedFromOwnProfileWithoutLosingTheReview() {
        val api = DeletionApi().apply { failDelete = true; deleteGate.complete(Unit) }
        val repositories = ReviewRepository(api)
        val users = UsersRepository(api)
        val content = ReviewContentUseCase(ProductRepository(api), users,
            CommentRepository(api), ReviewLikeRepository(api), CommentLikeRepository(api))
        lateinit var profile: OwnProfileViewModel
        val store = ViewModelStore()
        compose.runOnUiThread {
            val auth = AuthRepository(AuthRemoteDataSource(FirebaseAuth.getInstance()), compose.activity.applicationContext)
            profile = OwnProfileViewModel(auth,
                StorageRepository(StorageRemoteDataSource(FirebaseStorage.getInstance()), auth),
                ProfileContentUseCase(users, FollowRepository(api)), content, repositories,
                SavedStateHandle(mapOf("deleteReviewId" to 42))).also { store.put("profile", it) }
            compose.activity.setContent { DevicersAppTheme { OwnProfileView(viewModel = profile, deleteReviewId = 42) } }
        }
        try {
            val error = compose.activity.getString(R.string.review_delete_error)
            compose.waitUntil(10000) { compose.onAllNodesWithText(error).fetchSemanticsNodes().isNotEmpty() }
            assertEquals(1, api.records.size)
            api.failDelete = false
            compose.onNodeWithText(compose.activity.getString(R.string.home_feed_retry)).performClick()
            compose.waitUntil(10000) { !profile.uiState.value.loading && profile.uiState.value.errorMessageResId == null }
            assertTrue(api.records.isEmpty())
        } finally {
            compose.runOnUiThread { store.clear() }
        }
    }
}

/** API en memoria: no modifica reseñas reales ni realiza consultas de red. */
private class DeletionApi : ReviewRemoteDataSource, UsersRemoteDataSource, ProductRemoteDataSource,
    CommentRemoteDataSource, ReviewLikeRemoteDataSource, CommentLikeRemoteDataSource, FollowRemoteDataSource {
    val deleteGate = CompletableDeferred<Unit>()
    var deleteCalls = 0
    var failDelete = false
    var records = listOf(ReviewDto(42, 1, 7, 4, "Owned review", "Existing body", true, "", ""))
    override suspend fun getReviewById(reviewId: Int) = records.single { it.id == reviewId }
    override suspend fun getReviews() = records
    override suspend fun getReviewsByUser(userId: Int) = records.filter { it.user_id == userId }
    override suspend fun getReviewsByProduct(productId: Int) = records.filter { it.article_id == productId }
    override suspend fun createReview(request: CreateReviewRequestDto): ReviewDto = error("Unused")
    override suspend fun updateReview(reviewId: Int, request: UpdateReviewRequestDto): ReviewDto = error("Unused")
    override suspend fun deleteReview(reviewId: Int) {
        deleteCalls++
        deleteGate.await()
        if (failDelete) error("Offline")
        records = records.filter { it.id != reviewId }
    }
    override suspend fun getUsers(excludeUserId: Int?) = listOf(getUserById(1))
    override suspend fun getUserById(userId: Int) = UserDto(userId, "test@example.com", "owner", null, "Biography", null, null, true, "", "")
    override suspend fun getProducts() = listOf(getProductById(7))
    override suspend fun getProductById(productId: Int) = ProductDto(productId, 1, 1, "Product", "Model", "device_01", null, null, null, true, "", "")
    override suspend fun getComments() = emptyList<CommentDto>()
    override suspend fun getReviewLikes() = emptyList<ReviewLikeDto>()
    override suspend fun getCommentLikes() = emptyList<CommentLikeDto>()
    override suspend fun getFollows() = emptyList<FollowDto>()
}
