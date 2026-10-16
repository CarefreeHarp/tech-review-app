package com.example.devicersapp.data.dto.ResponseDTO.retrofit
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
/** Respuesta de Express para reseñas; las asociaciones anidadas son opcionales según el endpoint. */
data class ReviewRetrofitDto(
    val id: Int,
    val user_id: Int,
    val article_id: Int,
    val rating: Int,
    val title: String? = null,
    val body: String,
    val likes_count: Int,
    val comments_count: Int,
    val is_active: Boolean,
    val created_at: String? = null,
    val updated_at: String? = null,
    val user: UserRetrofitDto? = null,
    val article: ProductRetrofitDto? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
/** Conserva las fechas canónicas de SQL y usa los alias solo cuando el endpoint no las devuelve. */
fun ReviewRetrofitDto.toReviewInfo(): ReviewInfo = ReviewInfo(
    id = id,
    userId = user_id,
    articleId = article_id,
    rating = rating,
    title = title,
    body = body,
    likes = likes_count,
    commentsCount = comments_count,
    isActive = is_active,
    createdAt = checkNotNull(created_at ?: createdAt) { "Fecha created_at ausente" },
    updatedAt = checkNotNull(updated_at ?: updatedAt) { "Fecha updated_at ausente" },
    user = user?.toUserInfo(),
    article = article?.toProductInfo()
)
