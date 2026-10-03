package com.example.devicersapp.data.injection

import com.example.devicersapp.data.datasource.services.ProductRetrofitService
import com.example.devicersapp.data.datasource.services.ReviewRetrofitService
import com.example.devicersapp.data.datasource.services.UsersRetrofitService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/** Provee el cliente REST y los servicios compartidos durante toda la aplicación. */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // El emulador accede al localhost del computador mediante esta dirección.
    private const val BASE_URL = "http://10.0.2.2:3000/"

    /** Entrega el cliente HTTP compartido por los servicios de la API. */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    /** Configura Retrofit con el cliente compartido y la conversión de JSON con Gson. */
    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    /** Entrega el servicio de usuarios creado por Retrofit. */
    @Provides
    @Singleton
    fun provideUsersRetrofitService(retrofit: Retrofit): UsersRetrofitService =
        retrofit.create(UsersRetrofitService::class.java)

    /** Entrega el servicio de artículos creado por Retrofit. */
    @Provides
    @Singleton
    fun provideProductRetrofitService(retrofit: Retrofit): ProductRetrofitService =
        retrofit.create(ProductRetrofitService::class.java)

    /** Entrega el servicio de reseñas creado por Retrofit. */
    @Provides
    @Singleton
    fun provideReviewRetrofitService(retrofit: Retrofit): ReviewRetrofitService =
        retrofit.create(ReviewRetrofitService::class.java)
}
