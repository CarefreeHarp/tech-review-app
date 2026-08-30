package com.example.devicersapp.ui.screens.search_product

/** Representa la consulta y los filtros activos de la búsqueda de productos. */
data class SearchProductState(
    val searchText: String = "",
    val brand: String = "",
    val productName: String = "",
    val launchDate: String = "",
    val selectedCategory: String = DEFAULT_CATEGORY,
    val minimumRating: Float = DEFAULT_MINIMUM_RATING,
    val sortBy: String = DEFAULT_SORT_BY,
    val isLaunchDateValid: Boolean = true,
    val showLaunchDateError: Boolean = false
) {
    companion object {
        const val DEFAULT_CATEGORY = "all"
        const val DEFAULT_MINIMUM_RATING = 4f
        const val DEFAULT_SORT_BY = "recent"
    }
}
