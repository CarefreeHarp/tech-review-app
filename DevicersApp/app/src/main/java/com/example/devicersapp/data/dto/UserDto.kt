package com.example.devicersapp.data.dto

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.*

/** Datos de la API; las relaciones anidadas solo están presentes en algunas consultas. */
data class UserDto(
    val id: Int,
    val email: String,
    val username: String,
    val firebase_uid: String?,
    val biography: String?,
    val profile_image_url: String?,
    val notifications_last_viewed_at: String?,
    val is_active: Boolean,
    val createdAt: String,
    val updatedAt: String,
    val reviews: List<ReviewDto>? = null,
)

/** Traduce los campos de la API al modelo de usuario del front. */
fun UserDto.toUserInfo(): UserInfo = UserInfo(
    id = id,
    email = email,
    username = username,
    firebaseUid = firebase_uid,
    biography = biography,
    profileImageUrl = profile_image_url,
    notificationsLastViewedAt = notifications_last_viewed_at,
    isActive = is_active,
    createdAt = createdAt,
    updatedAt = updatedAt,
    reviews = reviews.orEmpty().map { it.toReviewInfo() },
)

/** Adapta el modelo del front al contenido visual utilizado por la pantalla. */
fun UserInfo.toProfileContent(
    reviewCount: Int,
    followerCount: Int? = null,
    followingCount: Int? = null
) =
    ProfileContent(
        id = id.toString(),
        avatarResId = R.drawable.profile_avatar_00,
        handleResId = R.string.profile_handle,
        biographyResId = R.string.profile_biography,
        username = username,
        biography = biography.orEmpty(),
        imageUrl = profileImageUrl,
        stats =
            buildList {
                add(ProfileStatContent(
                    R.string.profile_reviews_count,
                    R.string.profile_reviews,
                    reviewCount.toString(),
                ))
                followerCount?.let {
                    add(ProfileStatContent(R.string.profile_followers_count, R.string.profile_followers, it.toString()))
                }
                followingCount?.let {
                    add(ProfileStatContent(R.string.profile_following_count, R.string.profile_following, it.toString()))
                }
            },
    )

/** Adapta el modelo del front al contenido visual utilizado por la pantalla. */
fun UserInfo.toSearchContent() =
    ProfileSearchResultContent(
        id = id.toString(),
        avatarResId = R.drawable.profile_avatar_00,
        handleResId = R.string.profile_handle,
        interestsResId = R.string.profile_biography,
        reviewCountResId = R.string.profile_reviews_count,
        username = username,
        biography = biography.orEmpty(),
        imageUrl = profileImageUrl,
        reviewCount = reviews.count { it.isActive },
    )
