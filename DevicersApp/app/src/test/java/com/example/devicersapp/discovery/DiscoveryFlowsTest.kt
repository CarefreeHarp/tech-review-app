package com.example.devicersapp.discovery

import com.example.devicersapp.R
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.ui.screens.activity.ActivityViewModel
import com.example.devicersapp.ui.screens.found_products.FoundProductsViewModel
import com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsViewModel
import com.example.devicersapp.ui.screens.request_product.RequestProductViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*
import java.io.IOException
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class DiscoveryFlowsTest {
    private val dispatcher = StandardTestDispatcher()
    @Before fun setup() { Dispatchers.setMain(dispatcher) }
    @After fun cleanup() { Dispatchers.resetMain() }

    @Test fun categoryDescendantsRatingsAndSortUseActiveFirestoreReviews() = runTest(dispatcher) {
        val source = FakeDiscoverySource().apply {
            products = listOf(product(1, 2), product(2, 2), product(3, 3), product(4, 2))
            categories = listOf(CategoryInfo(1, null, "Audio", null), CategoryInfo(2, 1, "Audífonos", null))
            reviews = listOf(review(1, 1, 3), review(2, 2, 5), review(3, 3, 5), review(4, 1, 5).copy(isActive = false))
        }
        val vm = FoundProductsViewModel(DiscoveryRepository(source))
        vm.loadProducts("", "audio", 3f, "rated")
        advanceUntilIdle()
        assertEquals(listOf(2, 1), vm.uiState.value.results.map { it.id })
        assertEquals(1, vm.uiState.value.results.last().reviews.size)
        vm.onSearchTextChange("Product 1")
        assertEquals(listOf(1), vm.uiState.value.results.map { it.id })
        vm.onSearchTextChange("")
        assertEquals(2, vm.uiState.value.results.size)
    }

    @Test fun newerSearchCancelsOldLoadAndCanBroadenOriginalQuery() = runTest(dispatcher) {
        val pending = CompletableDeferred<List<ProductInfo>>()
        var calls = 0
        val source = object : FakeDiscoverySource() {
            override suspend fun getProducts() = if (calls++ == 0) pending.await() else listOf(product(2), product(3))
        }
        val vm = FoundProductsViewModel(DiscoveryRepository(source))
        vm.loadProducts("Product 1", "all", 0f, "recent")
        runCurrent()
        vm.loadProducts("Product 2", "all", 0f, "recent")
        advanceUntilIdle()
        pending.complete(listOf(product(1)))
        advanceUntilIdle()
        assertEquals(listOf(2), vm.uiState.value.results.map { it.id })
        vm.onSearchTextChange("")
        assertEquals(setOf(2, 3), vm.uiState.value.results.map { it.id }.toSet())
    }

    @Test fun searchFailureShowsErrorAndRetryRecovers() = runTest(dispatcher) {
        var fail = true
        val source = object : FakeDiscoverySource() {
            override suspend fun getProducts(): List<ProductInfo> {
                if (fail) throw IOException()
                return listOf(product(1))
            }
        }
        val vm = FoundProductsViewModel(DiscoveryRepository(source))
        vm.loadProducts("", "all", 0f, "recent")
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.errorMessage)
        assertFalse(vm.uiState.value.isLoading)
        fail = false
        vm.loadProducts("", "all", 0f, "recent")
        advanceUntilIdle()
        assertNull(vm.uiState.value.errorMessage)
        assertEquals(1, vm.uiState.value.results.size)
    }

    @Test fun profileSearchExcludesSessionAndInactiveUsersAndCountsReviews() = runTest(dispatcher) {
        val source = FakeDiscoverySource().apply {
            users = listOf(user(1), user(2), user(3).copy(isActive = false))
            reviews = listOf(review(1, 1, 4).copy(userId = 2), review(2, 1, 4).copy(userId = 2, isActive = false))
            followed = setOf(2)
        }
        val vm = ProfileSearchResultsViewModel(DiscoveryRepository(source), session())
        advanceUntilIdle()
        assertEquals("2", vm.uiState.value.results.single().id)
        assertEquals(1, vm.uiState.value.results.single().reviewCount)
        assertEquals("https://example.com/2.jpg", vm.uiState.value.results.single().imageUrl)
        vm.onFollow("3")
        advanceUntilIdle()
        assertEquals(setOf("2"), vm.uiState.value.followedProfileIds)
    }

    @Test fun activityUsesRecipientActualActorsAndValidDestinations() = runTest(dispatcher) {
        val now = Instant.parse("2026-10-11T17:00:00Z").toEpochMilli()
        val source = FakeDiscoverySource().apply {
            users = listOf(user(1), user(2))
            products = listOf(product(1))
            reviews = listOf(review(1, 1, 5))
            followed = setOf(2)
            notifications = listOf(
                DiscoveryNotification("follow", 1, 2, "follow", null, now),
                DiscoveryNotification("like", 1, 2, "review_like", 1, now - 1000),
                DiscoveryNotification("comment-like", 1, 2, "comment_like", 1, now - 2000),
                DiscoveryNotification("comment", 1, 2, "comment", 1, now - 86400000),
                DiscoveryNotification("other-user", 3, 2, "follow", null, now),
                DiscoveryNotification("deleted-review", 1, 2, "comment", 99, now),
                DiscoveryNotification("deleted-actor", 1, 99, "follow", null, now),
                DiscoveryNotification("unknown", 1, 2, "future-type", null, now)
            )
        }
        val result = DiscoveryRepository(source).getActivity(1, now)
        assertEquals(listOf("today", "yesterday"), result.groups.map { it.id })
        val events = result.groups.flatMap { it.notifications }
        assertEquals(listOf("follow", "like", "comment-like", "comment"), events.map { it.id })
        assertEquals("2", events.first().targetProfileId)
        assertEquals(R.string.discovery_activity_liked_comment, events[2].actionResId)
        assertEquals("Product 1", result.productNames[1])
        assertEquals("user2", result.actors[2]?.username)
        assertEquals(events.map { it.id }.toSet(), result.followedActivityIds)
    }

    @Test fun activityFailureDoesNotInventEventsAndRetryCanReturnEmpty() = runTest(dispatcher) {
        var fail = true
        val source = object : FakeDiscoverySource() {
            override suspend fun getNotifications(userId: Int): List<DiscoveryNotification> {
                if (fail) throw IOException()
                return emptyList()
            }
        }
        val vm = ActivityViewModel(DiscoveryRepository(source), session())
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)
        assertTrue(vm.uiState.value.activityGroups.isEmpty())
        fail = false
        vm.loadActivity()
        advanceUntilIdle()
        assertNull(vm.uiState.value.error)
        assertFalse(vm.uiState.value.loading)
        assertTrue(vm.uiState.value.activityGroups.isEmpty())
    }

    @Test fun requestWaitsForConfirmationAndPreventsDoubleSubmit() = runTest(dispatcher) {
        val pending = CompletableDeferred<Unit>()
        var writes = 0
        var successes = 0
        val source = object : ProductRequestsRemoteDataSource {
            override suspend fun submit(requestId: String, userId: Int, firebaseUid: String, suggestion: ProductSuggestion) {
                writes++
                assertEquals(1, userId)
                assertEquals("uid-1", firebaseUid)
                assertEquals("New phone", suggestion.productName)
                pending.await()
            }
        }
        val vm = RequestProductViewModel(ProductRequestsRepository(source, session()))
        vm.onProductNameChange(" New phone ")
        vm.submit { successes++ }
        vm.submit { successes++ }
        runCurrent()
        assertEquals(1, writes)
        assertEquals(0, successes)
        assertTrue(vm.uiState.value.submitting)
        vm.onProductNameChange("Ignored while sending")
        assertEquals(" New phone ", vm.uiState.value.productName)
        pending.complete(Unit)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.submitted)
        assertEquals(1, successes)
        vm.submit { successes++ }
        advanceUntilIdle()
        assertEquals(1, writes)
    }

    @Test fun requestRejectsInvalidDateAndRetriesSameIdAfterFailure() = runTest(dispatcher) {
        val ids = mutableListOf<String>()
        val source = object : ProductRequestsRemoteDataSource {
            override suspend fun submit(requestId: String, userId: Int, firebaseUid: String, suggestion: ProductSuggestion) {
                ids += requestId
                if (ids.size == 1) throw IOException()
            }
        }
        val vm = RequestProductViewModel(ProductRequestsRepository(source, session()))
        vm.onProductNameChange("Phone")
        vm.onReleaseDateChange("31022026")
        vm.submit()
        advanceUntilIdle()
        assertTrue(ids.isEmpty())
        vm.onReleaseDateChange("")
        vm.submit()
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)
        assertTrue(vm.uiState.value.canSendRequest)
        vm.submit()
        advanceUntilIdle()
        assertEquals(2, ids.size)
        assertEquals(ids[0], ids[1])
        assertTrue(vm.uiState.value.submitted)
    }

    private fun product(id: Int, category: Int = 1) = ProductInfo(id, category, 1, "Product $id", null,
        null, null, null, null, true, "2026-10-11T00:00:00Z", "")
    private fun review(id: Int, product: Int, rating: Int) = ReviewInfo(id, 1, product, rating, null, "", true, "", "")
    private fun user(id: Int) = UserInfo(id, "", "user$id", "uid-$id", "", "https://example.com/$id.jpg", null, true, "", "")
    private suspend fun session(): SessionRepository {
        val profile = user(1)
        val source = object : UserProfileRemoteDataSource {
            override suspend fun getById(userId: Int) = profile
            override suspend fun findByFirebaseUid(firebaseUid: String) = profile
            override suspend fun createIfMissing(firebaseUid: String, email: String, username: String, profileImageUrl: String?) = profile
            override suspend fun updateProfile(userId: Int, firebaseUid: String, username: String?, profileImageUrl: String?, biography: String?) = profile
        }
        return SessionRepository(source).also { it.loadProfile("uid-1", "", "user1", null) }
    }
}
