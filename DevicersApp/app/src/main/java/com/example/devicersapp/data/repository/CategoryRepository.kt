package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.CategoryRemoteDataSource
import com.example.devicersapp.data.dto.toCategoryInfo
import com.example.devicersapp.ui.models.CategoryInfo
import javax.inject.Inject
import kotlinx.coroutines.CancellationException

/** Consulta categorías y traduce sus DTOs al modelo del front. */
class CategoryRepository @Inject constructor(private val source: CategoryRemoteDataSource) {
    suspend fun getCategories(): Result<List<CategoryInfo>> = try {
        Result.success(source.getCategories().map { it.toCategoryInfo() })
    } catch (exception: CancellationException) {
        throw exception
    } catch (exception: Exception) {
        Result.failure(exception)
    }
}
