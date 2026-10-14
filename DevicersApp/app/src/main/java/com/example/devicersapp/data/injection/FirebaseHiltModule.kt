package com.example.devicersapp.data.injection

import com.google.firebase.Firebase
import com.example.devicersapp.data.datasource.ArticleReviewsRemoteDataSource
import com.example.devicersapp.data.datasource.OwnProfileRemoteDataSource
import com.example.devicersapp.data.datasource.UserProfileRemoteDataSource
import com.example.devicersapp.data.datasource.ProfileImagesRemoteDataSource

import com.example.devicersapp.data.datasource.implementations.ArticleReviewsFirestoreDataSourceImplementation
import com.example.devicersapp.data.datasource.implementations.OwnProfileFirestoreDataSourceImplementation
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

import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.implementations.*


@Module
@InstallIn(SingletonComponent::class)
object FirebaseHiltModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth


    @Provides
    @Singleton
    fun provideFirestore(): FirebaseFirestore = Firebase.firestore


    @Provides
    @Singleton
    fun storage(): FirebaseStorage = Firebase.storage


    @Provides
    fun provideUserProfiles(
        implementation: UserProfileFirestoreDataSourceImplementation
    ): UserProfileRemoteDataSource = implementation


    @Provides
    fun provideProfileImages(
        implementation: UserProfileFirestoreDataSourceImplementation
    ): ProfileImagesRemoteDataSource = implementation


    @Provides
    fun provideOwnProfileSource(
        implementation: OwnProfileFirestoreDataSourceImplementation
    ): OwnProfileRemoteDataSource = implementation


    @Provides
    fun provideArticleReviewsSource(
        implementation: ArticleReviewsFirestoreDataSourceImplementation
    ): ArticleReviewsRemoteDataSource = implementation

    @Provides
    fun provideProductSource(
        implementation: ProductFirestoreDataSourceImplementation
    ): ProductRemoteDataSource = implementation

    @Provides
    fun provideUsersSource(
        implementation: UsersFirestoreDataSourceImplementation
    ): UsersRemoteDataSource = implementation

    @Provides
    fun provideReviewSource(
        implementation: ReviewFirestoreDataSourceImplementation
    ): ReviewRemoteDataSource = implementation

    @Provides
    fun provideReviewBookmarkSource(
        implementation: ReviewBookmarkFirestoreDataSourceImplementation
    ): ReviewBookmarkRemoteDataSource = implementation

    @Provides
    fun provideCommentSource(
        implementation: CommentFirestoreDataSourceImplementation
    ): CommentRemoteDataSource = implementation

    @Provides
    fun provideCommentLikeSource(
        implementation: CommentLikeFirestoreDataSourceImplementation
    ): CommentLikeRemoteDataSource = implementation

    @Provides
    fun provideReviewLikeSource(
        implementation: ReviewLikeFirestoreDataSourceImplementation
    ): ReviewLikeRemoteDataSource = implementation
}