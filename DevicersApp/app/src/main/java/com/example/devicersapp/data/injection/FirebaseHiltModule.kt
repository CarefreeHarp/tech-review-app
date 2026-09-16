package com.example.devicersapp.data.injection

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.storage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/** Provee las dependencias de Firebase que viven durante toda la aplicación. */
@Module
@InstallIn(SingletonComponent::class)
object FirebaseHiltModule {

    /** Entrega una única instancia compartida del servicio Firebase Authentication. */
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    fun storage(): FirebaseStorage = Firebase.storage
}
