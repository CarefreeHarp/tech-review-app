package com.example.devicersapp.data.injection
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.repository.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/** Define los repositorios compartidos sin repetir constructores @Inject. */
@Module @InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides @Singleton
    fun provideUserRepository(source: UserRemoteDataSource, reviews: ReviewRepository, storage: StorageRemoteDataSource, messaging: MessagingRemoteDataSource, follows: FollowRepository): UserRepository = UserRepository(source, reviews, storage, messaging, follows)
    @Provides @Singleton
    fun provideProductRepository(source: ProductRemoteDataSource): ProductRepository = ProductRepository(source)
    @Provides @Singleton
    fun provideReviewRepository(source: ReviewRemoteDataSource, products: ProductRepository, users: UserRemoteDataSource, comments: CommentRepository, likes: ReviewLikeRemoteDataSource): ReviewRepository = ReviewRepository(source, products, users, comments, likes)
    @Provides @Singleton
    fun provideCommentRepository(source: CommentRemoteDataSource, users: UserRemoteDataSource, likes: CommentLikeRemoteDataSource): CommentRepository = CommentRepository(source, users, likes)
    @Provides @Singleton
    fun provideReviewLikeRepository(source: ReviewLikeRemoteDataSource): ReviewLikeRepository = ReviewLikeRepository(source)
    @Provides @Singleton
    fun provideCommentLikeRepository(source: CommentLikeRemoteDataSource): CommentLikeRepository = CommentLikeRepository(source)
    @Provides @Singleton
    fun provideFollowRepository(source: FollowRemoteDataSource, users: UserRemoteDataSource): FollowRepository = FollowRepository(source, users)
    @Provides @Singleton
    fun provideReviewBookmarkRepository(source: ReviewBookmarkRemoteDataSource): ReviewBookmarkRepository = ReviewBookmarkRepository(source)
    @Provides @Singleton
    fun provideCategoryRepository(source: CategoryRemoteDataSource): CategoryRepository = CategoryRepository(source)
    @Provides @Singleton
    fun provideStoreRepository(source: StoreRemoteDataSource): StoreRepository = StoreRepository(source)
    @Provides @Singleton
    fun provideNotificationRepository(source: NotificationRemoteDataSource, users: UserRemoteDataSource, reviews: ReviewRemoteDataSource, products: ProductRepository): NotificationRepository = NotificationRepository(source, users, reviews, products)
    @Provides @Singleton
    fun provideProductRequestRepository(source: ProductRequestRemoteDataSource): ProductRequestRepository = ProductRequestRepository(source)
}
