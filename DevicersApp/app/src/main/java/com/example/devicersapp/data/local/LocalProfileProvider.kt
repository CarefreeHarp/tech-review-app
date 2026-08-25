package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileSearchResultContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.models.SavedReviewContent

/** Centraliza los perfiles locales, sus productos calificados y sus reseñas guardadas. */
object LocalProfileProvider {

    val profile = ProfileContent(
        id = "own_profile",
        avatarResId = R.drawable.profile_avatar_00,
        handleResId = R.string.profile_handle,
        biographyResId = R.string.profile_biography,
        stats = listOf(
            ProfileStatContent(R.string.profile_reviews_count, R.string.profile_reviews),
            ProfileStatContent(R.string.profile_followers_count, R.string.profile_followers),
            ProfileStatContent(R.string.profile_following_count, R.string.profile_following)
        )
    )

    val savedReviews = listOf(
        SavedReviewContent(reviewId = R.string.feed_product_audio),
        SavedReviewContent(reviewId = R.string.feed_product_five),
        SavedReviewContent(reviewId = R.string.feed_product_nine),
        SavedReviewContent(reviewId = R.string.feed_product_phone),
        SavedReviewContent(reviewId = R.string.feed_product_computer),
        SavedReviewContent(reviewId = R.string.feed_product_four),
        SavedReviewContent(reviewId = R.string.feed_product_six),
        SavedReviewContent(reviewId = R.string.feed_product_seven)
    )

    /** Lista de todos los perfiles públicos disponibles para la búsqueda local. */
    val profiles = listOf(
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
        ),
        ProfileSearchResultContent(
            id = "nora_mobile",
            avatarResId = R.drawable.profile_avatar_04,
            handleResId = R.string.profile_result_fourth_handle,
            interestsResId = R.string.profile_result_fourth_interests,
            reviewCountResId = R.string.profile_result_fourth_reviews
        ),
        ProfileSearchResultContent(
            id = "diego_gadgets",
            avatarResId = R.drawable.profile_avatar_05,
            handleResId = R.string.profile_result_fifth_handle,
            interestsResId = R.string.profile_result_fifth_interests,
            reviewCountResId = R.string.profile_result_fifth_reviews
        ),
        ProfileSearchResultContent(
            id = "luna_digital",
            avatarResId = R.drawable.profile_avatar_01,
            handleResId = R.string.profile_result_sixth_handle,
            interestsResId = R.string.profile_result_sixth_interests,
            reviewCountResId = R.string.profile_result_sixth_reviews
        )
    )

    /** Obtiene un perfil público por el identificador recibido en la navegación. */
    fun getPublicProfileById(profileId: String): ProfileContent? {
        val searchResult = profiles.find {
            it.id == profileId
        } ?: return null

        return ProfileContent(
            id = searchResult.id,
            avatarResId = searchResult.avatarResId,
            handleResId = searchResult.handleResId,
            biographyResId = profile.biographyResId,
            stats = profile.stats
        )
    }

    /**
     * Perfiles referenciados por reseñas y comentarios. Centralizar aquí sus avatares evita
     * duplicar información de usuario dentro de las conversaciones.
     */
    private val conversationProfiles = listOf(
        conversationProfile("reply_one", R.drawable.profile_avatar_02, R.string.review_reply_author_one),
        conversationProfile("reply_two", R.drawable.profile_avatar_00, R.string.review_reply_author_two),
        conversationProfile("reply_three", R.drawable.profile_avatar_01, R.string.review_reply_author_three),
        conversationProfile("reply_four", R.drawable.profile_avatar_03, R.string.review_reply_author_four),
        conversationProfile("reply_five", R.drawable.profile_avatar_04, R.string.review_reply_author_five),
        conversationProfile("reply_six", R.drawable.profile_avatar_05, R.string.review_reply_author_six),
        conversationProfile("reply_seven", R.drawable.profile_avatar_00, R.string.review_reply_author_seven),
        conversationProfile("reply_eight", R.drawable.profile_avatar_01, R.string.review_reply_author_eight),
        conversationProfile("reply_nine", R.drawable.profile_avatar_02, R.string.review_reply_author_nine),
        conversationProfile("reply_ten", R.drawable.profile_avatar_03, R.string.review_reply_author_ten),
        conversationProfile("reply_eleven", R.drawable.profile_avatar_04, R.string.review_reply_author_eleven)
    )

    /** Devuelve un perfil desde el identificador que almacenan las reseñas y los comentarios. */
    fun getProfileById(profileId: String): ProfileContent? =
        if (profile.id == profileId) profile
        else conversationProfiles.find { it.id == profileId }
            ?: getPublicProfileById(profileId)

    /** Construye perfiles de conversación con datos compartidos que no son visibles en el hilo. */
    private fun conversationProfile(
        id: String,
        avatarResId: Int,
        handleResId: Int
    ) = ProfileContent(
        id = id,
        avatarResId = avatarResId,
        handleResId = handleResId,
        biographyResId = profile.biographyResId,
        stats = profile.stats
    )
}
