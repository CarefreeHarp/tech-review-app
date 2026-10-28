package com.example.devicersapp.data.auth

import android.content.Context
import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.UserRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

/** Resultado de autenticar con Google antes de decidir el siguiente destino. */
enum class GoogleSignInOutcome {
    CANCELLED,
    SIGNED_IN,
    PROFILE_REQUIRED
}

/** Completa Firebase Auth y distingue las cuentas nuevas de las que ya tienen perfil. */
class GoogleSignInUseCase @Inject constructor(
    private val googleSignInManager: GoogleSignInManager,
    private val auth: AuthRepository,
    private val users: UserRepository
) {
    suspend operator fun invoke(context: Context): Result<GoogleSignInOutcome> {
        var firebaseSessionCreated = false
        return try {
            val idToken = googleSignInManager.getIdToken(context)
                ?: return Result.success(GoogleSignInOutcome.CANCELLED)
            auth.signInWithGoogle(idToken).getOrThrow()
            firebaseSessionCreated = true
            val account = checkNotNull(auth.currentUser) { "Firebase no devolvió el usuario autenticado." }
            val profile = users.findByFirebaseUid(account.uid)
            if (profile == null) {
                Result.success(GoogleSignInOutcome.PROFILE_REQUIRED)
            } else {
                users.registerFcmToken()
                Result.success(GoogleSignInOutcome.SIGNED_IN)
            }
        } catch (cancelled: CancellationException) {
            throw cancelled
        } catch (error: Exception) {
            if (firebaseSessionCreated) auth.signOut()
            Result.failure(error)
        }
    }

}
