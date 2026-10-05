package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.BrandRemoteDataSource
import com.example.devicersapp.data.dto.toBrandInfo
import com.example.devicersapp.ui.models.BrandInfo
import javax.inject.Inject

/** Consulta marcas y traduce sus DTOs al modelo del front. */
class BrandRepository @Inject constructor(private val source: BrandRemoteDataSource) {
    suspend fun getBrands(): List<BrandInfo> = source.getBrands().map { it.toBrandInfo() }
}
