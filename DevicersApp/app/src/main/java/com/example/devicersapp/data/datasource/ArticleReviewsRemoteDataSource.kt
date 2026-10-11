package com.example.devicersapp.data.datasource

import com.example.devicersapp.data.dto.ArticleDocumentDto
import com.example.devicersapp.data.dto.ReviewDocumentDto
import com.example.devicersapp.data.dto.UserSummaryDto

/**
 * Contrato de lectura de artículos y reseñas para Home, Product y Review.
 *
 * Cada reseña llega con copias de su artículo, su autor y su hilo de comentarios, por lo que no se
 * necesitan uniones entre colecciones. Los errores se propagan al repositorio; la falta de conexión
 * se informa como [java.io.IOException].
 */
interface ArticleReviewsRemoteDataSource {
    /** Obtiene las reseñas activas de todos los artículos. */
    suspend fun getActiveReviews(): List<ReviewDocumentDto>

    /** Obtiene las reseñas activas del artículo indicado. */
    suspend fun getActiveReviewsByArticle(articleId: Int): List<ReviewDocumentDto>

    /** Obtiene una reseña aunque haya sido retirada; lanza [NoSuchElementException] si no existe. */
    suspend fun getReviewById(reviewId: Int): ReviewDocumentDto

    /** Obtiene un artículo; lanza [NoSuchElementException] si no existe. */
    suspend fun getArticleById(articleId: Int): ArticleDocumentDto

    /** Cuenta los likes de cada reseña indicada; las reseñas sin likes no aparecen en el mapa. */
    suspend fun getReviewLikeCounts(reviewIds: Set<Int>): Map<Int, Int>

    /** Cuenta los likes de cada comentario indicado; los comentarios sin likes no aparecen en el mapa. */
    suspend fun getCommentLikeCounts(commentIds: Set<Int>): Map<Int, Int>

    /** Obtiene los IDs de los usuarios que sigue la cuenta indicada. */
    suspend fun getFollowedUserIds(followerId: Int): Set<Int>

    /** Obtiene el nombre y la foto vigentes de cada perfil; los perfiles inexistentes no aparecen en el mapa. */
    suspend fun getUsers(userIds: Set<Int>): Map<Int, UserSummaryDto>
}
