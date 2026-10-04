package com.example.devicersapp.remote

import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.dto.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.screens.profile.ProfileViewModel
import com.example.devicersapp.ui.screens.rate_product.RateProductViewModel
import com.example.devicersapp.ui.screens.remote_reviews.RemoteReviewsViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteFlowsTest {
    private val dispatcher = StandardTestDispatcher()
    private val source = FakeReviews()
    private val products = ProductRepository(FakeProducts())
    private val repository = ReviewRepository(source)

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    @Test
    fun profileOnlyShowsSelectedUsersReviews() =
        runTest(dispatcher) {
            val vm = ProfileViewModel(UsersRepository(FakeUsers()), repository)
            source.records = listOf(review(1, 2), review(2, 1))
            vm.loadProfile("2")
            advanceUntilIdle()
            assertEquals("2", vm.uiState.value.profile?.id)
            assertEquals(listOf(1), vm.uiState.value.reviews.map { it.id })
            assertEquals(2, source.requestedUser)
        }

    @Test
    fun reviewDetailUsesRemoteAuthorProductAndBody() =
        runTest(dispatcher) {
            source.records = listOf(review(10, 2))
            val vm =
                com.example.devicersapp.ui.screens.review.ReviewViewModel(
                    repository,
                    products,
                    UsersRepository(FakeUsers()),
                )
            vm.loadReview(10)
            advanceUntilIdle()
            assertEquals("user2", vm.uiState.value.review?.authorName)
            assertEquals("Body", vm.uiState.value.review?.body)
            assertEquals(7, vm.uiState.value.product?.id)
            vm.loadReview(999)
            advanceUntilIdle()
            assertNull(vm.uiState.value.review)
            assertNotNull(vm.uiState.value.error)
        }

    @Test
    fun invalidProfileDoesNotFallBackToLocalUser() =
        runTest(dispatcher) {
            val vm = ProfileViewModel(UsersRepository(FakeUsers()), repository)
            vm.loadProfile("local_user")
            assertNull(vm.uiState.value.profile)
            assertNotNull(vm.uiState.value.error)
            assertNull(source.requestedUser)
        }

    @Test
    fun switchingProfilesCancelsOldResponse() =
        runTest(dispatcher) {
            val users =
                object : UsersRemoteDataSource {
                    override suspend fun getUsers() = emptyList<UserDto>()

                    override suspend fun getUserById(userId: Int): UserDto {
                        delay(if (userId == 1) 1000 else 10)
                        return user(userId)
                    }
                }
            val vm = ProfileViewModel(UsersRepository(users), repository)
            vm.loadProfile("1")
            runCurrent()
            vm.loadProfile("2")
            advanceUntilIdle()
            assertEquals("2", vm.uiState.value.profile?.id)
            assertFalse(vm.uiState.value.loading)
        }

    @Test
    fun createValidatesAndUsesBackendProductAndFixedUser() =
        runTest(dispatcher) {
            val vm = RateProductViewModel(products, repository)
            vm.loadProduct(7)
            advanceUntilIdle()
            vm.publish()
            assertEquals(0, source.creates)
            vm.onRatingChange(4)
            vm.onExperienceChange("Experience")
            vm.onAdvantageChange("Battery")
            vm.publish()
            vm.publish()
            advanceUntilIdle()
            assertEquals(1, source.creates)
            assertEquals(1, source.created?.userId)
            assertEquals(7, source.created?.articleId)
            assertTrue(source.created!!.body.contains("Battery"))
            assertTrue(vm.uiState.value.published)
        }

    @Test
    fun failedPublishKeepsDraftAndAllowsRetry() =
        runTest(dispatcher) {
            val vm = RateProductViewModel(products, repository)
            vm.loadProduct(7)
            advanceUntilIdle()
            vm.onRatingChange(3)
            vm.onExperienceChange("My draft")
            source.fail = true
            vm.publish()
            advanceUntilIdle()
            assertEquals("My draft", vm.uiState.value.experience)
            assertFalse(vm.uiState.value.published)
            assertFalse(vm.uiState.value.saving)
            assertNotNull(vm.uiState.value.error)
            source.fail = false
            vm.publish()
            advanceUntilIdle()
            assertTrue(vm.uiState.value.published)
        }

    @Test
    fun editAndDeleteOnlyOwnReviewsAndUpdateList() =
        runTest(dispatcher) {
            source.records = listOf(review(10, 1), review(20, 2))
            val vm = RemoteReviewsViewModel(repository, products)
            vm.load()
            advanceUntilIdle()
            vm.edit(review(20, 2))
            assertFalse(vm.uiState.value.editing)
            vm.requestDelete(review(20, 2))
            assertNull(vm.uiState.value.deleteId)
            vm.edit(review(10, 1))
            vm.body("Edited")
            vm.save()
            advanceUntilIdle()
            assertEquals("Edited", vm.uiState.value.reviews.first { it.id == 10 }.body)
            vm.requestDelete(review(10, 1))
            vm.delete()
            advanceUntilIdle()
            assertTrue(vm.uiState.value.reviews.none { it.id == 10 })
            assertEquals(10, source.deleted)
        }

    @Test
    fun failedEditRetainsFormAndFailedDeleteRetainsReview() =
        runTest(dispatcher) {
            source.records = listOf(review(10, 1))
            val vm = RemoteReviewsViewModel(repository, products)
            vm.load()
            advanceUntilIdle()
            vm.edit(review(10, 1))
            vm.body("Keep me")
            source.fail = true
            vm.save()
            advanceUntilIdle()
            assertTrue(vm.uiState.value.editing)
            assertEquals("Keep me", vm.uiState.value.body)
            vm.cancel()
            vm.requestDelete(review(10, 1))
            vm.delete()
            advanceUntilIdle()
            assertEquals(1, vm.uiState.value.reviews.size)
            assertNotNull(vm.uiState.value.error)
        }
}

private fun user(id: Int) =
    UserDto(id, "user@example.com", "user$id", null, "Biography", null, null, true, "", "")

private fun product() = ProductDto(7, 1, 1, "Phone", "Model", null, null, null, null, true, "", "")

private fun review(id: Int, owner: Int) = ReviewDto(id, owner, 7, 4, "Title", "Body", true, "", "")

private class FakeUsers : UsersRemoteDataSource {
    override suspend fun getUsers() = listOf(user(1), user(2))

    override suspend fun getUserById(userId: Int) = user(userId)
}

private class FakeProducts : ProductRemoteDataSource {
    override suspend fun getProducts() = listOf(product())

    override suspend fun getProductById(productId: Int) =
        product().also { require(productId == it.id) }
}

private class FakeReviews : ReviewRemoteDataSource {
    var records = emptyList<ReviewDto>()
    var requestedUser: Int? = null
    var creates = 0
    var created: CreateReviewRequestDto? = null
    var deleted: Int? = null
    var fail = false

    override suspend fun getReviews() = records

    override suspend fun getReviewById(reviewId: Int) = records.first { it.id == reviewId }

    override suspend fun getReviewsByUser(userId: Int): List<ReviewDto> {
        requestedUser = userId
        return records
    }

    override suspend fun getReviewsByProduct(productId: Int) =
        records.filter { it.articleId == productId }

    override suspend fun createReview(request: CreateReviewRequestDto): ReviewDto {
        creates++
        if (fail) error("Offline")
        created = request
        return review(30, request.userId).copy(body = request.body)
    }

    override suspend fun updateReview(reviewId: Int, request: UpdateReviewRequestDto): ReviewDto {
        if (fail) error("Offline")
        return getReviewById(reviewId).copy(body = request.body!!, rating = request.rating!!)
    }

    override suspend fun deleteReview(reviewId: Int) {
        if (fail) error("Offline")
        deleted = reviewId
    }
}
