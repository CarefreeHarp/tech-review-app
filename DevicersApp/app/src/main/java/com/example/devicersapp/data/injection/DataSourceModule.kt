package com.example.devicersapp.data.injection
import com.example.devicersapp.data.config.BackendConfig
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.implementations.firestore.*
import com.example.devicersapp.data.datasource.implementations.retrofit.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/** Recibe ambas implementaciones y proporciona la elegida por la configuración global. */
@Module @InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides @Singleton
    fun provideUser(
        firestore: UserRemoteDataSourceFirestoreImplementation,
        retrofit: UserRemoteDataSourceRetrofitImplementation
    ): UserRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideProduct(
        firestore: ProductRemoteDataSourceFirestoreImplementation,
        retrofit: ProductRemoteDataSourceRetrofitImplementation
    ): ProductRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideReview(
        firestore: ReviewRemoteDataSourceFirestoreImplementation,
        retrofit: ReviewRemoteDataSourceRetrofitImplementation
    ): ReviewRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideComment(
        firestore: CommentRemoteDataSourceFirestoreImplementation,
        retrofit: CommentRemoteDataSourceRetrofitImplementation
    ): CommentRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideReviewLike(
        firestore: ReviewLikeRemoteDataSourceFirestoreImplementation,
        retrofit: ReviewLikeRemoteDataSourceRetrofitImplementation
    ): ReviewLikeRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideCommentLike(
        firestore: CommentLikeRemoteDataSourceFirestoreImplementation,
        retrofit: CommentLikeRemoteDataSourceRetrofitImplementation
    ): CommentLikeRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideFollow(
        firestore: FollowRemoteDataSourceFirestoreImplementation,
        retrofit: FollowRemoteDataSourceRetrofitImplementation
    ): FollowRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideReviewBookmark(
        firestore: ReviewBookmarkRemoteDataSourceFirestoreImplementation,
        retrofit: ReviewBookmarkRemoteDataSourceRetrofitImplementation
    ): ReviewBookmarkRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideCategory(
        firestore: CategoryRemoteDataSourceFirestoreImplementation,
        retrofit: CategoryRemoteDataSourceRetrofitImplementation
    ): CategoryRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideStore(
        firestore: StoreRemoteDataSourceFirestoreImplementation,
        retrofit: StoreRemoteDataSourceRetrofitImplementation
    ): StoreRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideNotification(
        firestore: NotificationRemoteDataSourceFirestoreImplementation,
        retrofit: NotificationRemoteDataSourceRetrofitImplementation
    ): NotificationRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
    @Provides @Singleton
    fun provideProductRequest(
        firestore: ProductRequestRemoteDataSourceFirestoreImplementation,
        retrofit: ProductRequestRemoteDataSourceRetrofitImplementation
    ): ProductRequestRemoteDataSource = if (BackendConfig.USE_FIRESTORE) firestore else retrofit
}
