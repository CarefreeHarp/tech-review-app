package com.example.devicersapp.data.injection

import com.example.devicersapp.data.datasource.services.ProductRetrofitService
import com.example.devicersapp.data.datasource.services.ReviewRetrofitService
import com.example.devicersapp.data.datasource.services.UsersRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import com.example.devicersapp.data.datasource.ProductRemoteDataSource
import com.example.devicersapp.data.datasource.ReviewRemoteDataSource
import com.example.devicersapp.data.datasource.implementations.ProductRetrofitDataSourceImplementation
import com.example.devicersapp.data.datasource.implementations.ReviewRetrofitDataSourceImplementation

/** Provee Retrofit y los servicios compartidos durante toda la aplicación. */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Configura la conexión al backend del computador desde el emulador. */
    @Singleton
    @Provides
    fun providesRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
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

    @Singleton
    @Provides
    fun providesProductRemoteDataSource(
        service: ProductRetrofitService
    ): ProductRemoteDataSource {
        return ProductRetrofitDataSourceImplementation(service)
    }

    @Singleton
    @Provides
    fun providesReviewRemoteDataSource(
        service: ReviewRetrofitService
    ): ReviewRemoteDataSource {
        return ReviewRetrofitDataSourceImplementation(service)
    }
}
