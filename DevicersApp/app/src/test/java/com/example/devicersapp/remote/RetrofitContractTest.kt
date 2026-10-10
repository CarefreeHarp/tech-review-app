package com.example.devicersapp.remote

import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.*
import com.example.devicersapp.data.repository.*
import com.example.devicersapp.data.datasource.implementations.*
import com.example.devicersapp.ui.models.ReviewDraft
import com.example.devicersapp.ui.models.ReviewChanges
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.RecordedRequest
import com.google.gson.JsonParser
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.*
import org.junit.Assert.*
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitContractTest {
    private val server = MockWebServer()
    private lateinit var reviews: ReviewRetrofitService
    private lateinit var users: UsersRetrofitService
    private lateinit var repository: ReviewRepository
    private lateinit var products: ProductRepository
    private lateinit var profiles: UsersRepository

    private lateinit var contentComments: CommentRepository
    private lateinit var contentReviewLikes: ReviewLikeRepository
    private lateinit var contentCommentLikes: CommentLikeRepository

    private lateinit var feedBrands: BrandRepository
    private lateinit var feedCategories: CategoryRepository

    private lateinit var saved: ReviewBookmarkRepository

    private lateinit var profileContentFollows: FollowRepository
    private val json =
        """{"id":9,"user_id":1,"article_id":7,"rating":4,"body":"Test","is_active":true,"createdAt":"","updatedAt":""}"""

    @Before
    fun setup() {
        server.start()
        val retrofit =
            Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        reviews = retrofit.create(ReviewRetrofitService::class.java)
        users = retrofit.create(UsersRetrofitService::class.java)
        val productSource = ProductRetrofitDataSourceImplementation(retrofit.create(ProductRetrofitService::class.java))
        repository = ReviewRepository(ReviewRetrofitDataSourceImplementation(reviews))
        products = ProductRepository(productSource)
        profiles = UsersRepository(UsersRetrofitDataSourceImplementation(users), fixtureProfileImages)
        val comments = CommentRepository(CommentRetrofitDataSourceImplementation(retrofit.create(CommentRetrofitService::class.java)))
        val reviewLikes = ReviewLikeRepository(ReviewLikeRetrofitDataSourceImplementation(retrofit.create(ReviewLikeRetrofitService::class.java)))
        val commentLikes = CommentLikeRepository(CommentLikeRetrofitDataSourceImplementation(retrofit.create(CommentLikeRetrofitService::class.java)))

        contentComments = comments
        contentReviewLikes = reviewLikes
        contentCommentLikes = commentLikes

        feedBrands = BrandRepository(BrandRetrofitDataSourceImplementation(retrofit.create(BrandRetrofitService::class.java)))
        feedCategories = CategoryRepository(CategoryRetrofitDataSourceImplementation(retrofit.create(CategoryRetrofitService::class.java)))
        saved = ReviewBookmarkRepository(ReviewBookmarkRetrofitDataSourceImplementation(
            retrofit.create(ReviewBookmarkRetrofitService::class.java)))

        profileContentFollows = FollowRepository(FollowRetrofitDataSourceImplementation(
            retrofit.create(FollowRetrofitService::class.java)))
    }

    @After
    fun cleanup() {
        server.shutdown()
    }

    @Test
    fun userSearchSendsExcludedUserWithoutChangingGeneralUserQueries() = runBlocking {
        server.enqueue(MockResponse().setBody("[]"))
        assertTrue(profiles.getUsers(excludeUserId = 1).isEmpty())
        assertEquals("/users?excludeUserId=1", server.takeRequest().path)
        server.enqueue(MockResponse().setBody("[]"))
        profiles.getUsers()
        assertEquals("/users", server.takeRequest().path)
    }

    @Test
    fun createUsesCamelCaseAndReadsSnakeCase() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(201).setBody(json))
        val result = repository.createReview(ReviewDraft(1, 7, 4, "Test")).getOrThrow()
        val request = server.takeRequest()
        assertEquals("POST", request.method)
        assertEquals("/reviews", request.path)
        val body = JsonParser.parseString(request.body.readUtf8()).asJsonObject
        assertEquals(1, body["userId"].asInt)
        assertEquals(7, body["articleId"].asInt)
        assertFalse(body.has("user_id"))
        assertEquals(1, result.userId)
        assertEquals(7, result.articleId)
    }

    @Test
    fun updateDoesNotChangeOwnershipAndDeleteAccepts204() = runBlocking {
        server.enqueue(MockResponse().setBody(json))
        repository.updateReview(9, ReviewChanges(rating = 5, title = "", body = "Updated")).getOrThrow()
        val request = server.takeRequest()
        assertEquals("PUT", request.method)
        assertEquals("/reviews/9", request.path)
        val body = JsonParser.parseString(request.body.readUtf8()).asJsonObject
        assertFalse(body.has("user_id"))
        assertFalse(body.has("article_id"))
        server.enqueue(MockResponse().setResponseCode(204))
        repository.deleteReview(9).getOrThrow()
        assertEquals("DELETE", server.takeRequest().method)
    }

    @Test
    fun userReviewsUsesSelectedId() = runBlocking {
        server.enqueue(MockResponse().setBody("[$json]"))
        assertEquals(1, repository.getReviewsByUser(2).getOrThrow().size)
        assertEquals("/users/2/reviews", server.takeRequest().path)
    }

    @Test
    fun missingProfileIsAnError() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404).setBody("{}"))
        try {
            profiles.getUserById(123)
            fail("Expected HTTP error")
        } catch (error: HttpException) {
            assertEquals(404, error.code())
        }
        assertEquals("/users/123", server.takeRequest().path)
    }
    /** Comprueba que los campos JSON y las relaciones anidadas llegan a modelos del front. */
    @Test
    fun repositoriesMapProductsAndUsersWithoutSerializedName() = runBlocking {
        val userJson = """{"id":2,"email":"user@example.com","username":"ana","firebase_uid":"firebase-2","biography":"Bio","profile_image_url":"https://example.com/avatar.png","notifications_last_viewed_at":"2026-10-04T10:00:00Z","is_active":true,"createdAt":"2026-10-03T10:00:00Z","updatedAt":"2026-10-04T10:00:00Z"}"""
        val nestedReview = json.dropLast(1) + ",\"user\":" + userJson + "}"
        val productJson = """{"id":7,"category_id":3,"brand_id":4,"name":"Phone","model":"Model","description":"Description","image_url":"https://example.com/phone.png","release_date":"2026-10-03","specifications":{"ram":"8 GB"},"is_active":true,"createdAt":"2026-10-03T10:00:00Z","updatedAt":"2026-10-04T10:00:00Z","reviews":[$nestedReview]}"""
        server.enqueue(MockResponse().setBody(productJson))
        val product = products.getProductById(7).getOrThrow()
        assertEquals(3, product.categoryId)
        assertEquals(4, product.brandId)
        assertEquals("https://example.com/phone.png", product.imageUrl)
        assertEquals("2026-10-03", product.releaseDate)
        assertTrue(product.isActive)
        assertEquals("8 GB", JsonParser.parseString(product.specifications).asJsonObject["ram"].asString)
        assertEquals(7, product.reviews.single().articleId)
        assertEquals("ana", product.reviews.single().user?.username)
        assertEquals("firebase-2", product.reviews.single().user?.firebaseUid)
        assertEquals("/articles/7", server.takeRequest().path)

        server.enqueue(MockResponse().setBody(userJson))
        val profile = profiles.getUserById(2)
        assertEquals("https://example.com/firestore-avatar-2.png", profile.profileImageUrl)
        assertEquals("2026-10-04T10:00:00Z", profile.notificationsLastViewedAt)
        assertTrue(profile.isActive)
        assertTrue(profile.reviews.isEmpty())
        assertEquals("/users/2", server.takeRequest().path)
    }

    /** Verifica que el repositorio combina marcas, categorías, comentarios y likes del contrato real. */
    @Test
    fun feedMapsAllSupportingDtosAndCountsOnlyActiveComments() = runBlocking {
        val user = """{"id":2,"email":"user@example.com","username":"ana","is_active":true,"createdAt":"","updatedAt":""}"""
        val review = json.replace("\"createdAt\":\"\"", "\"createdAt\":\"2026-10-03T10:00:00Z\"").dropLast(1) + ",\"user\":" + user + "}"
        val product = """[{"id":7,"category_id":3,"brand_id":4,"name":"Phone","is_active":true,"createdAt":"","updatedAt":"","reviews":[$review]}]"""
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = when (request.path) {
                    "/articles" -> product
                    "/brands" -> """[{"id":4,"name":"Brand"}]"""
                    "/categories" -> """[{"id":3,"parent_category_id":1,"name":"Audio","description":"Category"}]"""
                    "/comments" -> """[{"id":1,"review_id":9,"user_id":2,"parent_comment_id":null,"body":"Active","is_active":true,"createdAt":"","updatedAt":""},{"id":2,"review_id":9,"user_id":2,"parent_comment_id":1,"body":"Inactive","is_active":false,"createdAt":"","updatedAt":""}]"""
                    "/review-likes" -> """[{"id":1,"user_id":2,"review_id":9},{"id":2,"user_id":3,"review_id":9}]"""
                    else -> return MockResponse().setResponseCode(404)
                }
                return MockResponse().setBody(body)
            }
        }
        val card = products.getFeedReviews(feedBrands, feedCategories, contentComments, contentReviewLikes, profiles).getOrThrow().single()
        assertEquals("Brand", card.productBrand)
        assertEquals("Audio", card.productCategory)
        assertEquals("ana", card.authorUsername)
        assertEquals("https://example.com/firestore-avatar-2.png", card.authorImage)
        assertEquals(2, card.likes)
        assertEquals(1, card.comments)
        assertEquals(4f, card.productAverage)
        assertTrue(card.createdAtMillis > 0)
    }

    /** Comprueba los likes de comentarios y sus autores a partir del JSON de cada endpoint. */
    @Test
    fun detailQueriesCommentLikesAndMapsTheWholeThread() = runBlocking {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = when (request.path) {
                    "/reviews/9" -> json
                    "/articles" -> """[{"id":7,"category_id":3,"brand_id":4,"name":"API product","image_url":"device_09","is_active":true,"createdAt":"","updatedAt":""}]"""
                    "/users" -> """[{"id":1,"email":"one@example.com","username":"author","is_active":true,"createdAt":"","updatedAt":""},{"id":2,"email":"two@example.com","username":"commenter","is_active":true,"createdAt":"","updatedAt":""}]"""
                    "/comments" -> """[{"id":20,"review_id":9,"user_id":2,"parent_comment_id":null,"body":"Comment from API","is_active":true,"createdAt":"","updatedAt":""},{"id":21,"review_id":9,"user_id":1,"parent_comment_id":20,"body":"Reply from API","is_active":true,"createdAt":"","updatedAt":""},{"id":22,"review_id":99,"user_id":999,"parent_comment_id":null,"body":"Unrelated","is_active":true,"createdAt":"","updatedAt":""}]"""
                    "/review-likes" -> """[{"id":1,"user_id":2,"review_id":9}]"""
                    "/comment-likes" -> """[{"id":1,"user_id":1,"comment_id":20},{"id":2,"user_id":3,"comment_id":20},{"id":3,"user_id":2,"comment_id":21}]"""
                    else -> return MockResponse().setResponseCode(404)
                }
                return MockResponse().setBody(body)
            }
        }
        val raw = repository.getReviewById(9).getOrThrow()
        val content = products.getReviewContents(listOf(raw), profiles, contentComments, contentReviewLikes, contentCommentLikes).single().toReviewContent()
        assertEquals("API product", content.productName)
        assertEquals("device_09", content.productImageUrl)
        assertEquals("author", content.authorName)
        assertEquals("https://example.com/firestore-avatar-1.png", content.authorImageUrl)
        assertEquals(1, content.likes)
        assertEquals(listOf("Comment from API", "Reply from API"), content.comments.map { it.body })
        assertEquals(listOf("commenter", "author"), content.comments.map { it.authorName })
        assertEquals(listOf("https://example.com/firestore-avatar-2.png", "https://example.com/firestore-avatar-1.png"), content.comments.map { it.authorImageUrl })
        assertEquals(listOf(0, 1), content.comments.map { it.depth })
        assertEquals(listOf(2, 1), content.comments.map { it.likes })
        val paths = (1..6).map { server.takeRequest().path }.toSet()
        assertEquals(setOf("/reviews/9", "/articles", "/users", "/comments", "/review-likes", "/comment-likes"), paths)
    }

    /** El contrato de guardados filtra user_id y combina los datos de la reseña seleccionada. */
    @Test
    fun savedReviewsReadBookmarksFromTheirEndpoint() = runBlocking {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = when (request.path) {
                    "/review-bookmarks" -> """[{"user_id":1,"review_id":9},{"user_id":2,"review_id":999}]"""
                    "/reviews" -> "[$json]"
                    "/articles" -> """[{"id":7,"category_id":3,"brand_id":4,"name":"Saved product","image_url":"device_01","is_active":true,"createdAt":"","updatedAt":""}]"""
                    "/users" -> """[{"id":1,"email":"one@example.com","username":"author","is_active":true,"createdAt":"","updatedAt":""}]"""
                    "/comments", "/review-likes", "/comment-likes" -> "[]"
                    else -> return MockResponse().setResponseCode(404)
                }
                return MockResponse().setBody(body)
            }
        }
        val saved = saved.getSavedReviews(1, repository, products, profiles, contentComments, contentReviewLikes, contentCommentLikes).getOrThrow().single().toReviewContent()
        assertEquals(9, saved.id)
        assertEquals("Saved product", saved.productName)
        assertEquals("author", saved.authorName)
        assertEquals(0, saved.likes)
        assertTrue(saved.comments.isEmpty())
        val paths = (1..7).map { server.takeRequest().path }.toSet()
        assertEquals(setOf("/review-bookmarks", "/reviews", "/articles", "/users",
            "/comments", "/review-likes", "/comment-likes"), paths)
    }

    /** Los campos follower_id y followed_id alimentan los conteos del perfil del usuario elegido. */
    @Test
    fun profileStatsReadTheSelectedUserAndFollows() = runBlocking {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                val body = when (request.path) {
                    "/users/1" -> """{"id":1,"email":"one@example.com","username":"backend-user","biography":"Remote biography","is_active":true,"createdAt":"","updatedAt":""}"""
                    "/follows" -> """[{"follower_id":1,"followed_id":2},{"follower_id":2,"followed_id":1},{"follower_id":3,"followed_id":1}]"""
                    else -> return MockResponse().setResponseCode(404)
                }
                return MockResponse().setBody(body)
            }
        }
        val profile = profiles.getProfileContent(1, 2, profileContentFollows)
        assertEquals("Remote biography", profile.biography)
        assertEquals("backend-user", profile.username)
        assertEquals(listOf("2", "2", "1"), profile.stats.map { it.number })
        assertEquals(setOf("/users/1", "/follows"), (1..2).map { server.takeRequest().path }.toSet())
    }

    /** El borrador de actualización se traduce a snake_case antes de enviarse al backend. */
    @Test
    fun updateMapsOptionalFieldsToBackendNames() = runBlocking {
        server.enqueue(MockResponse().setBody(json))
        repository.updateReview(9, ReviewChanges(isActive = false, userId = 2, articleId = 8)).getOrThrow()
        val body = JsonParser.parseString(server.takeRequest().body.readUtf8()).asJsonObject
        assertFalse(body["is_active"].asBoolean)
        assertEquals(2, body["user_id"].asInt)
        assertEquals(8, body["article_id"].asInt)
        assertFalse(body.has("isActive"))
        assertFalse(body.has("userId"))
        assertFalse(body.has("articleId"))
        assertFalse(body.has("rating"))
    }

}


/** Proporciona fotos explícitas a las pruebas existentes sin acceder a Firebase. */
private val fixtureProfileImages = com.example.devicersapp.data.datasource.ProfileImagesRemoteDataSource { ids ->
    ids.associateWith { id -> "https://example.com/firestore-avatar-$id.png" }
}
