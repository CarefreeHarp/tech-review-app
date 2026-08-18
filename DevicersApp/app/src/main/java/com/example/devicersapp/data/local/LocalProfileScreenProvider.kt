package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.models.ReviewContent

object LocalProfileScreenProvider {

    val profile = ProfileContent(
        avatarResId = R.drawable.profile_avatar_00,
        nameResId = R.string.profile_name,
        handleResId = R.string.profile_handle,
        biographyResId = R.string.profile_biography,
        stats = listOf(
            ProfileStatContent(R.string.profile_reviews_count, R.string.profile_reviews),
            ProfileStatContent(R.string.profile_followers_count, R.string.profile_followers),
            ProfileStatContent(R.string.profile_following_count, R.string.profile_following)
        )
    )

    val reviews = listOf(
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            authorResId = R.string.profile_handle,
            rating = 5,
            textResId = R.string.profile_review_first_text,
            likes = 23
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.profile_review_second_author,
            rating = 4,
            textResId = R.string.profile_review_second_text,
            likes = 11
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            authorResId = R.string.profile_handle,
            rating = 5,
            textResId = R.string.profile_review_third_text,
            likes = 34
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.profile_handle,
            rating = 4,
            textResId = R.string.profile_review_fourth_text,
            likes = 19
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            authorResId = R.string.profile_handle,
            rating = 5,
            textResId = R.string.profile_review_fifth_text,
            likes = 46
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.profile_handle,
            rating = 4,
            textResId = R.string.profile_review_sixth_text,
            likes = 28
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            authorResId = R.string.profile_handle,
            rating = 5,
            textResId = R.string.profile_review_seventh_text,
            likes = 52
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.profile_handle,
            rating = 4,
            textResId = R.string.profile_review_eighth_text,
            likes = 17
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            authorResId = R.string.profile_handle,
            rating = 5,
            textResId = R.string.profile_review_ninth_text,
            likes = 39
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.profile_handle,
            rating = 4,
            textResId = R.string.profile_review_tenth_text,
            likes = 24
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_00,
            authorResId = R.string.profile_handle,
            rating = 5,
            textResId = R.string.profile_review_eleventh_text,
            likes = 31
        ),
        ReviewContent(
            avatarResId = R.drawable.profile_avatar_01,
            authorResId = R.string.profile_handle,
            rating = 4,
            textResId = R.string.profile_review_twelfth_text,
            likes = 22
        )
    )
}