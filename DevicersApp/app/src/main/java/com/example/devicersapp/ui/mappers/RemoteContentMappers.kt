package com.example.devicersapp.ui.mappers

import com.example.devicersapp.R
import com.example.devicersapp.data.dto.*
import com.example.devicersapp.ui.models.*

/** Adapta las entidades remotas sin cambiar los datos de las vistas previas. */
fun UserDto.toProfileContent(reviewCount: Int) =
    ProfileContent(
        id = id.toString(),
        avatarResId = R.drawable.no_pfp_icon,
        handleResId = R.string.profile_handle,
        biographyResId = R.string.profile_biography,
        username = username,
        biography = biography.orEmpty(),
        imageUrl = profileImageUrl,
        stats =
            listOf(
                ProfileStatContent(
                    R.string.profile_reviews_count,
                    R.string.profile_reviews,
                    reviewCount.toString(),
                )
            ),
    )

fun ReviewDto.toReviewContent() =
    ReviewContent(
        id = id,
        authorId = userId.toString(),
        productNameResId = R.string.rate_product_name,
        productImageResId = R.drawable.device_00,
        productMetadataResId = R.string.rate_product_brand,
        rating = rating,
        textResId = R.string.rate_product_experience,
        likes = 0,
        productName = article?.name ?: "Producto #$articleId",
        productImageUrl = article?.imageUrl,
        body = body,
        authorName = user?.username ?: "Usuario #$userId",
        authorImageUrl = user?.profileImageUrl,
        title = title,
    )

fun UserDto.toSearchContent() =
    ProfileSearchResultContent(
        id = id.toString(),
        avatarResId = R.drawable.no_pfp_icon,
        handleResId = R.string.profile_handle,
        interestsResId = R.string.profile_biography,
        reviewCountResId = R.string.profile_reviews_count,
        username = username,
        biography = biography.orEmpty(),
        imageUrl = profileImageUrl,
        reviewCount = reviews.orEmpty().size,
    )

fun ProductDto.toProductContent() =
    ProductContent(
        nameResId = R.string.rate_product_name,
        brandResId = R.string.rate_product_brand,
        imageResId = R.drawable.device_00,
        imageDescriptionResId = R.string.rate_product_image_description,
        id = id,
        name = name,
        metadata = model.orEmpty(),
        imageUrl = imageUrl,
    )

fun ProductDto.toSearchContent() =
    ProductSearchContent(
        id = id.toString(),
        categoryId = categoryId.toString(),
        searchTerms = listOfNotNull(name, model),
        nameResId = R.string.rate_product_name,
        brandResId = R.string.rate_product_brand,
        categoryResId = R.string.rate_product_brand,
        imageDescriptionResId = R.string.rate_product_image_description,
        imageResId = R.drawable.device_00,
        rating = 1,
        name = name,
        metadata = model.orEmpty(),
        imageUrl = imageUrl,
    )
