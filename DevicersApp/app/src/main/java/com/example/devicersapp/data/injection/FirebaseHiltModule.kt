package com.example.devicersapp.data.injection

import com.google.firebase.Firebase
import com.example.devicersapp.data.datasource.OwnProfileRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.OwnProfileFirestoreDataSourceImplementation
import com.example.devicersapp.data.datasource.UserProfileRemoteDataSource
import com.example.devicersapp.data.datasource.ProfileImagesRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.UserProfileFirestoreDataSourceImplementation
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
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

    /** Entrega la base de datos compartida para guardar y recuperar los perfiles. */
    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore

    /** Selecciona Firestore como implementación del contrato de perfiles de sesión. */
    @Provides
    @Singleton
    fun provideUserProfiles(
        implementation: UserProfileFirestoreDataSourceImplementation
    ): UserProfileRemoteDataSource = implementation

    /** Usa los documentos users de Firestore como fuente de todas las fotos de perfil. */
    @Provides
    fun provideProfileImages(
        implementation: UserProfileFirestoreDataSourceImplementation
    ): ProfileImagesRemoteDataSource = implementation

    /** Conecta el perfil propio con Firestore sin cambiar los otros destinos. */
    @Provides
    fun provideOwnProfileSource(
        implementation: OwnProfileFirestoreDataSourceImplementation
    ): OwnProfileRemoteDataSource = implementation

    @Provides
    @Singleton
    fun storage(): FirebaseStorage = Firebase.storage
}
