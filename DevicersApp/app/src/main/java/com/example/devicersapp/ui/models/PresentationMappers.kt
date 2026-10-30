package com.example.devicersapp.ui.models

import com.example.devicersapp.R
import java.time.Instant

/** Construye el encabezado visible con el usuario consultado y sus conteos. */
fun UserInfo.toProfileContent(reviewCount: Int, followers: Int, following: Int) = ProfileContent(
    id = id.toString(),
    avatarResId = R.drawable.no_pfp_icon,
    handleResId = R.string.profile_handle,
    biographyResId = R.string.profile_biography,
    username = username,
    biography = biography.orEmpty(),
    imageUrl = profileImageUrl,
    stats = listOf(
        ProfileStatContent(
            numberResId = R.string.profile_reviews_count,
            labelResId = R.string.profile_reviews,
            number = reviewCount.toString()
        ),
        ProfileStatContent(
            numberResId = R.string.profile_followers_count,
            labelResId = R.string.profile_followers,
            number = followers.toString()
        ),
        ProfileStatContent(
            numberResId = R.string.profile_following_count,
            labelResId = R.string.profile_following,
            number = following.toString()
        )
    )
)

/** Proyecta un usuario a la tarjeta de búsqueda sin consultar una sesión global. */
fun UserInfo.toSearchContent(reviewCount: Int) = ProfileSearchResultContent(
    id = id.toString(),
    avatarResId = R.drawable.no_pfp_icon,
    handleResId = R.string.profile_handle,
    interestsResId = R.string.profile_biography,
    reviewCountResId = R.string.profile_reviews_count,
    username = username,
    biography = biography,
    imageUrl = profileImageUrl,
    reviewCount = reviewCount
)

/** Adapta la ficha del producto al contenido visible del detalle. */
fun ProductInfo.toProductContent() = ProductContent(
    nameResId = R.string.rate_product_name,
    brandResId = R.string.rate_product_brand,
    imageResId = R.drawable.logo_icono_claro,
    imageDescriptionResId = R.string.rate_product_image_description,
    id = id,
    name = name,
    metadata = listOf(brandName, categoryName).filter { it.isNotBlank() }.joinToString(" · "),
    imageUrl = imageUrl
)

/** Construye una tarjeta desde una reseña cuyas relaciones fueron consultadas por el repositorio. */
fun ReviewInfo.toReviewContent(): ReviewContent {
    val product = checkNotNull(article)
    val author = checkNotNull(user)
    return ReviewContent(
        authorId = userId.toString(),
        productNameResId = R.string.rate_product_name,
        productImageResId = R.drawable.logo_icono_claro,
        productMetadataResId = R.string.rate_product_brand,
        rating = rating,
        textResId = R.string.rate_product_experience,
        likes = likes ?: 0,
        comments = comments.orEmpty(),
        id = id,
        productName = product.name,
        productImageUrl = product.imageUrl,
        body = body,
        authorName = author.username,
        authorImageUrl = author.profileImageUrl,
        title = title,
        isLiked = isLiked
    )
}

/** Mantiene el hilo y las copias de los autores al proyectar el detalle de una reseña. */
fun ReviewDetailInfo.toReviewContent(): ReviewContent = review.copy(
    article = article,
    user = author,
    comments = comments.map { it.toReplyContent() }
).toReviewContent()

/** Conserva la profundidad, la identidad y el estado de reacción de cada comentario del hilo. */
private fun CommentThreadItem.toReplyContent() = ReplyContent(
    authorId = comment.userId.toString(),
    timeAgoResId = R.string.review_reply_time,
    textResId = R.string.review_reply_text_two,
    likes = likes,
    depth = depth,
    authorName = comment.userUsername,
    authorImageUrl = authorImageUrl,
    body = comment.body,
    createdAtMillis = Instant.parse(comment.createdAt).toEpochMilli(),
    id = comment.id,
    isLiked = isLiked
)

/** Proyecta un comentario recién publicado como respuesta de primer nivel, todavía sin likes propios. */
fun CommentInfo.toReplyContent(): ReplyContent =
    CommentThreadItem(comment = this, depth = 0, likes = likesCount).toReplyContent()
