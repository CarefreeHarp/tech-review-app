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
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.implementations.*

/** Provee Retrofit, sus servicios y las implementaciones de los contratos de datos remotos. */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Configura la conexión al backend del computador desde el emulador. */
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.DEVICERS_API_BASE_URL)
            // Scalars atiende el texto antes de que Gson intente interpretarlo como JSON.
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /** Entrega el servicio de usuarios creado por Retrofit. */
    @Singleton
    @Provides
    fun providesUsersRetrofitService(retrofit: Retrofit): UsersRetrofitService {
        return retrofit.create(UsersRetrofitService::class.java)
    }

    /** Entrega el servicio de artículos creado por Retrofit. */
    @Singleton
    @Provides
    fun providesProductRetrofitService(retrofit: Retrofit): ProductRetrofitService {
        return retrofit.create(ProductRetrofitService::class.java)
    }

    /** Entrega el servicio de reseñas creado por Retrofit. */
    @Singleton
    @Provides
    fun providesReviewRetrofitService(retrofit: Retrofit): ReviewRetrofitService {
        return retrofit.create(ReviewRetrofitService::class.java)
    }

    /** Asocia el contrato de artículos con su implementación basada en Retrofit. */
    @Singleton
    @Provides
    fun providesProductRemoteDataSource(service: ProductRetrofitService): ProductRemoteDataSource {
        return ProductRetrofitDataSourceImplementation(service)
    }

    /** Asocia el contrato de reseñas con su implementación basada en Retrofit. */
    @Singleton
    @Provides
    fun providesReviewRemoteDataSource(service: ReviewRetrofitService): ReviewRemoteDataSource {
        return ReviewRetrofitDataSourceImplementation(service)
    }

    /** Construye el datasource de usuarios con su servicio Retrofit. */
    @Singleton
    @Provides
    fun providesUsersRemoteDataSource(service: UsersRetrofitService): UsersRemoteDataSource {
        return UsersRetrofitDataSourceImplementation(service)
    }

    /** Construye el datasource de seguidores con su servicio Retrofit. */
    @Singleton
    @Provides
    fun providesFollowRemoteDataSource(service: FollowRetrofitService): FollowRemoteDataSource {
        return FollowRetrofitDataSourceImplementation(service)
    }

    /** Construye el datasource de comentarios con su servicio Retrofit. */
    @Singleton
    @Provides
    fun providesCommentRemoteDataSource(service: CommentRetrofitService): CommentRemoteDataSource {
        return CommentRetrofitDataSourceImplementation(service)
    }

    /** Construye el datasource de likes de reseñas con su servicio Retrofit. */
    @Singleton
    @Provides
    fun providesReviewLikeRemoteDataSource(service: ReviewLikeRetrofitService): ReviewLikeRemoteDataSource {
        return ReviewLikeRetrofitDataSourceImplementation(service)
    }

    /** Construye el datasource de likes de comentarios con su servicio Retrofit. */
    @Singleton
    @Provides
    fun providesCommentLikeRemoteDataSource(service: CommentLikeRetrofitService): CommentLikeRemoteDataSource {
        return CommentLikeRetrofitDataSourceImplementation(service)
    }

    /** Construye el datasource de reseñas guardadas con su servicio Retrofit. */
    @Singleton
    @Provides
    fun providesReviewBookmarkRemoteDataSource(service: ReviewBookmarkRetrofitService): ReviewBookmarkRemoteDataSource {
        return ReviewBookmarkRetrofitDataSourceImplementation(service)
    }

    /** Construye el datasource de marcas con su servicio Retrofit. */
    @Singleton
    @Provides
    fun providesBrandRemoteDataSource(service: BrandRetrofitService): BrandRemoteDataSource {
        return BrandRetrofitDataSourceImplementation(service)
    }

    /** Construye el datasource de categorías con su servicio Retrofit. */
    @Singleton
    @Provides
    fun providesCategoryRemoteDataSource(service: CategoryRetrofitService): CategoryRemoteDataSource {
        return CategoryRetrofitDataSourceImplementation(service)
    }

    /** Crea el servicio exclusivo de Follow. */
    @Singleton
    @Provides
    fun providesFollowRetrofitService(retrofit: Retrofit): FollowRetrofitService =
        retrofit.create(FollowRetrofitService::class.java)

    /** Crea el servicio exclusivo de Comment. */
    @Singleton
    @Provides
    fun providesCommentRetrofitService(retrofit: Retrofit): CommentRetrofitService =
        retrofit.create(CommentRetrofitService::class.java)

    /** Crea el servicio exclusivo de ReviewLike. */
    @Singleton
    @Provides
    fun providesReviewLikeRetrofitService(retrofit: Retrofit): ReviewLikeRetrofitService =
        retrofit.create(ReviewLikeRetrofitService::class.java)

    /** Crea el servicio exclusivo de CommentLike. */
    @Singleton
    @Provides
    fun providesCommentLikeRetrofitService(retrofit: Retrofit): CommentLikeRetrofitService =
        retrofit.create(CommentLikeRetrofitService::class.java)

    /** Crea el servicio exclusivo de ReviewBookmark. */
    @Singleton
    @Provides
    fun providesReviewBookmarkRetrofitService(retrofit: Retrofit): ReviewBookmarkRetrofitService =
        retrofit.create(ReviewBookmarkRetrofitService::class.java)

    /** Crea el servicio exclusivo de Brand. */
    @Singleton
    @Provides
    fun providesBrandRetrofitService(retrofit: Retrofit): BrandRetrofitService =
        retrofit.create(BrandRetrofitService::class.java)

    /** Crea el servicio exclusivo de Category. */
    @Singleton
    @Provides
    fun providesCategoryRetrofitService(retrofit: Retrofit): CategoryRetrofitService =
        retrofit.create(CategoryRetrofitService::class.java)

}
