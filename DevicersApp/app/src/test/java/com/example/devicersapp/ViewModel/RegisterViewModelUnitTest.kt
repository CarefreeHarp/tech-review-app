package com.example.devicersapp.ui.screens.register

import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.UserRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
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
import com.example.devicersapp.data.utils.Utils
import com.example.devicersapp.ui.models.UserInfo
import com.google.firebase.auth.FirebaseUser
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelUnitTest {

    private lateinit var viewModel: RegisterViewModel
    private lateinit var authRepository: AuthRepository
    private lateinit var users: UserRepository

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        authRepository = mockk()
        users = mockk()
        mockkObject(Utils)
        viewModel = RegisterViewModel(authRepository, users)
    }


    @Test
    fun register_invalidForm_doesNotCreateAccount() = runTest {

        // arrange
        viewModel.onUsernameChange("")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmationPasswordChange("123456")

        var callbackExecuted = false
//Nuestro ViewModel no tiene una variable navigate que podamos comprobar.
// En su lugar, ejecuta una función que le proporciona la pantalla.

        // act
        viewModel.onCreateAccount {
            callbackExecuted = true
        }

        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.showValidationWarning).isTrue()
        assertThat(state.isUsernameValid).isFalse()
        assertThat(state.isEmailValid).isTrue()
        assertThat(state.isPasswordValid).isTrue()
        assertThat(state.isConfirmationPasswordValid).isTrue()
        assertThat(callbackExecuted).isFalse()
    }

    @Test
    fun register_authFailure_showsErrorMessage() = runTest {

        // arrange
        viewModel.onUsernameChange("juanito")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmationPasswordChange("123456")

        coEvery {
            authRepository.signUp(any(), any())
        } returns Result.failure(
            Exception("El correo ya está registrado")
        )

        var callbackExecuted = false

        // act
        viewModel.onCreateAccount {
            callbackExecuted = true
        }

        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.registrationErrorMessage)
            .isEqualTo("El correo ya está registrado")

        assertThat(callbackExecuted).isFalse()
    }


    @Test
    fun register_success_createsUserAndExecutesCallback() = runTest {

        // arrange
        viewModel.onUsernameChange("juanito")
        viewModel.onEmailChange("juan@example.com")
        viewModel.onPasswordChange("123456")
        viewModel.onConfirmationPasswordChange("123456")

        // Simulamos el usuario autenticado de Firebase.
        val account = mockk<FirebaseUser>()

        every { Utils.getCurrentUser() } returns account
        every { Utils.getCurrentUserId() } returns "firebase-uid-123"

        every { account.email } returns "juan@example.com"
        every { account.photoUrl } returns null

        // Simulamos el registro exitoso en Auth.
        coEvery {
            authRepository.signUp(any(), any())
        } returns Result.success(Unit)

        // Simulamos la creación del perfil.
        coEvery {
            users.createIfMissing(any())
        } returns mockk<UserInfo>()

        // Simulamos el registro exitoso del token FCM.
        coEvery {
            users.registerFcmToken()
        } returns Result.success(Unit)

        var callbackExecuted = false

        // act
        viewModel.onCreateAccount {
            callbackExecuted = true
        }

        advanceUntilIdle()

        // assert
        val state = viewModel.uiState.value

        assertThat(state.showValidationWarning).isTrue()
        assertThat(state.isUsernameValid).isTrue()
        assertThat(state.isEmailValid).isTrue()
        assertThat(state.isPasswordValid).isTrue()
        assertThat(state.isConfirmationPasswordValid).isTrue()

        assertThat(state.registrationErrorMessage).isNull()
        assertThat(callbackExecuted).isTrue()
    }


    @After
    fun tearDown() {
        unmockkObject(Utils)
        Dispatchers.resetMain()
    }
}


