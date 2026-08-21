package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.FeedReviewContent

/** Provee las reseñas de ejemplo que alimentan el feed editorial de la pantalla principal. */
object LocalHomeScreenProvider {

    val reviews = listOf(
        FeedReviewContent(
            productName = R.string.feed_product_phone,
            productImageResId = R.drawable.device_00,
            productMetadata = R.string.feed_metadata_phone,
            author = R.string.feed_user_phone,
            avatarResId = R.drawable.profile_avatar_01,
            reviewText = R.string.feed_review_phone,
            likes = 125,
            comments = 18,
            timeAgo = R.string.feed_time_two_days,
            rating = 5,
            productAverage = R.string.feed_average_phone
        ),
        FeedReviewContent(
            productName = R.string.feed_product_audio,
            productImageResId = R.drawable.device_01,
            productMetadata = R.string.feed_metadata_audio,
            author = R.string.feed_user_audio,
            avatarResId = R.drawable.profile_avatar_02,
            reviewText = R.string.feed_review_audio,
            likes = 86,
            comments = 11,
            timeAgo = R.string.feed_time_one_day,
            rating = 4,
            productAverage = R.string.feed_average_audio
        ),
        FeedReviewContent(
            productName = R.string.feed_product_computer,
            productImageResId = R.drawable.device_02,
            productMetadata = R.string.feed_metadata_computer,
            author = R.string.feed_user_computer,
            avatarResId = R.drawable.profile_avatar_03,
            reviewText = R.string.feed_review_computer,
            likes = 41,
            comments = 6,
            timeAgo = R.string.feed_time_five_hours,
            rating = 3,
            productAverage = R.string.feed_average_computer
        ),
        FeedReviewContent(
            productName = R.string.feed_product_four,
            productImageResId = R.drawable.device_03,
            productMetadata = R.string.feed_metadata_four,
            author = R.string.feed_user_four,
            avatarResId = R.drawable.profile_avatar_04,
            reviewText = R.string.feed_review_four,
            likes = 67,
            comments = 14,
            timeAgo = R.string.feed_time_one_day,
            rating = 5,
            productAverage = R.string.feed_average_four
        ),
        FeedReviewContent(
            productName = R.string.feed_product_five,
            productImageResId = R.drawable.device_04,
            productMetadata = R.string.feed_metadata_five,
            author = R.string.feed_user_five,
            avatarResId = R.drawable.profile_avatar_05,
            reviewText = R.string.feed_review_five,
            likes = 93,
            comments = 21,
            timeAgo = R.string.feed_time_five_hours,
            rating = 4,
            productAverage = R.string.feed_average_five
        ),
        FeedReviewContent(
            productName = R.string.feed_product_six,
            productImageResId = R.drawable.device_05,
            productMetadata = R.string.feed_metadata_six,
            author = R.string.feed_user_six,
            avatarResId = R.drawable.profile_avatar_01,
            reviewText = R.string.feed_review_six,
            likes = 52,
            comments = 8,
            timeAgo = R.string.feed_time_two_days,
            rating = 4,
            productAverage = R.string.feed_average_six
        ),
        FeedReviewContent(
            productName = R.string.feed_product_seven,
            productImageResId = R.drawable.device_06,
            productMetadata = R.string.feed_metadata_seven,
            author = R.string.feed_user_seven,
            avatarResId = R.drawable.profile_avatar_02,
            reviewText = R.string.feed_review_seven,
            likes = 110,
            comments = 27,
            timeAgo = R.string.feed_time_one_day,
            rating = 5,
            productAverage = R.string.feed_average_seven
        ),
        FeedReviewContent(
            productName = R.string.feed_product_eight,
            productImageResId = R.drawable.device_07,
            productMetadata = R.string.feed_metadata_eight,
            author = R.string.feed_user_eight,
            avatarResId = R.drawable.profile_avatar_03,
            reviewText = R.string.feed_review_eight,
            likes = 38,
            comments = 4,
            timeAgo = R.string.feed_time_five_hours,
            rating = 4,
            productAverage = R.string.feed_average_eight
        ),
        FeedReviewContent(
            productName = R.string.feed_product_nine,
            productImageResId = R.drawable.device_08,
            productMetadata = R.string.feed_metadata_nine,
            author = R.string.feed_user_nine,
            avatarResId = R.drawable.profile_avatar_04,
            reviewText = R.string.feed_review_nine,
            likes = 74,
            comments = 12,
            timeAgo = R.string.feed_time_two_days,
            rating = 5,
            productAverage = R.string.feed_average_nine
        ),
        FeedReviewContent(
            productName = R.string.feed_product_ten,
            productImageResId = R.drawable.device_09,
            productMetadata = R.string.feed_metadata_ten,
            author = R.string.feed_user_ten,
            avatarResId = R.drawable.profile_avatar_05,
            reviewText = R.string.feed_review_ten,
            likes = 59,
            comments = 9,
            timeAgo = R.string.feed_time_one_day,
            rating = 4,
            productAverage = R.string.feed_average_ten
        ),
        FeedReviewContent(
            productName = R.string.feed_product_eleven,
            productImageResId = R.drawable.device_00,
            productMetadata = R.string.feed_metadata_eleven,
            author = R.string.feed_user_eleven,
            avatarResId = R.drawable.profile_avatar_01,
            reviewText = R.string.feed_review_eleven,
            likes = 46,
            comments = 7,
            timeAgo = R.string.feed_time_five_hours,
            rating = 5,
            productAverage = R.string.feed_average_eleven
        ),
        FeedReviewContent(
            productName = R.string.feed_product_twelve,
            productImageResId = R.drawable.device_01,
            productMetadata = R.string.feed_metadata_twelve,
            author = R.string.feed_user_twelve,
            avatarResId = R.drawable.profile_avatar_02,
            reviewText = R.string.feed_review_twelve,
            likes = 82,
            comments = 16,
            timeAgo = R.string.feed_time_two_days,
            rating = 4,
            productAverage = R.string.feed_average_twelve
        ),
        FeedReviewContent(
            productName = R.string.feed_product_thirteen,
            productImageResId = R.drawable.device_02,
            productMetadata = R.string.feed_metadata_thirteen,
            author = R.string.feed_user_thirteen,
            avatarResId = R.drawable.profile_avatar_03,
            reviewText = R.string.feed_review_thirteen,
            likes = 31,
            comments = 5,
            timeAgo = R.string.feed_time_one_day,
            rating = 4,
            productAverage = R.string.feed_average_thirteen
        )
    )
}
