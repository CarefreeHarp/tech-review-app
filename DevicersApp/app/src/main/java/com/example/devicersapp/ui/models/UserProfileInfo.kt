package com.example.devicersapp.ui.models
/** Perfil agregado, independiente del diseño visual y del estado de carga de una pantalla. */
data class UserProfileInfo(val user: UserInfo, val reviews: List<ReviewInfo>,
    val followerCount: Int, val followingCount: Int, val isFollowed: Boolean)
/**
 * Comentario del hilo con profundidad, reacciones y foto resuelta para su autor.
 *
 * @param authorImageUrl Copia del comentario o foto del usuario cuando esa copia está ausente.
 */
data class CommentThreadItem(
    val comment: CommentInfo,
    val depth: Int,
    val likes: Int,
    val authorImageUrl: String? = comment.userProfileImageUrl,
    val isLiked: Boolean = false
)
/** Reseña completa con las relaciones necesarias para su detalle. */
data class ReviewDetailInfo(val review: ReviewInfo, val article: ProductInfo, val author: UserInfo,
    val comments: List<CommentThreadItem>)
