package com.example.devicersapp.data.datasource.implementations

import com.example.devicersapp.data.datasource.BrandRemoteDataSource
import com.example.devicersapp.data.datasource.services.BrandRetrofitService
import com.example.devicersapp.data.dto.BrandDto
import javax.inject.Inject

/** Implementa el acceso a marcas mediante su propio servicio Retrofit. */
class BrandRetrofitDataSourceImplementation @Inject constructor(
    private val service: BrandRetrofitService
) : BrandRemoteDataSource {
    override suspend fun getBrands(): List<BrandDto> = service.getBrands()
}
