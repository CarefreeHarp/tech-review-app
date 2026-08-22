package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.models.RatedProductContent
import com.example.devicersapp.ui.models.SavedReviewContent

/** Provee la información de ejemplo del perfil, sus productos calificados y sus reseñas guardadas. */
object LocalProfileProvider {

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

    val savedReviews = listOf(
        SavedReviewContent(
            productNameResId = R.string.profile_saved_first_product,
            productImageResId = R.drawable.device_01,
            imageDescriptionResId = R.string.profile_saved_image,
            authorResId = R.string.profile_saved_first_author,
            rating = 5,
            averageResId = R.string.profile_saved_first_average,
            textResId = R.string.profile_saved_first_text
        ),
        SavedReviewContent(
            productNameResId = R.string.profile_saved_second_product,
            productImageResId = R.drawable.device_04,
            imageDescriptionResId = R.string.profile_saved_image,
            authorResId = R.string.profile_saved_second_author,
            rating = 4,
            averageResId = R.string.profile_saved_second_average,
            textResId = R.string.profile_saved_second_text
        ),
        SavedReviewContent(
            productNameResId = R.string.profile_saved_third_product,
            productImageResId = R.drawable.device_03,
            imageDescriptionResId = R.string.profile_saved_image,
            authorResId = R.string.profile_saved_third_author,
            rating = 5,
            averageResId = R.string.profile_saved_third_average,
            textResId = R.string.profile_saved_third_text
        )
    )
}
