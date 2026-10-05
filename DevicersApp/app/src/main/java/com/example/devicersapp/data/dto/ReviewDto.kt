package com.example.devicersapp.data.dto

import java.time.Instant
import com.example.devicersapp.R
import com.example.devicersapp.ui.models.*

/** Datos de la API; las relaciones anidadas solo están presentes en algunas consultas. */
data class ReviewDto(
    val id: Int,
    val user_id: Int,
    val article_id: Int,
    val rating: Int,
    val title: String?,
    val body: String,
    val is_active: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val user: UserDto? = null,
    val article: ProductDto? = null,
)

/** Traduce los campos de la API al modelo de reseña del front. */
fun ReviewDto.toReviewInfo(): ReviewInfo = ReviewInfo(
    id = id,
    userId = user_id,
    articleId = article_id,
    rating = rating,
    title = title,
    body = body,
    isActive = is_active,
    createdAt = createdAt,
    updatedAt = updatedAt,
    user = user?.toUserInfo(),
    article = article?.toProductInfo(),
)

/** Adapta el modelo del front al contenido visual utilizado por la pantalla. */
fun ReviewInfo.toReviewContent() =
    ReviewContent(
        id = id,
        authorId = userId.toString(),
        productNameResId = R.string.rate_product_name,
        productImageResId = com.example.devicersapp.ui.utils.images.localImageResIdFor(article?.imageUrl) ?: R.drawable.logo_icono_claro,
        productMetadataResId = R.string.rate_product_brand,
        rating = rating,
        textResId = R.string.rate_product_experience,
        likes = requireNotNull(likes) { "Falta consultar los likes de la reseña" },
        comments = requireNotNull(comments) { "Falta consultar los comentarios de la reseña" },
        productName = requireNotNull(article) { "Falta consultar el producto" }.name,
        productImageUrl = article?.imageUrl,
        body = body,
        authorName = requireNotNull(user) { "Falta consultar el autor" }.username,
        authorImageUrl = user?.profileImageUrl,
        title = title,
    )

/**
 * Convierte una reseña del backend en el contenido que muestra el feed.
 *
 * Las imágenes se copian tal como llegan del backend; la UI decide cómo mostrarlas.
 *
 * @param product Artículo reseñado, con la marca y categoría ya resueltas por nombre.
 * @param author Usuario que publicó la reseña.
 */
fun ReviewInfo.toFeedReviewContent(
    product: ProductInfo,
    author: UserInfo,
    brandName: String,
    categoryName: String,
    productAverage: Float,
    likes: Int,
    comments: Int
): FeedReviewContent = FeedReviewContent(
    reviewId = id,
    productId = product.id,
    productName = product.name,
    productBrand = brandName,
    productCategory = categoryName,
    productImage = product.imageUrl,
    productAverage = productAverage,
    authorId = author.id,
    authorUsername = author.username,
    authorImage = author.profileImageUrl,
    rating = rating,
    title = title,
    body = body,
    likes = likes,
    comments = comments,
    // Sequelize serializa las fechas en ISO 8601 con zona UTC.
    createdAtMillis = Instant.parse(createdAt).toEpochMilli()
)
