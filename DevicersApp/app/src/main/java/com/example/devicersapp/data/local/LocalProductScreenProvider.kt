package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.RatingDistribution
import com.example.devicersapp.ui.models.RatingSummaryContent
import com.example.devicersapp.ui.models.ReviewContent

object LocalProductScreenProvider {

    val product = ProductContent(
        nameResId = R.string.product_title,
        brandResId = R.string.product_brand_label,
        imageResId = R.drawable.device_00,
        imageDescriptionResId = R.string.rate_product_image_description
    )

    val ratingSummary = RatingSummaryContent(
        titleResId = R.string.product_average_rating,
        averageResId = R.string.product_average_value,
        rating = 5,
        reviewCountResId = R.string.product_review_count,
        distribution = listOf(
            RatingDistribution(R.string.rating_five, 0.88f, R.string.product_rating_five_percentage),
            RatingDistribution(R.string.rating_four, 0.52f, R.string.product_rating_four_percentage),
            RatingDistribution(R.string.rating_three, 0.17f, R.string.product_rating_three_percentage),
            RatingDistribution(R.string.rating_two, 0.07f, R.string.product_rating_two_percentage),
            RatingDistribution(R.string.rating_one, 0.05f, R.string.product_rating_one_percentage)
        )
    )

    val reviews = listOf(
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            authorResId = R.string.review_card_username,
            rating = 5,
            textResId = R.string.review_card_text,
            likes = 12,
            timeAgoResId = R.string.feed_time_five_hours
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_04,
            authorResId = R.string.product_review_author_two,
            rating = 4,
            textResId = R.string.product_review_text_two,
            likes = 8,
            timeAgoResId = R.string.feed_time_one_day
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_07,
            authorResId = R.string.product_review_author_three,
            rating = 4,
            textResId = R.string.product_review_text_three,
            likes = 5,
            timeAgoResId = R.string.feed_time_one_day
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_09,
            authorResId = R.string.product_review_author_four,
            rating = 5,
            textResId = R.string.product_review_text_four,
            likes = 15,
            timeAgoResId = R.string.feed_time_two_days
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_03,
            authorResId = R.string.product_review_author_five,
            rating = 4,
            textResId = R.string.product_review_text_five,
            likes = 9,
            timeAgoResId = R.string.feed_time_two_days
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_02,
            authorResId = R.string.product_review_author_six,
            rating = 5,
            textResId = R.string.product_review_text_six,
            likes = 11,
            timeAgoResId = R.string.feed_time_two_days
        )
    )
}