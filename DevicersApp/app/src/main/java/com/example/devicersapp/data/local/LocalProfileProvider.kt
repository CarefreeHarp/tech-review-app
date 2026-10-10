package com.example.devicersapp.data.local

import com.example.devicersapp.R
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ProfileSearchResultContent
import com.example.devicersapp.ui.models.ProfileStatContent
import com.example.devicersapp.ui.models.SavedReviewContent

/** Centraliza los perfiles locales, sus productos calificados y sus reseñas guardadas. */
object LocalProfileProvider {

    val profile = ProfileContent(
        id = "1",
        avatarResId = R.drawable.no_pfp_icon,
        handleResId = R.string.profile_handle,
        username = "prueba1",
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
            id = "1", avatarResId = R.drawable.no_pfp_icon,
            handleResId = R.string.profile_result_first_handle,
            interestsResId = R.string.profile_result_first_interests,
            reviewCountResId = R.string.profile_result_first_reviews,
            username = "prueba1", imageUrl = null
        ),
        ProfileSearchResultContent(
            id = "2", avatarResId = R.drawable.no_pfp_icon,
            handleResId = R.string.profile_result_second_handle,
            interestsResId = R.string.profile_result_second_interests,
            reviewCountResId = R.string.profile_result_second_reviews,
            username = "prueba2", imageUrl = null
        ),
        ProfileSearchResultContent(
            id = "3", avatarResId = R.drawable.no_pfp_icon,
            handleResId = R.string.profile_result_third_handle,
            interestsResId = R.string.profile_result_third_interests,
            reviewCountResId = R.string.profile_result_third_reviews,
            username = "prueba3", imageUrl = null
        ),
        ProfileSearchResultContent(
            id = "4", avatarResId = R.drawable.no_pfp_icon,
            handleResId = R.string.profile_result_fourth_handle,
            interestsResId = R.string.profile_result_fourth_interests,
            reviewCountResId = R.string.profile_result_fourth_reviews,
            username = "prueba4", imageUrl = null
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
            username = searchResult.username,
            biographyResId = profile.biographyResId,
            stats = profile.stats
        )
    }

    /** Devuelve uno de los cuatro perfiles de muestra por su ID numérico. */
    fun getProfileById(profileId: String): ProfileContent? =
        if (profile.id == profileId) profile else getPublicProfileById(profileId)
}
