package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import javax.inject.Singleton

/** Centraliza el acceso a las operaciones de autenticación que consume la aplicación. */
@Singleton
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {
    private val _currentUserState = MutableStateFlow(authRemoteDataSource.currentUser)
    val currentUserState: StateFlow<FirebaseUser?> = _currentUserState


    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    /** Inicia una sesión con las credenciales proporcionadas. */
    suspend fun signIn(email: String, password: String) {
        authRemoteDataSource.signIn(email, password)
        _currentUserState.value = authRemoteDataSource.currentUser
    }

    /** Crea una cuenta con las credenciales proporcionadas. */
    suspend fun signUp(email: String, password: String) {
        authRemoteDataSource.signUp(email, password)
        _currentUserState.value = authRemoteDataSource.currentUser
    }

    /** Guarda el nombre público de la cuenta y publica el usuario actualizado. */
    suspend fun updateDisplayName(displayName: String) {
        authRemoteDataSource.updateDisplayName(displayName)
        _currentUserState.value = authRemoteDataSource.currentUser
    }

    /** Cierra la sesión de la cuenta autenticada. */
    fun signOut() {
        authRemoteDataSource.signOut()
        _currentUserState.value = null
    }
}
