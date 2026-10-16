package com.example.devicersapp.data.datasource
import androidx.core.net.toUri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
/** Encapsula Firebase Auth, compartido por ambas opciones de persistencia de datos. */
class AuthRemoteDataSource @Inject constructor(private val auth: FirebaseAuth) {
    val currentUser: FirebaseUser? get() = auth.currentUser
    suspend fun signIn(email: String, password: String) { auth.signInWithEmailAndPassword(email, password).await() }
    suspend fun signUp(email: String, password: String) { auth.createUserWithEmailAndPassword(email, password).await() }
    fun signOut() { auth.signOut() }
    /** Actualiza la imagen de la cuenta autenticada usando la instancia inyectada. */
    suspend fun updateProfileImage(url: String) {
        val user = checkNotNull(auth.currentUser) { "Usuario no autenticado" }
        user.updateProfile(UserProfileChangeRequest.Builder().setPhotoUri(url.toUri()).build()).await()
    }
}
