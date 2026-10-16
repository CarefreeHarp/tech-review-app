package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de likes de reseñas; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface ReviewLikeRemoteDataSource {
    suspend fun getReviewLikes(reviewId: Int? = null): List<ReviewLikeInfo>
}
