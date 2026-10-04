package com.example.devicersapp.remote

import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.*
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
    }

    @After
    fun cleanup() {
        server.shutdown()
    }

    @Test
    fun createUsesCamelCaseAndReadsSnakeCase() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(201).setBody(json))
        val result = reviews.createReview(CreateReviewRequestDto(1, 7, 4, "Test"))
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
        reviews.updateReview(9, UpdateReviewRequestDto(rating = 5, title = "", body = "Updated"))
        val request = server.takeRequest()
        assertEquals("PUT", request.method)
        assertEquals("/reviews/9", request.path)
        val body = JsonParser.parseString(request.body.readUtf8()).asJsonObject
        assertFalse(body.has("user_id"))
        assertFalse(body.has("article_id"))
        server.enqueue(MockResponse().setResponseCode(204))
        reviews.deleteReview(9)
        assertEquals("DELETE", server.takeRequest().method)
    }

    @Test
    fun userReviewsUsesSelectedId() = runBlocking {
        server.enqueue(MockResponse().setBody("[$json]"))
        assertEquals(1, reviews.getReviewsByUser(2).size)
        assertEquals("/users/2/reviews", server.takeRequest().path)
    }

    @Test
    fun missingProfileIsAnError() = runBlocking {
        server.enqueue(MockResponse().setResponseCode(404).setBody("{}"))
        try {
            users.getUserById(123)
            fail("Expected HTTP error")
        } catch (error: HttpException) {
            assertEquals(404, error.code())
        }
        assertEquals("/users/123", server.takeRequest().path)
    }
}
