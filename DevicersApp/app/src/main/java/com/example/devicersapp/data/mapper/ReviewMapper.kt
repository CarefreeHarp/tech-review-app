package com.example.devicersapp.data.mapper

import com.example.devicersapp.data.dto.ProductDto
import com.example.devicersapp.data.dto.ReviewDto
import com.example.devicersapp.data.dto.UserDto
import com.example.devicersapp.ui.models.FeedReviewContent
import java.time.Instant

/**
 * Convierte una reseña del backend en el contenido que muestra el feed.
 *
 * Las imágenes se copian tal como llegan del backend; la UI decide cómo mostrarlas.
 *
 * @param product Artículo reseñado, con la marca y categoría ya resueltas por nombre.
 * @param author Usuario que publicó la reseña.
 */
fun ReviewDto.toFeedReviewContent(
    product: ProductDto,
    author: UserDto,
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
