package com.example.devicersapp.data.datasource.implementations.retrofit
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.data.datasource.services.*
import com.example.devicersapp.data.dto.ResponseDTO.retrofit.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.retrofit.*
import com.example.devicersapp.ui.models.*
import javax.inject.Inject

import kotlinx.coroutines.tasks.await
/** Implementa tiendas con Express y convierte sus respuestas a los modelos comunes. */
class StoreRemoteDataSourceRetrofitImplementation @Inject constructor(
    private val service: StoreRetrofitService
) : StoreRemoteDataSource {
    override suspend fun getStores(): List<StoreInfo> = service.getStores().map { it.toStoreInfo() }
}
