package com.example.devicersapp.data.injection

import com.example.devicersapp.data.datasource.DiscoveryRemoteDataSource
import com.example.devicersapp.data.datasource.ProductRequestsRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.DiscoveryFirestoreDataSource
import com.example.devicersapp.data.datasource.implementations.ProductRequestsFirestoreDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/** Bindings exclusivos de las tareas 12–15; no reemplaza bindings de otras pantallas. */
@Module
@InstallIn(SingletonComponent::class)
abstract class DiscoveryModule {
    @Binds abstract fun discovery(source: DiscoveryFirestoreDataSource): DiscoveryRemoteDataSource
    @Binds abstract fun requests(source: ProductRequestsFirestoreDataSource): ProductRequestsRemoteDataSource
}
