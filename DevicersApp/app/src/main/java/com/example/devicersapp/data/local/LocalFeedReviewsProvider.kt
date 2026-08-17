package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.FeedReviewContent

object LocalFeedReviewsProvider {

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
            productName = R.string.feed_product_computer,
            productImageResId = R.drawable.electronic_desktop,
            author = R.string.feed_user_computer,
            reviewText = R.string.feed_review_computer,
            likes = 41,
            timeAgo = R.string.feed_time_five_hours,
            rating = 3
        ),
        FeedReviewContent(
            productName = R.string.feed_product_computer,
            productImageResId = R.drawable.electronic_desktop,
            author = R.string.feed_user_computer,
            reviewText = R.string.feed_review_computer,
            likes = 41,
            timeAgo = R.string.feed_time_five_hours,
            rating = 3
        )
    )
}