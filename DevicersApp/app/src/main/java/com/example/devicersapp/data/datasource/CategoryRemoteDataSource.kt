package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de categorías; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface CategoryRemoteDataSource {
    suspend fun getCategories(): List<CategoryInfo>
}
