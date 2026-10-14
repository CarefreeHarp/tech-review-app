package com.example.devicersapp.data.injection

import com.example.devicersapp.data.datasource.FollowListRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.FollowListFirestoreDataSource
import com.example.devicersapp.data.datasource.ProfileRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.ProfileFirestoreDataSource
import com.example.devicersapp.data.repository.EditProfileRepository
import com.example.devicersapp.data.repository.FirebaseEditProfileRepository
import com.google.firebase.Firebase
import com.example.devicersapp.data.datasource.ArticleReviewsRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.ArticleReviewsFirestoreDataSourceImplementation
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

    /** Conecta Home, Product y Review con Firestore sin cambiar los destinos de las demás pantallas. */
    @Provides
    fun provideArticleReviewsSource(
        implementation: ArticleReviewsFirestoreDataSourceImplementation
    ): ArticleReviewsRemoteDataSource = implementation

    /** Conecta el perfil público únicamente con consultas de Firestore. */
    @Provides
    fun providePublicProfiles(implementation: ProfileFirestoreDataSource): ProfileRemoteDataSource = implementation

    /** Entrega la edición del perfil de sesión respaldada por Firebase. */
    @Provides
    fun provideProfileEditor(implementation: FirebaseEditProfileRepository): EditProfileRepository = implementation

    /** Provee exclusivamente Firestore a la lista de seguidores y seguidos. */
    @Provides
    fun provideFollowList(implementation: FollowListFirestoreDataSource): FollowListRemoteDataSource = implementation

    @Provides
    @Singleton
    fun storage(): FirebaseStorage = Firebase.storage
}
