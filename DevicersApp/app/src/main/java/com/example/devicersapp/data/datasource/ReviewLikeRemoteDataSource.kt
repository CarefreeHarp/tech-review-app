package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de likes de reseñas; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface ReviewLikeRemoteDataSource {
    suspend fun getReviewLikes(reviewId: Int? = null): List<ReviewLikeInfo>
    /** Devuelve, de las reseñas indicadas, aquellas a las que el usuario ya dio like. */
    suspend fun getLikedReviewIds(userId: Int, reviewIds: Set<Int>): Set<Int>
    /** Da like si el usuario aún no lo había dado o lo quita si ya existía, notificando al autor de la reseña. */
    suspend fun sendReviewLike(reviewId: Int, userId: Int): ReviewLikeStatus
}
