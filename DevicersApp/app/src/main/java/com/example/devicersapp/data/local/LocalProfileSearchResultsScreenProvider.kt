package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileSearchResultContent

/** Provee los perfiles de ejemplo que devuelve una búsqueda de usuarios. */
object LocalProfileSearchResultsScreenProvider {

    val results = listOf(
        ProfileSearchResultContent(
            id = "mariana",
            avatarResId = R.drawable.profile_avatar_01,
            handleResId = R.string.profile_result_first_handle,
            interestsResId = R.string.profile_result_first_interests,
            reviewCountResId = R.string.profile_result_first_reviews
        ),
        ProfileSearchResultContent(
            id = "camila",
            avatarResId = R.drawable.profile_avatar_02,
            handleResId = R.string.profile_result_second_handle,
            interestsResId = R.string.profile_result_second_interests,
            reviewCountResId = R.string.profile_result_second_reviews
        ),
        ProfileSearchResultContent(
            id = "audio_fan",
            avatarResId = R.drawable.profile_avatar_03,
            handleResId = R.string.profile_result_third_handle,
            interestsResId = R.string.profile_result_third_interests,
            reviewCountResId = R.string.profile_result_third_reviews
        )
    )
}
