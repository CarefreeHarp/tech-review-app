package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Operaciones de categorías independientes de la tecnología de persistencia. */
class CategoryRepository(private val source: CategoryRemoteDataSource) {
    suspend fun getCategories(): List<CategoryInfo> = source.getCategories()
}
