package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.FeedReviewContent

/** Provee las reseñas locales mostradas en la pantalla principal. */
object LocalFeedScreenProvider {

    val reviews = listOf(
        FeedReviewContent(
            productName = R.string.feed_product_phone,
            productImageResId = R.drawable.electronic_phone,
            author = R.string.feed_user_phone,
            reviewText = R.string.feed_review_phone,
            likes = 125,
            timeAgo = R.string.feed_time_two_days,
            rating = 5
        ),
        FeedReviewContent(
            productName = R.string.feed_product_audio,
            productImageResId = R.drawable.auriculares_logo,
            author = R.string.feed_user_audio,
            reviewText = R.string.feed_review_audio,
            likes = 86,
            timeAgo = R.string.feed_time_one_day,
            rating = 4
        ),
        FeedReviewContent(
            productName = R.string.feed_product_computer,
            productImageResId = R.drawable.electronic_desktop,
            author = R.string.feed_user_computer,
            reviewText = R.string.feed_review_computer,
            likes = 41,
            timeAgo = R.string.feed_time_five_hours,
            rating = 3
        ),
        FeedReviewContent(
            productName = R.string.feed_product_four,
            productImageResId = R.drawable.electronic_desktop,
            author = R.string.feed_user_four,
            reviewText = R.string.feed_review_four,
            likes = 67,
            timeAgo = R.string.feed_time_one_day,
            rating = 5
        ),
        FeedReviewContent(
            productName = R.string.feed_product_five,
            productImageResId = R.drawable.auriculares_logo,
            author = R.string.feed_user_five,
            reviewText = R.string.feed_review_five,
            likes = 93,
            timeAgo = R.string.feed_time_five_hours,
            rating = 4
        ),
        FeedReviewContent(
            productName = R.string.feed_product_six,
            productImageResId = R.drawable.electronic_desktop,
            author = R.string.feed_user_six,
            reviewText = R.string.feed_review_six,
            likes = 52,
            timeAgo = R.string.feed_time_two_days,
            rating = 4
        ),
        FeedReviewContent(
            productName = R.string.feed_product_seven,
            productImageResId = R.drawable.electronic_phone,
            author = R.string.feed_user_seven,
            reviewText = R.string.feed_review_seven,
            likes = 110,
            timeAgo = R.string.feed_time_one_day,
            rating = 5
        ),
        FeedReviewContent(
            productName = R.string.feed_product_eight,
            productImageResId = R.drawable.auriculares_logo,
            author = R.string.feed_user_eight,
            reviewText = R.string.feed_review_eight,
            likes = 38,
            timeAgo = R.string.feed_time_five_hours,
            rating = 4
        ),
        FeedReviewContent(
            productName = R.string.feed_product_nine,
            productImageResId = R.drawable.electronic_desktop,
            author = R.string.feed_user_nine,
            reviewText = R.string.feed_review_nine,
            likes = 74,
            timeAgo = R.string.feed_time_two_days,
            rating = 5
        ),
        FeedReviewContent(
            productName = R.string.feed_product_ten,
            productImageResId = R.drawable.electronic_phone,
            author = R.string.feed_user_ten,
            reviewText = R.string.feed_review_ten,
            likes = 59,
            timeAgo = R.string.feed_time_one_day,
            rating = 4
        ),
        FeedReviewContent(
            productName = R.string.feed_product_eleven,
            productImageResId = R.drawable.auriculares_logo,
            author = R.string.feed_user_eleven,
            reviewText = R.string.feed_review_eleven,
            likes = 46,
            timeAgo = R.string.feed_time_five_hours,
            rating = 5
        ),
        FeedReviewContent(
            productName = R.string.feed_product_twelve,
            productImageResId = R.drawable.electronic_phone,
            author = R.string.feed_user_twelve,
            reviewText = R.string.feed_review_twelve,
            likes = 82,
            timeAgo = R.string.feed_time_two_days,
            rating = 4
        ),
        FeedReviewContent(
            productName = R.string.feed_product_thirteen,
            productImageResId = R.drawable.electronic_desktop,
            author = R.string.feed_user_thirteen,
            reviewText = R.string.feed_review_thirteen,
            likes = 31,
            timeAgo = R.string.feed_time_one_day,
            rating = 4
        )
    )
}
