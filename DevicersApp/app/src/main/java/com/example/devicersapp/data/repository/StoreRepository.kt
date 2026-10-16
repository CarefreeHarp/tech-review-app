package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Operaciones de tiendas independientes de la tecnología de persistencia. */
class StoreRepository(private val source: StoreRemoteDataSource) {
    suspend fun getStores(): List<StoreInfo> = source.getStores()
}
