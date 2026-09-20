package com.example.devicersapp.data.repository

import android.content.Context
import androidx.annotation.StringRes
import com.example.devicersapp.R
import com.example.devicersapp.data.datasource.AuthRemoteDataSource
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Centraliza el acceso a las operaciones de autenticación que consume la aplicación. */
@Singleton
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    @param:ApplicationContext private val appContext: Context
) {
    private val _currentUserState = MutableStateFlow(authRemoteDataSource.currentUser)
    val currentUserState: StateFlow<FirebaseUser?> = _currentUserState


    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    /** Inicia una sesión y encapsula cualquier error remoto en un resultado. */
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_sign_in) {
            authRemoteDataSource.signIn(email, password)
            _currentUserState.value = authRemoteDataSource.currentUser
        }
    }

    /** Crea una cuenta y encapsula cualquier error remoto en un resultado. */
    suspend fun signUp(email: String, password: String): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_sign_up) {
            authRemoteDataSource.signUp(email, password)
            _currentUserState.value = authRemoteDataSource.currentUser
        }
    }

    /** Guarda el nombre público y devuelve el resultado de la actualización remota. */
    suspend fun updateDisplayName(displayName: String): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_update_profile) {
            authRemoteDataSource.updateDisplayName(displayName)
            _currentUserState.value = authRemoteDataSource.currentUser
        }
    }

    /** Cierra la sesión activa y encapsula cualquier error inesperado en un resultado. */
    fun signOut(): Result<Unit> {
        return try {
            authRemoteDataSource.signOut()
            _currentUserState.value = null
            Result.success(Unit)
        } catch (exception: Exception) {
            authenticationFailure(R.string.auth_error_sign_out, exception)
        }
    }

    /** Actualiza la foto de perfil y devuelve el resultado de Firebase. */
    suspend fun updateProfileImage(url: String): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_update_profile) {
            authRemoteDataSource.updateProfileImage(url)
            _currentUserState.value = authRemoteDataSource.currentUser
        }
    }

    /** Ejecuta una operación remota y convierte errores técnicos en mensajes comprensibles. */
    private suspend fun runAuthenticationOperation(
        @StringRes defaultErrorMessage: Int,
        operation: suspend () -> Unit
    ): Result<Unit> {
        return try {
            operation()
            Result.success(Unit)
        } catch (exception: FirebaseAuthWeakPasswordException) {
            authenticationFailure(R.string.auth_error_weak_password, exception)
        } catch (exception: FirebaseAuthInvalidUserException) {
            authenticationFailure(R.string.auth_error_user_not_found, exception)
        } catch (exception: FirebaseAuthUserCollisionException) {
            authenticationFailure(R.string.auth_error_email_in_use, exception)
        } catch (exception: FirebaseAuthInvalidCredentialsException) {
            authenticationFailure(R.string.auth_error_invalid_credentials, exception)
        } catch (exception: FirebaseNetworkException) {
            authenticationFailure(R.string.auth_error_network, exception)
        } catch (exception: FirebaseTooManyRequestsException) {
            authenticationFailure(R.string.auth_error_too_many_requests, exception)
        } catch (exception: Exception) {
            authenticationFailure(defaultErrorMessage, exception)
        }
    }

    /** Crea un resultado fallido con un texto localizado y conserva la causa original. */
    private fun authenticationFailure(
        @StringRes messageResource: Int,
        exception: Exception
    ): Result<Unit> {
        return Result.failure(Exception(appContext.getString(messageResource), exception))
    }
}
