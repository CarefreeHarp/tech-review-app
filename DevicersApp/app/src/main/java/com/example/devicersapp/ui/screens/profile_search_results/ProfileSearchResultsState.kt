package com.example.devicersapp.ui.screens.profile_search_results

import com.example.devicersapp.ui.models.ProfileSearchResultContent

/** Representa la consulta, los resultados de perfiles en la búsqueda. */
data class ProfileSearchResultsState(
    val loading: Boolean = false,
    val error: Int? = null,
    val results: List<ProfileSearchResultContent> = emptyList(),
    val searchText: String = "",
    val followedUserIds: Set<Int> = emptySet(),
    val followsInProgress: Set<Int> = emptySet(),
    val followError: Int? = null
)
