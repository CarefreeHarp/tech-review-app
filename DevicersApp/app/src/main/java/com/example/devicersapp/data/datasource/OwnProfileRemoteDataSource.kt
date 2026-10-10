package com.example.devicersapp.data.datasource

import com.example.devicersapp.ui.models.ReviewInfo

/** Define las consultas y acciones remotas exclusivas del perfil propio. */
interface OwnProfileRemoteDataSource {
    /** Obtiene los datos de las tarjetas de reseñas activas del usuario. */
    suspend fun getReviewsByUser(userId: Int): List<ReviewInfo>

    /** Devuelve los conteos de seguidores y seguidos, respectivamente. */
    suspend fun getFollowCounts(userId: Int): Pair<Int, Int>

    /** Retira una reseña tras comprobar que pertenece al usuario indicado. */
    suspend fun deleteReview(reviewId: Int, userId: Int)
}
