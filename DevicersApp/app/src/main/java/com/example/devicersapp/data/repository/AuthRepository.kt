package com.example.devicersapp.data.repository
import android.content.Context
import androidx.annotation.StringRes
import com.example.devicersapp.R
import com.example.devicersapp.data.datasource.AuthRemoteDataSource
import com.example.devicersapp.data.utils.Utils
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CancellationException
import javax.inject.Inject
import javax.inject.Singleton
/** Ejecuta las operaciones de Firebase Auth y conserva los mensajes específicos de cada error. */
@Singleton
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource,
    @param:ApplicationContext private val appContext: Context
) {
    val currentUser: FirebaseUser? get() = Utils.getCurrentUser()
    /** Firebase mantiene la sesión al completar el inicio de sesión. */
    suspend fun signIn(email: String, password: String): Result<Unit> =
        runAuthenticationOperation(R.string.auth_error_sign_in) { authRemoteDataSource.signIn(email.trim(), password) }
    /** Intercambia el ID token de Google por una sesión persistente de Firebase. */
    suspend fun signInWithGoogle(idToken: String): Result<Unit> =
        runAuthenticationOperation(R.string.auth_error_sign_in) { authRemoteDataSource.signInWithGoogle(idToken) }
    /** Crea solo la cuenta Auth; el registro del perfil corresponde al repositorio de usuarios. */
    suspend fun signUp(email: String, password: String): Result<Unit> =
        runAuthenticationOperation(R.string.auth_error_sign_up) { authRemoteDataSource.signUp(email.trim(), password) }
    /** Cierra directamente la sesión mantenida por Firebase. */
    fun signOut(): Result<Unit> = try {
        authRemoteDataSource.signOut()
        Result.success(Unit)
    } catch (exception: Exception) { authenticationFailure(R.string.auth_error_sign_out, exception) }
    /** Actualiza la imagen de la cuenta Auth, independientemente de la base de datos seleccionada. */
    suspend fun updateProfileImage(url: String): Result<Unit> =
        runAuthenticationOperation(R.string.auth_error_update_profile) { authRemoteDataSource.updateProfileImage(url) }
    /** Ejecuta una operación remota y convierte errores técnicos en mensajes comprensibles. */
    private suspend fun runAuthenticationOperation(
        @StringRes defaultErrorMessage: Int,
        operation: suspend () -> Unit
    ): Result<Unit> {
        return try {
            operation()
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
