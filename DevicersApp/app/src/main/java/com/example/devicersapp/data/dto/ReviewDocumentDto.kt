package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.FeedReviewContent
import com.example.devicersapp.ui.models.ProductInfo
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewInfo
import com.example.devicersapp.ui.models.UserInfo
import java.time.Instant

/**
 * Reseña desnormalizada: además de sus campos guarda copias de su artículo, de su autor y de su hilo.
 *
 * @param userUsername Nombre copiado al publicar; solo se usa si el perfil vigente no está disponible.
 * @param article Copia del artículo reseñado.
 * @param comments Todo el hilo en una lista plana; cada comentario indica a cuál responde.
 */
data class ReviewDocumentDto(
    val id: Int,
    val userId: Int,
    val rating: Int,
    val title: String?,
    val body: String,
    val isActive: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val userUsername: String,
    val article: ArticleDocumentDto,
    val comments: List<CommentDocumentDto>
)

/** Traduce la reseña al modelo que consumen el detalle de producto y el detalle de reseña. */
fun ReviewDocumentDto.toReviewInfo(
    author: UserInfo,
    product: ProductInfo,
    likes: Int? = null,
    comments: List<ReplyContent>? = null
): ReviewInfo = ReviewInfo(
    id = id,
    userId = userId,
    articleId = article.id,
    rating = rating,
    title = title,
    body = body,
    isActive = isActive,
    createdAt = createdAt,
    updatedAt = updatedAt,
    user = author,
    article = product,
    likes = likes,
    comments = comments
)

/** Convierte la reseña en la tarjeta del feed usando los datos del artículo copiados en ella. */
fun ReviewDocumentDto.toFeedReviewContent(
    author: UserInfo,
    productAverage: Float,
    likes: Int,
    comments: Int
): FeedReviewContent = FeedReviewContent(
    reviewId = id,
    productId = article.id,
    productName = article.name,
    productBrand = article.brandName,
    productCategory = article.categoryName,
    productImage = article.imageUrl,
    productAverage = productAverage,
    authorId = author.id,
    authorUsername = author.username,
    authorImage = author.profileImageUrl,
    rating = rating,
    title = title,
    body = body,
    likes = likes,
    comments = comments,
    // Una fecha ausente no debe impedir que se muestre el resto del feed.
    createdAtMillis = createdAt.toEpochMillisOrNull() ?: 0L
)

/** Interpreta una fecha ISO 8601; devuelve `null` si el texto está vacío o no tiene ese formato. */
fun String.toEpochMillisOrNull(): Long? = runCatching { Instant.parse(this).toEpochMilli() }.getOrNull()
