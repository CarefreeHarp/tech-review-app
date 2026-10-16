package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de reseñas guardadas; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface ReviewBookmarkRemoteDataSource {
    suspend fun getReviewBookmarks(userId: Int): List<ReviewBookmarkInfo>
}
