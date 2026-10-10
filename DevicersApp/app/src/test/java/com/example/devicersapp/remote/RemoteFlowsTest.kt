package com.example.devicersapp.remote

import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.dto.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.ui.screens.profile.ProfileViewModel
import com.example.devicersapp.ui.screens.rate_product.RateProductViewModel
import com.example.devicersapp.ui.screens.edit_review.EditReviewViewModel
import com.example.devicersapp.ui.screens.review.ReviewViewModel
import com.example.devicersapp.R
import kotlinx.coroutines.*
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class RemoteFlowsTest {

    /** Proporciona una identidad explícita a las pruebas existentes sin acceder a Firestore. */
    private fun fixtureSession(): SessionRepository = kotlinx.coroutines.runBlocking {
        val profile = user(1).toUserInfo().copy(firebaseUid = "fixture-uid")
        val source = object : com.example.devicersapp.data.datasource.UserProfileRemoteDataSource {
            override suspend fun getById(userId: Int) = profile.also { check(it.id == userId) }
            override suspend fun findByFirebaseUid(firebaseUid: String) = profile
            override suspend fun createIfMissing(firebaseUid: String, email: String, username: String, profileImageUrl: String?) = profile
            override suspend fun updateProfile(userId: Int, firebaseUid: String, username: String?, profileImageUrl: String?) = profile
        }
        SessionRepository(source).also { it.loadProfile("fixture-uid", profile.email, profile.username, profile.profileImageUrl) }
    }

    private val session = fixtureSession()
    private val dispatcher = StandardTestDispatcher()
    private val source = FakeReviews()
    private val products = ProductRepository(FakeProducts())
    private val repository = ReviewRepository(source)
    private val contentUsers = UsersRepository(FakeUsers(), fixtureProfileImages)
    private val contentComments = CommentRepository(source)
    private val contentReviewLikes = ReviewLikeRepository(source)
    private val contentCommentLikes = CommentLikeRepository(source)
    private val saved = ReviewBookmarkRepository(source)
    private val follows = FollowRepository(FakeUsers())

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
    }

    /** Los guardados usan la relación del usuario solicitado y conservan sus datos remotos. */
    @Test
    fun savedReviewsFilterBookmarksAndLoadAuthorsProductsAndReactions() = runTest(dispatcher) {
        source.records = listOf(review(10, 2), review(11, 1), review(12, 2).copy(is_active = false))
        source.bookmarks = listOf(ReviewBookmarkDto(1, 10), ReviewBookmarkDto(1, 12), ReviewBookmarkDto(2, 999))
        source.likes = listOf(ReviewLikeDto(1, 1, 10), ReviewLikeDto(2, 3, 10))
        source.comments = listOf(CommentDto(20, 10, 1, null, "Saved review comment", true, "", ""))
        source.commentLikes = listOf(CommentLikeDto(1, 2, 20))
        val saved = saved.getSavedReviews(1, repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes).getOrThrow().map { it.toReviewContent() }
        assertEquals(listOf(10), saved.map { it.id })
        assertEquals("user2", saved.single().authorName)
        assertEquals("Phone", saved.single().productName)
        assertEquals(2, saved.single().likes)
        assertEquals(1, saved.single().comments.single().likes)
    }

    /** Un artículo retirado del catálogo conserva los datos de las reseñas que aún lo referencian. */
    @Test
    fun reviewContentKeepsReferencedInactiveProducts() = runTest(dispatcher) {
        val remote = object : ProductRemoteDataSource {
            override suspend fun getProducts() = listOf(product().copy(is_active = false))
            override suspend fun getProductById(productId: Int) = getProducts().single()
        }
        val catalog = ProductRepository(remote)
        assertTrue(catalog.getProducts().getOrThrow().isEmpty())
        val detailProducts = catalog
        val detailUsers = UsersRepository(FakeUsers(), fixtureProfileImages)
        val detailComments = CommentRepository(source)
        val detailReviewLikes = ReviewLikeRepository(source)
        val detailCommentLikes = CommentLikeRepository(source)
        val content = detailProducts.getReviewContents(listOf(review(10, 2).toReviewInfo()), detailUsers, detailComments, detailReviewLikes, detailCommentLikes).single()
        assertEquals("Phone", content.article?.name)
        assertEquals(false, content.article?.isActive)
    }

    /** Un fallo de consulta o una relación incompleta no se presenta como una lista vacía. */
    @Test
    fun savedReviewsPropagateFailuresAndMissingReferences() = runTest(dispatcher) {
        source.failBookmarks = true
        assertTrue(saved.getSavedReviews(1, repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes).isFailure)
        source.failBookmarks = false
        assertTrue(saved.getSavedReviews(1, repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes).getOrThrow().isEmpty())
        source.bookmarks = listOf(ReviewBookmarkDto(1, 999))
        assertTrue(saved.getSavedReviews(1, repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes).isFailure)
        source.records = listOf(review(999, 2))
        source.failCommentLikes = true
        assertTrue(saved.getSavedReviews(1, repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes).isFailure)
    }

    /** Las estadísticas separan seguidores de seguidos y admiten ceros realmente consultados. */
    @Test
    fun profileStatsCountIncomingAndOutgoingRelations() = runTest(dispatcher) {
        var relations = listOf(FollowDto(1, 2), FollowDto(2, 1), FollowDto(3, 1), FollowDto(3, 2))
        val remote = object : UsersRemoteDataSource, FollowRemoteDataSource {
            override suspend fun getUsers(excludeUserId: Int?) = listOf(user(1))
            override suspend fun getUserById(userId: Int) = user(userId)
            override suspend fun getFollows() = relations
        }
        val users = UsersRepository(remote, fixtureProfileImages)
        val usersFollows = FollowRepository(remote)
        val profile = users.getProfileContent(1, 2, usersFollows)
        assertEquals("Biography", profile.biography)
        assertEquals(listOf("2", "2", "1"), profile.stats.map { it.number })
        relations = emptyList()
        assertEquals(listOf("0", "0", "0"), users.getProfileContent(1, 0, usersFollows).stats.map { it.number })
    }

    /** Si falla la consulta de seguidores, el perfil no sustituye sus conteos por ceros. */
    @Test
    fun profileStatsPropagateFollowingFailure() = runTest(dispatcher) {
        val remote = object : UsersRemoteDataSource, FollowRemoteDataSource {
            override suspend fun getUsers(excludeUserId: Int?) = listOf(user(1))
            override suspend fun getUserById(userId: Int) = user(userId)
            override suspend fun getFollows(): List<FollowDto> = error("Offline")
        }
        assertTrue(runCatching { UsersRepository(remote, fixtureProfileImages).getProfileContent(1, 2, FollowRepository(remote)) }.isFailure)
    }

    @Test
    fun profileOnlyShowsSelectedUsersReviews() =
        runTest(dispatcher) {
            val vm = ProfileViewModel(UsersRepository(FakeUsers(), fixtureProfileImages), repository, products, contentComments, contentReviewLikes, contentCommentLikes, follows, session)
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
                com.example.devicersapp.ui.screens.review.ReviewViewModel(repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes, session)
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
    fun detailLoadsScopedCountsAndNestedCommentsWithRealAuthors() = runTest(dispatcher) {
        source.records = listOf(review(10, 2))
        source.comments = listOf(
            CommentDto(2, 10, 2, 1, "Answer", true, "", ""),
            CommentDto(1, 10, 3, null, "Question", true, "", ""),
            CommentDto(3, 10, 4, null, "Hidden", false, "", ""),
            CommentDto(4, 99, 4, null, "Other review", true, "", "")
        )
        source.likes = listOf(ReviewLikeDto(1, 3, 10), ReviewLikeDto(2, 4, 10), ReviewLikeDto(3, 4, 99))
        source.commentLikes = listOf(
            CommentLikeDto(1, 1, 1), CommentLikeDto(2, 2, 1),
            CommentLikeDto(3, 3, 2), CommentLikeDto(4, 1, 4)
        )
        val vm = com.example.devicersapp.ui.screens.review.ReviewViewModel(repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes, session)
        vm.loadReview(10)
        advanceUntilIdle()
        val state = vm.uiState.value
        assertEquals(2, state.review?.likes)
        assertEquals(2, state.review?.comments?.size)
        assertEquals(listOf("Question", "Answer"), state.replies.map { it.body })
        assertEquals(listOf("user3", "user2"), state.replies.map { it.authorName })
        assertEquals(listOf(0, 1), state.replies.map { it.depth })
        assertEquals(listOf(2, 1), state.replies.map { it.likes })
        assertEquals(state.review?.comments, state.replies)
    }

    @Test
    fun detailDoesNotReportZeroWhenInteractionsFail() = runTest(dispatcher) {
        source.records = listOf(review(10, 2))
        source.failInteractions = true
        val vm = com.example.devicersapp.ui.screens.review.ReviewViewModel(repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes, session)
        vm.loadReview(10)
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)
        assertNull(vm.uiState.value.review)
    }

    /** Una consulta fallida de likes de comentarios no debe convertirse en conteos de cero. */
    @Test
    fun detailReportsAnErrorWhenCommentLikesFail() = runTest(dispatcher) {
        source.records = listOf(review(10, 2))
        source.failCommentLikes = true
        val vm = com.example.devicersapp.ui.screens.review.ReviewViewModel(repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes, session)
        vm.loadReview(10)
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.error)
        assertNull(vm.uiState.value.review)
    }

    /** Siguiendo usa las relaciones del usuario temporal 1 y conserva el feed completo en Para ti. */
    @Test
    fun homeFiltersFollowingWithBackendRelationsForUserOne() = runTest(dispatcher) {
        val catalog = object : ProductRemoteDataSource, BrandRemoteDataSource, CategoryRemoteDataSource {
            override suspend fun getBrands() = listOf(BrandDto(1, "API brand"))
            override suspend fun getCategories() = listOf(CategoryDto(1, null, "API category", null))
            override suspend fun getProducts() = listOf(product().copy(reviews = listOf(
                review(10, 2).copy(user = user(2), createdAt = "2026-10-05T10:00:00Z"),
                review(11, 3).copy(user = user(3), createdAt = "2026-10-05T09:00:00Z")
            )))
            override suspend fun getProductById(productId: Int) = getProducts().single()
        }
        val users = object : UsersRemoteDataSource, FollowRemoteDataSource {
            override suspend fun getUsers(excludeUserId: Int?) = listOf(user(1), user(2), user(3))
            override suspend fun getUserById(userId: Int) = user(userId)
            override suspend fun getFollows() = listOf(FollowDto(1, 2), FollowDto(2, 3))
        }
        val vm = com.example.devicersapp.ui.screens.home.HomeViewModel(ProductRepository(catalog), BrandRepository(catalog), CategoryRepository(catalog), CommentRepository(source), ReviewLikeRepository(source), FollowRepository(users), session, UsersRepository(users, fixtureProfileImages))
        advanceUntilIdle()
        assertNull(vm.uiState.value.errorMessageResId)
        assertEquals(listOf(10, 11), vm.uiState.value.feedReviews.map { it.reviewId })
        vm.onFollowingClick()
        assertEquals(listOf(10), vm.uiState.value.feedReviews.map { it.reviewId })
        vm.onForYouClick()
        assertEquals(listOf(10, 11), vm.uiState.value.feedReviews.map { it.reviewId })
    }

    @Test
    fun detailKeepsActiveOrphansAndHandlesInvalidCommentCycles() = runTest(dispatcher) {
        source.records = listOf(review(10, 2))
        source.comments = listOf(
            CommentDto(1, 10, 2, 99, "Orphan", true, "", ""),
            CommentDto(2, 10, 2, 3, "Cycle A", true, "", ""),
            CommentDto(3, 10, 2, 2, "Cycle B", true, "", "")
        )
        val vm = com.example.devicersapp.ui.screens.review.ReviewViewModel(repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes, session)
        vm.loadReview(10)
        advanceUntilIdle()
        assertEquals(3, vm.uiState.value.replies.size)
        assertEquals(0, vm.uiState.value.replies.first().depth)
    }

    @Test
    fun productQueriesAuthorsAndReportsReviewFailureInsteadOfEmptyRatings() = runTest(dispatcher) {
        source.records = listOf(review(10, 2))
        val vm = com.example.devicersapp.ui.screens.product.ProductViewModel(products, repository, contentUsers, contentComments, contentReviewLikes, contentCommentLikes)
        vm.loadProduct(7)
        advanceUntilIdle()
        assertEquals("user2", vm.uiState.value.reviews.single().user?.username)
        source.failInteractions = true
        vm.loadProduct(7)
        advanceUntilIdle()
        assertNotNull(vm.uiState.value.errorMessageResId)
        assertNull(vm.uiState.value.product)
    }

    @Test
    fun createFiltersWithRemoteCategoryIdsAndTheirHierarchy() = runTest(dispatcher) {
        val remote = object : ProductRemoteDataSource, BrandRemoteDataSource, CategoryRemoteDataSource {
            override suspend fun getBrands() = emptyList<BrandDto>()
            override suspend fun getCategories() = listOf(CategoryDto(50, null, "Audio API", null), CategoryDto(99, 50, "Audífonos API", null))
            override suspend fun getProducts() = listOf(product().copy(category_id = 99))
            override suspend fun getProductById(productId: Int) = product().copy(category_id = 99)
        }
        val vm = com.example.devicersapp.ui.screens.create_review.CreateReviewViewModel(ProductRepository(remote), CategoryRepository(remote))
        advanceUntilIdle()
        assertEquals(listOf("all", "50", "99"), vm.uiState.value.categories.map { it.id })
        assertEquals("Audífonos API", vm.uiState.value.categories.last().label)
        vm.onCategoryChange("50")
        assertEquals(1, vm.uiState.value.filteredProducts.size)
        vm.onCategoryChange("999")
        assertTrue(vm.uiState.value.filteredProducts.isEmpty())
    }

    @Test
    fun profileAndReviewDetailUseQueriedCountsInsteadOfDefaults() = runTest(dispatcher) {
        source.records = listOf(review(10, 2))
        source.likes = listOf(ReviewLikeDto(1, 1, 10))
        source.comments = listOf(CommentDto(20, 10, 1, null, "API comment", true, "", ""))
        val profile = ProfileViewModel(UsersRepository(FakeUsers(), fixtureProfileImages), repository, products, contentComments, contentReviewLikes, contentCommentLikes, follows, session)
        profile.loadProfile("2")
        advanceUntilIdle()
        assertEquals(1, profile.uiState.value.reviews.single().likes)
        assertEquals("API comment", profile.uiState.value.reviews.single().comments.single().body)
        val managed = ReviewViewModel(repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes, session)
        managed.loadReview(10)
        advanceUntilIdle()
        assertEquals(1, managed.uiState.value.review?.likes)
        assertEquals("API comment", managed.uiState.value.review?.comments?.single()?.body)
    }

    @Test
    fun profileSearchLoadsBackendFollowingAndDoesNotSimulateNewFollows() = runTest(dispatcher) {
        val remote = object : UsersRemoteDataSource, FollowRemoteDataSource {
            override suspend fun getUsers(excludeUserId: Int?): List<UserDto> {
                assertEquals(1, excludeUserId)
                return listOf(user(2))
            }
            override suspend fun getUserById(userId: Int) = user(userId)
            override suspend fun getFollows() = listOf(FollowDto(1, 2), FollowDto(9, 1))
        }
        val vm = com.example.devicersapp.ui.screens.profile_search_results.ProfileSearchResultsViewModel(UsersRepository(remote, fixtureProfileImages), FollowRepository(remote), session)
        advanceUntilIdle()
        assertEquals(setOf("2"), vm.uiState.value.followedProfileIds)
        vm.onFollow("3")
        advanceUntilIdle()
        assertEquals(setOf("2"), vm.uiState.value.followedProfileIds)
    }

    @Test
    fun invalidProfileDoesNotFallBackToLocalUser() =
        runTest(dispatcher) {
            val vm = ProfileViewModel(UsersRepository(FakeUsers(), fixtureProfileImages), repository, products, contentComments, contentReviewLikes, contentCommentLikes, follows, session)
            vm.loadProfile("local_user")
            assertNull(vm.uiState.value.profile)
            assertNotNull(vm.uiState.value.error)
            assertNull(source.requestedUser)
        }

    @Test
    fun switchingProfilesCancelsOldResponse() =
        runTest(dispatcher) {
            val users =
                object : UsersRemoteDataSource, FollowRemoteDataSource {
                    override suspend fun getFollows() = emptyList<FollowDto>()
                    override suspend fun getUsers(excludeUserId: Int?) = emptyList<UserDto>()

                    override suspend fun getUserById(userId: Int): UserDto {
                        delay(if (userId == 1) 1000 else 10)
                        return user(userId)
                    }
                }
            val vm = ProfileViewModel(UsersRepository(users, fixtureProfileImages), repository, products, contentComments, contentReviewLikes, contentCommentLikes, FollowRepository(users), session)
            vm.loadProfile("1")
            runCurrent()
            vm.loadProfile("2")
            advanceUntilIdle()
            assertEquals("2", vm.uiState.value.profile?.id)
            assertFalse(vm.uiState.value.loading)
        }

    @Test
    fun createValidatesAndUsesBackendProductAndSessionUser() =
        runTest(dispatcher) {
            val vm = RateProductViewModel(products, repository, session)
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
            val vm = RateProductViewModel(products, repository, session)
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
    fun editPreloadsFieldsAndOnlyUpdatesContentOfOwnReview() = runTest(dispatcher) {
        val body = "Experience\n\nVentajas: Comfort\n\nDesventajas: Price"
        source.records = listOf(review(10, 1).copy(body = body), review(20, 2))
        val vm = EditReviewViewModel(repository, products, session)
        vm.loadReview(10)
        advanceUntilIdle()
        assertTrue(vm.uiState.value.canEdit)
        assertEquals("Title", vm.uiState.value.title)
        assertEquals(4, vm.uiState.value.rating)
        assertEquals("Experience", vm.uiState.value.experience)
        assertEquals("Comfort", vm.uiState.value.advantage)
        assertEquals("Price", vm.uiState.value.disadvantage)
        assertEquals(7, vm.uiState.value.product?.id)
        vm.onTitleChange("Edited title")
        vm.onExperienceChange("Edited experience")
        vm.onRatingChange(5)
        vm.save()
        vm.save()
        advanceUntilIdle()
        assertTrue(vm.uiState.value.saved)
        assertEquals(1, source.updates)
        val updated = source.records.first { it.id == 10 }
        assertEquals("Edited title", updated.title)
        assertEquals("Edited experience\n\nVentajas: Comfort\n\nDesventajas: Price", updated.body)
        assertEquals(1, updated.user_id)
        assertEquals(7, updated.article_id)
        assertEquals(5, updated.rating)
    }

    @Test
    fun otherAuthorsCannotBeEditedOrDeletedAndHaveNoActionsMenu() = runTest(dispatcher) {
        source.records = listOf(review(20, 2))
        val edit = EditReviewViewModel(repository, products, session)
        edit.loadReview(20)
        advanceUntilIdle()
        assertFalse(edit.uiState.value.canEdit)
        assertEquals(R.string.edit_review_owner_error, edit.uiState.value.errorResId)
        edit.save()
        val detail = ReviewViewModel(repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes, session)
        detail.loadReview(20)
        advanceUntilIdle()
        assertFalse(detail.uiState.value.canManage)
        detail.setActionsMenuExpanded(true)
        assertFalse(detail.uiState.value.actionsMenuExpanded)
        detail.requestDeletion()
        advanceUntilIdle()
        assertEquals(0, source.updates)
        assertNull(source.deleted)
    }

    @Test
    fun ownReviewImmediatelyRequestsProfileNavigationWithoutStartingDeleteInDetail() = runTest(dispatcher) {
        source.records = listOf(review(10, 1))
        val detail = ReviewViewModel(repository, products, contentUsers, contentComments, contentReviewLikes, contentCommentLikes, session)
        detail.loadReview(10)
        advanceUntilIdle()
        detail.setActionsMenuExpanded(true)
        assertEquals(10, detail.requestDeletion())
        assertTrue(detail.uiState.value.deletionRequested)
        assertFalse(detail.uiState.value.actionsMenuExpanded)
        assertNull(detail.requestDeletion())
        assertNull(source.deleted)
    }

    @Test
    fun failedEditRetainsFormUntilRetrySucceeds() = runTest(dispatcher) {
        source.records = listOf(review(10, 1))
        val edit = EditReviewViewModel(repository, products, session)
        edit.loadReview(10)
        advanceUntilIdle()
        edit.onExperienceChange("Keep me")
        source.fail = true
        edit.save()
        advanceUntilIdle()
        assertFalse(edit.uiState.value.saved)
        assertFalse(edit.uiState.value.saving)
        assertEquals("Keep me", edit.uiState.value.experience)
        assertEquals(R.string.edit_review_save_error, edit.uiState.value.errorResId)
        source.fail = false
        edit.save()
        advanceUntilIdle()
        assertTrue(edit.uiState.value.saved)
    }

    @Test
    fun editingLongReviewsDoesNotTruncateTheirBodyOrDuplicateSections() = runTest(dispatcher) {
        val body = "Long experience ".repeat(80) + "\n\nVentajas: Works\n\nDesventajas: Heavy"
        source.records = listOf(review(10, 1).copy(body = body))
        val edit = EditReviewViewModel(repository, products, session)
        edit.loadReview(10)
        advanceUntilIdle()
        edit.save()
        advanceUntilIdle()
        assertEquals(body, source.records.single().body)
    }

    @Test
    fun editValidationAndLoadFailureDoNotSendUpdates() = runTest(dispatcher) {
        source.records = listOf(review(10, 1))
        val edit = EditReviewViewModel(repository, products, session)
        edit.loadReview(999)
        advanceUntilIdle()
        assertEquals(R.string.edit_review_load_error, edit.uiState.value.errorResId)
        edit.save()
        edit.loadReview(10)
        advanceUntilIdle()
        edit.onExperienceChange("   ")
        edit.save()
        assertEquals(R.string.edit_review_validation_error, edit.uiState.value.errorResId)
        edit.onExperienceChange("Valid body")
        edit.onRatingChange(0)
        edit.save()
        advanceUntilIdle()
        assertEquals(0, source.updates)
        assertFalse(edit.uiState.value.saved)
    }

}

private fun user(id: Int) =
    UserDto(id, "user@example.com", "user$id", null, "Biography", null, null, true, "", "")

private fun product() = ProductDto(7, 1, 1, "Phone", "Model", null, null, null, null, true, "", "")

private fun review(id: Int, owner: Int) = ReviewDto(id, owner, 7, 4, "Title", "Body", true, "", "")

private class FakeUsers : UsersRemoteDataSource, FollowRemoteDataSource {
    override suspend fun getFollows() = emptyList<FollowDto>()
    override suspend fun getUsers(excludeUserId: Int?) = listOf(user(1), user(2))

    override suspend fun getUserById(userId: Int) = user(userId)
}

private class FakeProducts : ProductRemoteDataSource, BrandRemoteDataSource, CategoryRemoteDataSource {
    override suspend fun getBrands(): List<BrandDto> = emptyList()

    override suspend fun getCategories(): List<CategoryDto> = emptyList()

    override suspend fun getProducts() = listOf(product())

    override suspend fun getProductById(productId: Int) =
        product().also { require(productId == it.id) }
}

private class FakeReviews : ReviewRemoteDataSource, CommentRemoteDataSource, ReviewLikeRemoteDataSource, CommentLikeRemoteDataSource, ReviewBookmarkRemoteDataSource {
    var bookmarks = emptyList<ReviewBookmarkDto>()
    var failBookmarks = false
    override suspend fun getReviewBookmarks(): List<ReviewBookmarkDto> {
        if (failBookmarks) error("Offline")
        return bookmarks
    }
    var commentLikes = emptyList<CommentLikeDto>()
    var failCommentLikes = false
    override suspend fun getCommentLikes(): List<CommentLikeDto> {
        if (failCommentLikes) error("Offline")
        return commentLikes
    }
    var comments = emptyList<CommentDto>()
    var likes = emptyList<ReviewLikeDto>()
    var failInteractions = false

    override suspend fun getComments(): List<CommentDto> {
        if (failInteractions) error("Offline")
        return comments
    }

    override suspend fun getReviewLikes(): List<ReviewLikeDto> = likes

    var records = emptyList<ReviewDto>()
    var requestedUser: Int? = null
    var creates = 0
    var created: CreateReviewRequestDto? = null
    var updates = 0
    var deletes = 0
    var deleted: Int? = null
    var fail = false

    override suspend fun getReviews() = records

    override suspend fun getReviewById(reviewId: Int) = records.first { it.id == reviewId }

    override suspend fun getReviewsByUser(userId: Int): List<ReviewDto> {
        requestedUser = userId
        return records
    }

    override suspend fun getReviewsByProduct(productId: Int) =
        records.filter { it.article_id == productId }

    override suspend fun createReview(request: CreateReviewRequestDto): ReviewDto {
        creates++
        if (fail) error("Offline")
        created = request
        return review(30, request.userId).copy(body = request.body)
    }

    override suspend fun updateReview(reviewId: Int, request: UpdateReviewRequestDto): ReviewDto {
        if (fail) error("Offline")
        updates++
        val updated = getReviewById(reviewId).copy(body = request.body!!, rating = request.rating!!, title = request.title)
        records = records.map { if (it.id == reviewId) updated else it }
        return updated
    }

    override suspend fun deleteReview(reviewId: Int) {
        if (fail) error("Offline")
        deletes++
        deleted = reviewId
        records = records.filter { it.id != reviewId }
    }
}


/** Proporciona fotos explícitas a las pruebas existentes sin acceder a Firebase. */
private val fixtureProfileImages = com.example.devicersapp.data.datasource.ProfileImagesRemoteDataSource { ids ->
    ids.associateWith { id -> "https://example.com/firestore-avatar-$id.png" }
}
