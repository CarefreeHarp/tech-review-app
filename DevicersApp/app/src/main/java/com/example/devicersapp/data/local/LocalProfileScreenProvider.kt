package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.models.RatedProductContent

/** Provee la información de ejemplo del perfil y de los productos que ya calificó. */
object LocalProfileScreenProvider {

    val profile = ProfileContent(
        avatarResId = R.drawable.profile_avatar_00,
        handleResId = R.string.profile_handle,
        biographyResId = R.string.profile_biography,
        stats = listOf(
            ProfileStatContent(R.string.profile_reviews_count, R.string.profile_reviews),
            ProfileStatContent(R.string.profile_followers_count, R.string.profile_followers),
            ProfileStatContent(R.string.profile_following_count, R.string.profile_following)
        )
    )

    val ratedProducts = listOf(
        RatedProductContent(
            nameResId = R.string.profile_product_first,
            imageResId = R.drawable.device_00,
            imageDescriptionResId = R.string.profile_product_image,
            rating = 5
        ),
        RatedProductContent(
            nameResId = R.string.profile_product_second,
            imageResId = R.drawable.device_03,
            imageDescriptionResId = R.string.profile_product_image,
            rating = 5
        ),
        RatedProductContent(
            nameResId = R.string.profile_product_third,
            imageResId = R.drawable.device_04,
            imageDescriptionResId = R.string.profile_product_image,
            rating = 4
        ),
        RatedProductContent(
            nameResId = R.string.profile_product_fourth,
            imageResId = R.drawable.device_05,
            imageDescriptionResId = R.string.profile_product_image,
            rating = 5
        ),
        RatedProductContent(
            nameResId = R.string.profile_product_fifth,
            imageResId = R.drawable.device_01,
            imageDescriptionResId = R.string.profile_product_image,
            rating = 4
        ),
        RatedProductContent(
            nameResId = R.string.profile_product_sixth,
            imageResId = R.drawable.device_02,
            imageDescriptionResId = R.string.profile_product_image,
            rating = 5
        )
    )
}
