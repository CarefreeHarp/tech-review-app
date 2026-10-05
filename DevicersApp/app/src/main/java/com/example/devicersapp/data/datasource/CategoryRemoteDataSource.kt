package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.CategoryDto

/** Contrato remoto exclusivo de categorías; propaga los errores de consulta. */
interface CategoryRemoteDataSource {
    suspend fun getCategories(): List<CategoryDto>
}
