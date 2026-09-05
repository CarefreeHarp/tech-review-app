package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.AuthRemoteDataSource
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject
import javax.inject.Singleton

/** Centraliza el acceso a las operaciones de autenticación que consume la aplicación. */
@Singleton
class AuthRepository @Inject constructor(
    private val authRemoteDataSource: AuthRemoteDataSource
) {

    val currentUser: FirebaseUser?
        get() = authRemoteDataSource.currentUser

    /** Inicia una sesión con las credenciales proporcionadas. */
    suspend fun signIn(email: String, password: String) {
        authRemoteDataSource.signIn(email, password)
    }

    /** Crea una cuenta con las credenciales proporcionadas. */
    suspend fun signUp(email: String, password: String) {
        authRemoteDataSource.signUp(email, password)
    }

    /** Cierra la sesión de la cuenta autenticada. */
    fun signOut() {
        authRemoteDataSource.signOut()
    }
}
