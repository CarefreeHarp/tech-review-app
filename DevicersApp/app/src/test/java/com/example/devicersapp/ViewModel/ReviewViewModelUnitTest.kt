package com.example.devicersapp.ui.screens.review

import com.example.devicersapp.R
import com.example.devicersapp.data.repository.CommentLikeRepository
import com.example.devicersapp.data.repository.ReviewLikeRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.UserRepository
import com.example.devicersapp.ui.models.UserInfo
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ReviewViewModelUnitTest {

    private lateinit var viewModel: ReviewViewModel
    private lateinit var articleReviews: ReviewRepository
    private lateinit var users: UserRepository
    private lateinit var reviewLikes: ReviewLikeRepository
    private lateinit var commentLikes: CommentLikeRepository

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)

        articleReviews = mockk()
        users = mockk()
        reviewLikes = mockk()
        commentLikes = mockk()

        viewModel = ReviewViewModel(
            articleReviews,
            users,
            reviewLikes,
            commentLikes
        )
    }

    @Test
    fun loadReview_failure_showsErrorMessage() = runTest {

        // arrange
        val authenticatedUser = mockk<UserInfo>()

        every {
            authenticatedUser.id
        } returns 1

        coEvery {
            users.getAuthenticatedUser()
        } returns authenticatedUser

        coEvery {
            articleReviews.getReviewDetail(10, 1)
        } returns Result.failure(
            Exception("Error al cargar la reseña")
        )

        // act
        viewModel.loadReview(10)
        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.loading).isFalse()
        assertThat(state.error).isEqualTo(R.string.review_load_error)
        assertThat(state.review).isNull()
        assertThat(state.product).isNull()
    }

    @Test
    fun onViewAnswers_togglesExpandedReplies() = runTest {

        // arrange
        val initialState = viewModel.uiState.value

        assertThat(initialState.expandedReplies).isEmpty()

        // act
        viewModel.onViewAnswers(0)

        // assert
        var state = viewModel.uiState.value

        assertThat(state.expandedReplies).containsEntry(0, true)

        // act: volvemos a pulsar para ocultar las respuestas
        viewModel.onViewAnswers(0)

        // assert
        state = viewModel.uiState.value

        assertThat(state.expandedReplies).containsEntry(0, false)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
