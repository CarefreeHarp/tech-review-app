package com.example.devicersapp.repositoryunittests

import com.example.devicersapp.data.datasource.MessagingRemoteDataSource
import com.example.devicersapp.data.datasource.StorageRemoteDataSource
import com.example.devicersapp.data.datasource.UserRemoteDataSource
import com.example.devicersapp.data.repository.FollowRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.UserRepository
import com.example.devicersapp.ui.models.UserChanges
import com.example.devicersapp.ui.models.UserInfo
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

/** Pruebas unitarias de [UserRepository]: la fuente de datos y los repositorios que usa son mocks. */
class UserRepositoryTest {
    private val mockDataSource = mockk<UserRemoteDataSource>()
    private val reviewRepository = mockk<ReviewRepository>()
    private val storageDataSource = mockk<StorageRemoteDataSource>()
    private val messagingDataSource = mockk<MessagingRemoteDataSource>()
    private val followRepository = mockk<FollowRepository>()
    private val repository = UserRepository(
        mockDataSource, reviewRepository, storageDataSource, messagingDataSource, followRepository
    )

    /** Genera un usuario con los campos obligatorios de [UserInfo]. */
    private fun generateUser(id: Int, username: String, isActive: Boolean = true) = UserInfo(
        id = id,
        email = "$username@example.com",
        username = username,
        firebaseUid = "uid_$id",
        biography = "Bio de $username",
        profileImageUrl = "https://picsum.photos/200/200?random=$id",
        notificationsLastViewedAt = null,
        isActive = isActive,
        createdAt = "2026-10-01T10:00:00Z",
        updatedAt = "2026-10-01T10:00:00Z"
    )

    @Test
    fun `when getById is called with a valid id it returns the user`() = runTest {
        // Arrange
        coEvery { mockDataSource.getById(123) } returns generateUser(id = 123, username = "juan")
        // Act
        val result = repository.getById(123)
        // Assert
        Truth.assertThat(result.id).isEqualTo(123)
        Truth.assertThat(result.username).isEqualTo("juan")
    }

    @Test
    fun `when updateProfile receives a blank username it fails without calling the data source`() = runTest {
        // Arrange
        val changes = UserChanges(username = "   ")
        // Act
        // runCatching guarda el valor o la excepción en un Result, igual al que revisa el profesor.
        val result = runCatching { repository.updateProfile(123, changes) }
        // Assert
        Truth.assertThat(result.isFailure).isTrue()
        Truth.assertThat(result.exceptionOrNull()).isInstanceOf(IllegalArgumentException::class.java)
        // coVerify revisa cuántas veces se llamó a un mock: el cambio inválido nunca llega a la fuente de datos.
        coVerify(exactly = 0) { mockDataSource.updateProfile(any(), any()) }
    }

    @Test
    fun `when getSummaries receives empty fields it maps them and leaves out inactive users`() = runTest {
        // Arrange
        val userWithEmptyFields = UserInfo(
            id = 123,
            email = "",
            username = "",
            firebaseUid = null,
            biography = null,
            profileImageUrl = null,
            notificationsLastViewedAt = null,
            isActive = true,
            createdAt = "",
            updatedAt = ""
        )
        val inactiveUser = generateUser(id = 456, username = "inactivo", isActive = false)
        coEvery { mockDataSource.getByIds(setOf(123, 456)) } returns mapOf(123 to userWithEmptyFields, 456 to inactiveUser)
        // Act
        val result = repository.getSummaries(setOf(123, 456))
        // Assert
        Truth.assertThat(result).hasSize(1)
        Truth.assertThat(result[0].id).isEqualTo(123)
        Truth.assertThat(result[0].username).isEmpty()
        Truth.assertThat(result[0].biography).isNull()
        Truth.assertThat(result[0].profileImageUrl).isNull()
    }
}
