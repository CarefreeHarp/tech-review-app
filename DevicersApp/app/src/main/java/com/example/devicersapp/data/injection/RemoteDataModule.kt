package com.example.devicersapp.data.injection

import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.implementations.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RemoteDataModule {
    @Binds abstract fun users(source: UsersRetrofitDataSourceImplementation): UsersRemoteDataSource

    /** Asocia el contrato de Follow con su implementación Retrofit. */
    @Binds abstract fun follow(source: FollowRetrofitDataSourceImplementation): FollowRemoteDataSource

    /** Asocia el contrato de Comment con su implementación Retrofit. */
    @Binds abstract fun comment(source: CommentRetrofitDataSourceImplementation): CommentRemoteDataSource

    /** Asocia el contrato de ReviewLike con su implementación Retrofit. */
    @Binds abstract fun reviewLike(source: ReviewLikeRetrofitDataSourceImplementation): ReviewLikeRemoteDataSource

    /** Asocia el contrato de CommentLike con su implementación Retrofit. */
    @Binds abstract fun commentLike(source: CommentLikeRetrofitDataSourceImplementation): CommentLikeRemoteDataSource

    /** Asocia el contrato de ReviewBookmark con su implementación Retrofit. */
    @Binds abstract fun reviewBookmark(source: ReviewBookmarkRetrofitDataSourceImplementation): ReviewBookmarkRemoteDataSource

    /** Asocia el contrato de Brand con su implementación Retrofit. */
    @Binds abstract fun brand(source: BrandRetrofitDataSourceImplementation): BrandRemoteDataSource

    /** Asocia el contrato de Category con su implementación Retrofit. */
    @Binds abstract fun category(source: CategoryRetrofitDataSourceImplementation): CategoryRemoteDataSource

}
