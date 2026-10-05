package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.BrandDto

/** Contrato remoto exclusivo de marcas; propaga los errores de consulta. */
interface BrandRemoteDataSource {
    suspend fun getBrands(): List<BrandDto>
}
