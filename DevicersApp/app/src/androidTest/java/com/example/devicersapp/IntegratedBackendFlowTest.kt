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
import com.example.devicersapp.ui.screens.edit_review.EditReviewViewModel
import com.example.devicersapp.ui.screens.own_profile.OwnProfileViewModel
import com.example.devicersapp.data.datasource.ArticleReviewsRemoteDataSource
import com.example.devicersapp.data.dto.ArticleDocumentDto
import com.example.devicersapp.data.dto.CommentDocumentDto
import com.example.devicersapp.data.dto.ReviewDocumentDto
import com.example.devicersapp.data.dto.UserSummaryDto
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ReviewInfo
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
        val users = UsersRepository(UsersRetrofitDataSourceImplementation(retrofit.create(UsersRetrofitService::class.java)), fixtureProfileImages)
        val follows = FollowRepository(FollowRetrofitDataSourceImplementation(retrofit.create(FollowRetrofitService::class.java)))
        val contentProducts = products
        val contentUsers = users
        val contentComments = CommentRepository(CommentRetrofitDataSourceImplementation(retrofit.create(CommentRetrofitService::class.java)))
        val contentReviewLikes = ReviewLikeRepository(ReviewLikeRetrofitDataSourceImplementation(retrofit.create(ReviewLikeRetrofitService::class.java)))
        val contentCommentLikes = CommentLikeRepository(CommentLikeRetrofitDataSourceImplementation(retrofit.create(CommentLikeRetrofitService::class.java)))
        val session = fixtureSession(users.getUserById(1).copy(firebaseUid = "fixture-uid"))
        val articleReviews = fixtureArticleReviewsRepository(products, reviews, users, contentComments, contentReviewLikes, contentCommentLikes, follows)
        val originalIds = reviews.getReviewsByUser(session.requireCurrentProfile().id).getOrThrow().map { it.id }.toSet()
        val productId = products.getProducts().getOrThrow().first().id
        val marker = "Integration " + System.currentTimeMillis()
        var createdId: Int? = null
        val store = ViewModelStore()
        try {
            withContext(Dispatchers.Main) {
                withTimeout(30000) {
                    val profile = ProfileViewModel(users, reviews, contentProducts, contentComments, contentReviewLikes, contentCommentLikes, follows, session).also { store.put("profile", it) }
                    profile.loadProfile("2")
                    val selected = profile.uiState.first { !it.loading }
                    assertNull(selected.error)
                    assertEquals("2", selected.profile?.id)
                    assertTrue(selected.reviews.isNotEmpty())
                    assertTrue(selected.reviews.all { it.authorId == "2" })

                    val product = ProductViewModel(articleReviews).also { store.put("product", it) }
                    product.loadProduct(productId)
                    val productState = product.uiState.first { !it.isLoading }
                    assertNull(productState.errorMessage)
                    assertEquals(productId, productState.product?.id)

                    val rate = RateProductViewModel(products, reviews, session).also { store.put("rate", it) }
                    rate.loadProduct(productState.product!!.id)
                    assertNull(rate.uiState.first { !it.loading }.error)
                    rate.onRatingChange(4)
                    rate.onTitleChange(marker)
                    rate.onExperienceChange(marker)
                    rate.publish()
                    val published = rate.uiState.first { !it.saving }
                    assertNull(published.error)
                    assertTrue(published.published)

                    val created = reviews.getReviewsByUser(session.requireCurrentProfile().id).getOrThrow().single { it.title == marker }
                    createdId = created.id
                    assertEquals(productId, created.articleId)
                    val detail = ReviewViewModel(articleReviews, session).also { store.put("detail", it) }
                    detail.loadReview(created.id)
                    val detailState = detail.uiState.first { !it.loading }
                    assertNull(detailState.error)
                    assertEquals(productId, detailState.product?.id)
                    assertEquals(session.requireCurrentProfile().id.toString(), detailState.review?.authorId)

                    val edit = EditReviewViewModel(reviews, products, session).also { store.put("edit", it) }
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
                        androidx.test.platform.app.InstrumentationRegistry.getInstrumentation().targetContext,
                        session)
                    val ownProfile = OwnProfileViewModel(StorageRepository(StorageRemoteDataSource(com.google.firebase.storage.FirebaseStorage.getInstance()), auth), fixtureOwnProfileRepository(session, reviews, contentProducts, follows), androidx.lifecycle.SavedStateHandle(), session)
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
            assertEquals(originalIds, reviews.getReviewsByUser(session.requireCurrentProfile().id).getOrThrow().map { it.id }.toSet())
        } finally {
            withContext(Dispatchers.Main) { store.clear() }
            createdId?.let { reviews.deleteReview(it).getOrThrow() }
        }
    }
}


/** Proporciona una identidad explícita a las pruebas existentes sin acceder a Firestore. */
private fun fixtureSession(profile: com.example.devicersapp.ui.models.UserInfo): SessionRepository = kotlinx.coroutines.runBlocking {
    val source = object : com.example.devicersapp.data.datasource.UserProfileRemoteDataSource {
        override suspend fun getById(userId: Int) = profile.also { check(it.id == userId) }
        override suspend fun findByFirebaseUid(firebaseUid: String) = profile
        override suspend fun createIfMissing(firebaseUid: String, email: String, username: String, profileImageUrl: String?) = profile
        override suspend fun updateProfile(userId: Int, firebaseUid: String, username: String?, profileImageUrl: String?) = profile
    }
    SessionRepository(source).also { it.loadProfile("fixture-uid", profile.email, profile.username, profile.profileImageUrl) }
}

/** Adapta las fuentes de las pruebas existentes al contrato del perfil propio. */
private fun fixtureOwnProfileRepository(
    session: SessionRepository,
    reviews: ReviewRepository,
    products: ProductRepository,
    follows: FollowRepository
): OwnProfileRepository = OwnProfileRepository(session, object : com.example.devicersapp.data.datasource.OwnProfileRemoteDataSource {
    override suspend fun getReviewsByUser(userId: Int) = reviews.getReviewsByUser(userId).getOrThrow().map {
        it.copy(article = products.getProductById(it.articleId).getOrThrow())
    }
    override suspend fun getFollowCounts(userId: Int): Pair<Int, Int> {
        val relations = follows.getFollows()
        return relations.count { it.followedId == userId } to relations.count { it.followerId == userId }
    }
    override suspend fun deleteReview(reviewId: Int, userId: Int) {
        check(reviews.getReviewById(reviewId).getOrThrow().userId == userId)
        reviews.deleteReview(reviewId).getOrThrow()
    }
})

/**
 * Adapta las fuentes de la API al contrato que Home, Product y Review leen desde Firestore.
 *
 * Cada reseña se arma con su artículo, su autor y su hilo, igual que en los documentos desnormalizados,
 * para que este flujo siga comprobando los mismos IDs del backend.
 */
private fun fixtureArticleReviewsRepository(
    products: ProductRepository,
    reviews: ReviewRepository,
    users: UsersRepository,
    comments: CommentRepository,
    reviewLikes: ReviewLikeRepository,
    commentLikes: CommentLikeRepository,
    follows: FollowRepository
): ArticleReviewsRepository = ArticleReviewsRepository(object : ArticleReviewsRemoteDataSource {
    override suspend fun getActiveReviews() = products.getProducts().getOrThrow().flatMap { product ->
        product.reviews.filter { it.isActive }.map { document(it.copy(article = product)) }
    }

    override suspend fun getActiveReviewsByArticle(articleId: Int) =
        reviews.getReviewsByProduct(articleId).getOrThrow().filter { it.isActive }.map { document(it) }

    override suspend fun getReviewById(reviewId: Int) = document(reviews.getReviewById(reviewId).getOrThrow())

    override suspend fun getArticleById(articleId: Int) = products.getProductById(articleId).getOrThrow().toDocument()

    override suspend fun getReviewLikeCounts(reviewIds: Set<Int>) =
        reviewLikes.getReviewLikes().filter { it.reviewId in reviewIds }.groupingBy { it.reviewId }.eachCount()

    override suspend fun getCommentLikeCounts(commentIds: Set<Int>) =
        commentLikes.getCommentLikes().filter { it.commentId in commentIds }.groupingBy { it.commentId }.eachCount()

    override suspend fun getFollowedUserIds(followerId: Int) =
        follows.getFollows().filter { it.followerId == followerId }.map { it.followedId }.toSet()

    override suspend fun getUsers(userIds: Set<Int>) = userIds.associateWith { id ->
        users.getUserById(id).let { UserSummaryDto(it.id, it.username, it.profileImageUrl) }
    }

    private suspend fun document(review: ReviewInfo) = ReviewDocumentDto(
        id = review.id,
        userId = review.userId,
        rating = review.rating,
        title = review.title,
        body = review.body,
        isActive = review.isActive,
        createdAt = review.createdAt,
        updatedAt = review.updatedAt,
        userUsername = review.user?.username ?: users.getUserById(review.userId).username,
        article = (review.article ?: products.getProductById(review.articleId).getOrThrow()).toDocument(),
        comments = comments.getComments().filter { it.reviewId == review.id }.map {
            CommentDocumentDto(it.id, it.userId, "", it.parentCommentId, it.body, it.isActive, it.createdAt)
        }
    )

    private fun ProductInfo.toDocument() = ArticleDocumentDto(
        id, name, model, description, imageUrl, brandId, "", categoryId, "", releaseDate, null, isActive, createdAt, updatedAt
    )
})


/** Proporciona fotos explícitas a las pruebas existentes sin acceder a Firebase. */
private val fixtureProfileImages = com.example.devicersapp.data.datasource.ProfileImagesRemoteDataSource { ids ->
    ids.associateWith { id -> "https://example.com/firestore-avatar-$id.png" }
}
