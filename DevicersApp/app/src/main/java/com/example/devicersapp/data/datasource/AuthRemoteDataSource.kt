package com.example.devicersapp.data.datasource

import android.net.Uri
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import androidx.core.net.toUri

/** Encapsula las operaciones remotas de autenticación basadas en correo y contraseña. */
class AuthRemoteDataSource @Inject constructor(
    private val auth: FirebaseAuth
) {

    /** Expone el usuario autenticado actualmente o `null` si no hay sesión activa. */
    val currentUser: FirebaseUser?
        get() = auth.currentUser

    /** Inicia sesión y espera el resultado de Firebase sin bloquear el hilo principal. */
    suspend fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    /** Registra una cuenta con correo y contraseña y espera el resultado de Firebase. */
    suspend fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
    }

    /** Actualiza el nombre público que Firebase asocia a la cuenta autenticada. */
    suspend fun updateDisplayName(displayName: String) {
        auth.currentUser?.updateProfile(
            userProfileChangeRequest { this.displayName = displayName }
        )?.await()
    }

    /** Cierra la sesión de la cuenta autenticada en el dispositivo. */
    fun signOut() {
        auth.signOut()
    }

    //
    suspend fun updateProfileImage(url:String){

        val user = Firebase.auth.currentUser
            ?: throw Exception("Usuario no autenticado")


        val profileUpdates =
            UserProfileChangeRequest.Builder()
                .setPhotoUri(url.toUri())
                .build()


        user.updateProfile(profileUpdates).await()

    }
}
