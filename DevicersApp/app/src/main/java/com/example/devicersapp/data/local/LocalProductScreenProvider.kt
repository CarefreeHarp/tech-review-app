package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.RatingDistribution
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.models.ReviewContent

/** Provee los datos locales de ejemplo para el detalle y la calificación de un producto. */
object LocalProductScreenProvider {

    val product = ProductContent(
        nameResId = R.string.product_title,
        brandResId = R.string.product_brand_label,
        imageResId = R.drawable.devicers_headphones_black,
        imageDescriptionResId = R.string.review_product_image
    )

    val ratingSummary = RatingSummaryContent(
        titleResId = R.string.product_average_rating,
        averageResId = R.string.product_average_value,
        rating = 5,
        reviewCountResId = R.string.product_review_count,
        distribution = listOf(
            RatingDistribution(
                ratingResId = R.string.rating_five,
                progress = 0.88f,
                percentageResId = R.string.product_rating_five_percentage
            ),
            RatingDistribution(
                ratingResId = R.string.rating_four,
                progress = 0.52f,
                percentageResId = R.string.product_rating_four_percentage
            ),
            RatingDistribution(
                ratingResId = R.string.rating_three,
                progress = 0.17f,
                percentageResId = R.string.product_rating_three_percentage
            ),
            RatingDistribution(
                ratingResId = R.string.rating_two,
                progress = 0.07f,
                percentageResId = R.string.product_rating_two_percentage
            ),
            RatingDistribution(
                ratingResId = R.string.rating_one,
                progress = 0.05f,
                percentageResId = R.string.product_rating_one_percentage
            )
        )
    )

    val reviews = listOf(
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.review_card_username,
            rating = 5,
            textResId = R.string.review_card_text,
            likes = 23
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_02,
            authorResId = R.string.product_review_author_two,
            rating = 5,
            textResId = R.string.product_review_text_two,
            likes = 42
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_03,
            authorResId = R.string.product_review_author_three,
            rating = 4,
            textResId = R.string.product_review_text_three,
            likes = 28
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_04,
            authorResId = R.string.product_review_author_four,
            rating = 5,
            textResId = R.string.product_review_text_four,
            likes = 35
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_05,
            authorResId = R.string.product_review_author_five,
            rating = 4,
            textResId = R.string.product_review_text_five,
            likes = 19
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            authorResId = R.string.product_review_author_six,
            rating = 5,
            textResId = R.string.product_review_text_six,
            likes = 31
        )
    )
}
