package com.example.devicersapp.ui.models
import com.example.devicersapp.R
import java.time.Instant
/** Construye el encabezado visible con el usuario consultado y sus conteos. */
fun UserInfo.toProfileContent(reviewCount: Int, followers: Int, following: Int) = ProfileContent(
    id.toString(), R.drawable.no_pfp_icon, R.string.profile_handle, R.string.profile_biography,
    username, biography.orEmpty(), profileImageUrl,
    listOf(ProfileStatContent(R.string.profile_reviews_count, R.string.profile_reviews, reviewCount.toString()),
        ProfileStatContent(R.string.profile_followers_count, R.string.profile_followers, followers.toString()),
        ProfileStatContent(R.string.profile_following_count, R.string.profile_following, following.toString()))
)
/** Proyecta un usuario a la tarjeta de búsqueda sin consultar una sesión global. */
fun UserInfo.toSearchContent(reviewCount: Int) = ProfileSearchResultContent(id.toString(), R.drawable.no_pfp_icon,
    R.string.profile_handle, R.string.profile_biography, R.string.profile_reviews_count,
    username, biography, profileImageUrl, reviewCount)
/** Adapta la ficha del producto al contenido visible del detalle. */
fun ProductInfo.toProductContent() = ProductContent(R.string.rate_product_name, R.string.rate_product_brand,
    R.drawable.logo_icono_claro, R.string.rate_product_image_description, id = id, name = name,
    metadata = listOf(brandName, categoryName).filter { it.isNotBlank() }.joinToString(" · "), imageUrl = imageUrl)
/** Construye una tarjeta desde una reseña cuyas relaciones fueron consultadas por el repositorio. */
fun ReviewInfo.toReviewContent(): ReviewContent {
    val product = checkNotNull(article)
    val author = checkNotNull(user)
    return ReviewContent(authorId = userId.toString(), productNameResId = R.string.rate_product_name,
        productImageResId = R.drawable.logo_icono_claro, productMetadataResId = R.string.rate_product_brand,
        rating = rating, textResId = R.string.rate_product_experience, likes = 0,
        comments = comments.orEmpty(), id = id, productName = product.name, productImageUrl = product.imageUrl,
        body = body, authorName = author.username, authorImageUrl = author.profileImageUrl, title = title)
}
/** Mantiene el hilo y las copias de los autores al proyectar el detalle de una reseña. */
fun ReviewDetailInfo.toReviewContent(): ReviewContent = review.copy(article = article, user = author,
    comments = comments.map { item -> ReplyContent(item.comment.userId.toString(), R.string.review_reply_time,
        R.string.review_reply_text_two, item.likes, item.depth, item.comment.userUsername,
        item.authorImageUrl, item.comment.body, Instant.parse(item.comment.createdAt).toEpochMilli()) }
).toReviewContent()
