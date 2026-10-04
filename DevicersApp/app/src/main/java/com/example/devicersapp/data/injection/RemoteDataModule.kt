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
}
