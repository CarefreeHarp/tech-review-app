package com.example.devicersapp.data.injection
import com.example.devicersapp.BuildConfig
import com.example.devicersapp.data.datasource.services.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
/** Construye Retrofit y los servicios REST; no elige implementaciones de datos. */
@Module @InstallIn(SingletonComponent::class)
object ServiceModule {
    /** Usa la URL central de Gradle y conserva los endpoints que responden texto. */
    @Provides @Singleton
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BuildConfig.DEVICERS_API_BASE_URL)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create()).build()
    @Provides @Singleton
    fun provideUserService(retrofit: Retrofit): UserRetrofitService = retrofit.create(UserRetrofitService::class.java)
    @Provides @Singleton
    fun provideProductService(retrofit: Retrofit): ProductRetrofitService = retrofit.create(ProductRetrofitService::class.java)
    @Provides @Singleton
    fun provideReviewService(retrofit: Retrofit): ReviewRetrofitService = retrofit.create(ReviewRetrofitService::class.java)
    @Provides @Singleton
    fun provideCommentService(retrofit: Retrofit): CommentRetrofitService = retrofit.create(CommentRetrofitService::class.java)
    @Provides @Singleton
    fun provideReviewLikeService(retrofit: Retrofit): ReviewLikeRetrofitService = retrofit.create(ReviewLikeRetrofitService::class.java)
    @Provides @Singleton
    fun provideCommentLikeService(retrofit: Retrofit): CommentLikeRetrofitService = retrofit.create(CommentLikeRetrofitService::class.java)
    @Provides @Singleton
    fun provideFollowService(retrofit: Retrofit): FollowRetrofitService = retrofit.create(FollowRetrofitService::class.java)
    @Provides @Singleton
    fun provideReviewBookmarkService(retrofit: Retrofit): ReviewBookmarkRetrofitService = retrofit.create(ReviewBookmarkRetrofitService::class.java)
    @Provides @Singleton
    fun provideCategoryService(retrofit: Retrofit): CategoryRetrofitService = retrofit.create(CategoryRetrofitService::class.java)
    @Provides @Singleton
    fun provideStoreService(retrofit: Retrofit): StoreRetrofitService = retrofit.create(StoreRetrofitService::class.java)
    @Provides @Singleton
    fun provideNotificationService(retrofit: Retrofit): NotificationRetrofitService = retrofit.create(NotificationRetrofitService::class.java)
    @Provides @Singleton
    fun provideProductRequestService(retrofit: Retrofit): ProductRequestRetrofitService = retrofit.create(ProductRequestRetrofitService::class.java)
}
