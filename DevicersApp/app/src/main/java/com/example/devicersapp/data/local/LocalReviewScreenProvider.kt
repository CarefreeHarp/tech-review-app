package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProductContent
import com.example.devicersapp.ui.models.ReplyContent
import com.example.devicersapp.ui.models.ReviewContent

/** Provee los datos locales de ejemplo para el detalle de una reseña y sus respuestas. */
object LocalReviewScreenProvider {

    val product = ProductContent(
        nameResId = R.string.review_product_name,
        brandResId = R.string.review_product_brand,
        imageResId = R.drawable.auriculares_logo,
        imageDescriptionResId = R.string.review_product_image,
        showImage = true
    )

    val review = ReviewContent(
        avatarResId = R.drawable.profile_avatar_02,
        authorResId = R.string.review_author,
        rating = 5,
        textResId = R.string.review_detail_text,
        likes = 128,
        timeAgoResId = R.string.review_time
    )

    val replies = listOf(
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_03,
            authorResId = R.string.review_reply_author_one,
            timeAgoResId = R.string.review_reply_time,
            textResId = R.string.review_reply_text_one
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_04,
            authorResId = R.string.review_reply_author_two,
            timeAgoResId = R.string.review_reply_time,
            textResId = R.string.review_reply_text_two
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_05,
            authorResId = R.string.review_reply_author_three,
            timeAgoResId = R.string.review_reply_time_three,
            textResId = R.string.review_reply_text_three
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.review_reply_author_four,
            timeAgoResId = R.string.review_reply_time_four,
            textResId = R.string.review_reply_text_four
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_02,
            authorResId = R.string.review_reply_author_five,
            timeAgoResId = R.string.review_reply_time_five,
            textResId = R.string.review_reply_text_five
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_03,
            authorResId = R.string.review_reply_author_six,
            timeAgoResId = R.string.review_reply_time_six,
            textResId = R.string.review_reply_text_six
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_04,
            authorResId = R.string.review_reply_author_seven,
            timeAgoResId = R.string.review_reply_time_seven,
            textResId = R.string.review_reply_text_seven
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_05,
            authorResId = R.string.review_reply_author_eight,
            timeAgoResId = R.string.review_reply_time_eight,
            textResId = R.string.review_reply_text_eight
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.review_reply_author_nine,
            timeAgoResId = R.string.review_reply_time_nine,
            textResId = R.string.review_reply_text_nine
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_02,
            authorResId = R.string.review_reply_author_ten,
            timeAgoResId = R.string.review_reply_time_ten,
            textResId = R.string.review_reply_text_ten
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_03,
            authorResId = R.string.review_reply_author_eleven,
            timeAgoResId = R.string.review_reply_time_eleven,
            textResId = R.string.review_reply_text_eleven
        ),
        ReplyContent(
            avatarResId = R.drawable.profile_avatar_04,
            authorResId = R.string.review_reply_author_twelve,
            timeAgoResId = R.string.review_reply_time_twelve,
            textResId = R.string.review_reply_text_twelve
        )
    )
}
