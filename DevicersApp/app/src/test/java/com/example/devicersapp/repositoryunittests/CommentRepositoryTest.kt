package com.example.devicersapp.repositoryunittests

import com.example.devicersapp.data.datasource.CommentLikeRemoteDataSource
import com.example.devicersapp.data.datasource.CommentRemoteDataSource
import com.example.devicersapp.data.datasource.UserRemoteDataSource
import com.example.devicersapp.data.repository.CommentRepository
import com.example.devicersapp.ui.models.CommentInfo
import com.example.devicersapp.ui.models.UserInfo
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/** Pruebas unitarias de [CommentRepository] con comentarios, usuarios y likes simulados con mocks. */
class CommentRepositoryTest {
    private val mockDataSource = mockk<CommentRemoteDataSource>()
    private val userDataSource = mockk<UserRemoteDataSource>()
    private val commentLikeDataSource = mockk<CommentLikeRemoteDataSource>()
    private val repository = CommentRepository(mockDataSource, userDataSource, commentLikeDataSource)

    @Test
    fun `when a comment has an empty photo the thread uses the profile photo of the author`() = runTest {
        // Arrange
        val comment = CommentInfo(
            id = 1,
            reviewId = 10,
            userId = 7,
            parentCommentId = null,
            body = "Muy útil la reseña",
            isActive = true,
            createdAt = "2026-10-01T10:00:00Z",
            updatedAt = "2026-10-01T10:00:00Z",
            userUsername = "juan",
            userProfileImageUrl = ""
        )
        val author = UserInfo(
            id = 7,
            email = "juan@example.com",
            username = "juan",
            firebaseUid = "uid_7",
            biography = null,
            profileImageUrl = "https://picsum.photos/200/200?random=7",
            notificationsLastViewedAt = null,
            isActive = true,
            createdAt = "2026-10-01T10:00:00Z",
            updatedAt = "2026-10-01T10:00:00Z"
        )
        coEvery { mockDataSource.getComments(10) } returns listOf(comment)
        coEvery { userDataSource.getByIds(setOf(7)) } returns mapOf(7 to author)
        // Act
        val result = repository.getThread(reviewId = 10)
        // Assert
        Truth.assertThat(result).hasSize(1)
        Truth.assertThat(result[0].authorImageUrl).isEqualTo("https://picsum.photos/200/200?random=7")
    }
}
