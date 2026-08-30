package com.example.devicersapp.ui.screens.search_profile

/** Representa la consulta y los filtros activos de la búsqueda de perfiles. */
data class SearchProfileState(
    val searchText: String = "",
    val username: String = "",
    val interests: String = "",
    val minimumReviews: Float = DEFAULT_MINIMUM_REVIEWS,
    val relationship: String = DEFAULT_RELATIONSHIP,
    val sortBy: String = DEFAULT_SORT_BY
) {
    companion object {
        const val DEFAULT_MINIMUM_REVIEWS = 20f
        const val DEFAULT_RELATIONSHIP = "all"
        const val DEFAULT_SORT_BY = "alphabetical"
    }
}
