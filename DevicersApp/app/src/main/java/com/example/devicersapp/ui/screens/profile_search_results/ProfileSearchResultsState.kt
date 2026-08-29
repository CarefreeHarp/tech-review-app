package com.example.devicersapp.ui.screens.profile_search_results

import com.example.devicersapp.ui.models.ProfileSearchResultContent

/** Representa la consulta, los resultados y los perfiles seguidos en la búsqueda. */
data class ProfileSearchResultsState(
    val results: List<ProfileSearchResultContent> = emptyList(),
    val searchText: String = "",
    val followedProfileIds: Set<String> = emptySet()
)
