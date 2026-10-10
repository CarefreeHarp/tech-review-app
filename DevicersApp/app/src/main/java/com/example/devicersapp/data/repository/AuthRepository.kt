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
import com.google.firebase.firestore.FirebaseFirestoreException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

/** Centraliza el acceso a las operaciones de autenticación que consume la aplicación. */
@Singleton
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    @param:ApplicationContext private val appContext: Context,
    private val sessionRepository: SessionRepository
) {
    private val _currentUserState = MutableStateFlow(authRemoteDataSource.currentUser)
    val currentUserState: StateFlow<FirebaseUser?> = _currentUserState
    val currentProfileState = sessionRepository.currentProfile
    val currentProfile get() = currentProfileState.value
    private val operationMutex = Mutex()
    private var pendingRegistrationUid: String? = null
    private var sessionRevision = 0L

    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    /** Inicia una sesión y encapsula cualquier error remoto en un resultado. */
    suspend fun signIn(email: String, password: String): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_sign_in) {
            val revision = clearSession()
            authRemoteDataSource.signIn(email.trim(), password)
            loadCurrentProfile(revision)
            pendingRegistrationUid = null
        }
    }

    /** Crea la cuenta y su perfil; el resultado solo es exitoso cuando ambos están disponibles. */
    suspend fun signUp(email: String, password: String, username: String): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_sign_up) {
            val revision = clearSession()
            val existing = authRemoteDataSource.currentUser
            if (pendingRegistrationUid != null && existing?.uid == pendingRegistrationUid &&
                existing?.email.equals(email.trim(), ignoreCase = true)) {
                // Si falló Firestore, autentica de nuevo la cuenta ya creada antes
                // de reintentar su perfil; no intenta registrar el mismo correo.
                authRemoteDataSource.signIn(email.trim(), password)
            } else {
                authRemoteDataSource.signUp(email.trim(), password)
                pendingRegistrationUid = checkNotNull(authRemoteDataSource.currentUser).uid
            }
            checkRevision(revision)
            loadCurrentProfile(revision, username.trim())
            pendingRegistrationUid = null
        }
    }

    /** Reconstruye la relación UID/ID cuando Firebase restaura una cuenta al abrir la aplicación. */
    suspend fun restoreSession(): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_profile_storage) {
            val account = authRemoteDataSource.currentUser
            if (account == null) {
                clearSession()
            } else if (currentProfile?.firebaseUid == account.uid) {
                _currentUserState.value = account
            } else {
                val revision = clearSession()
                loadCurrentProfile(revision)
            }
        }
    }

    /** Guarda el nombre de usuario exclusivamente en su perfil de Firestore. */
    suspend fun updateUsername(username: String): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_update_profile) {
            sessionRepository.updateProfile(username = username.trim())
        }
    }

    /** Cierra la sesión activa y encapsula cualquier error inesperado en un resultado. */
    fun signOut(): Result<Unit> {
        return try {
            clearSession()
            pendingRegistrationUid = null
            authRemoteDataSource.signOut()
            Result.success(Unit)
        } catch (exception: Exception) {
            authenticationFailure(R.string.auth_error_sign_out, exception)
        }
    }

    /** Actualiza la foto de perfil y devuelve el resultado de Firebase. */
    suspend fun updateProfileImage(url: String): Result<Unit> {
        return runAuthenticationOperation(R.string.auth_error_update_profile) {
            authRemoteDataSource.updateProfileImage(url)
            sessionRepository.updateProfile(profileImageUrl = url)
            _currentUserState.value = authRemoteDataSource.currentUser
        }
    }

    /** Ejecuta una operación remota y convierte errores técnicos en mensajes comprensibles. */
    private suspend fun runAuthenticationOperation(
        @StringRes defaultErrorMessage: Int,
        operation: suspend () -> Unit
    ): Result<Unit> {
        return try {
            operationMutex.withLock { operation() }
            Result.success(Unit)
        } catch (exception: CancellationException) {
            throw exception
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
        } catch (exception: FirebaseFirestoreException) {
            authenticationFailure(R.string.auth_error_profile_storage, exception)
        } catch (exception: Exception) {
            authenticationFailure(defaultErrorMessage, exception)
        }
    }

    /** Obtiene el UID de Auth; nunca toma la identidad desde un valor escrito por el usuario. */
    private suspend fun loadCurrentProfile(revision: Long, username: String? = null) {
        checkRevision(revision)
        val account = checkNotNull(authRemoteDataSource.currentUser) { "No hay una cuenta autenticada." }
        // Solo las cuentas antiguas sin perfil requieren un nombre inicial de respaldo.
        // Un perfil existente conserva siempre el username guardado en Firestore.
        val resolvedName = username ?: account.email.orEmpty().substringBefore('@')
        sessionRepository.loadProfile(
            firebaseUid = account.uid,
            email = account.email.orEmpty(),
            username = resolvedName,
            profileImageUrl = account.photoUrl?.toString()
        )
        checkRevision(revision)
        if (authRemoteDataSource.currentUser?.uid != account.uid) {
            sessionRepository.clear()
            throw CancellationException("La cuenta cambió durante la carga del perfil.")
        }
        _currentUserState.value = account
    }

    private fun clearSession(): Long {
        sessionRevision++
        sessionRepository.clear()
        _currentUserState.value = null
        return sessionRevision
    }

    /** Respeta un cierre de sesión aunque una llamada de Auth termine después. */
    private fun checkRevision(expected: Long) {
        if (expected != sessionRevision) {
            pendingRegistrationUid = null
            authRemoteDataSource.signOut()
            throw CancellationException("La sesión cambió durante la autenticación.")
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
