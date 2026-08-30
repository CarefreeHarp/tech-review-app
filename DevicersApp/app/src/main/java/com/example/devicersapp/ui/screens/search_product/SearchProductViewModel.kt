package com.example.devicersapp.ui.screens.search_product

import androidx.lifecycle.ViewModel
import com.example.devicersapp.ui.utils.search.isValidLaunchDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Conserva la consulta y los filtros con los que se acota la búsqueda de productos. */
class SearchProductViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SearchProductState())
    val uiState: StateFlow<SearchProductState> = _uiState

    /** Conserva el texto con el que se busca un producto. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { it.copy(searchText = searchText) }
    }

    /** Conserva la marca con la que se filtran los productos. */
    fun onBrandChange(brand: String) {
        _uiState.update { it.copy(brand = brand) }
    }

    /** Conserva el nombre con el que se filtran los productos. */
    fun onProductNameChange(productName: String) {
        _uiState.update { it.copy(productName = productName) }
    }

    /** Conserva la fecha de lanzamiento y vuelve a calcular sus validaciones. */
    fun onLaunchDateChange(launchDate: String) {
        _uiState.update { state -> state.withValidation(launchDate = launchDate) }
    }

    /** Cambia la categoría activa de la búsqueda. */
    fun onCategorySelected(selectedCategory: String) {
        _uiState.update { it.copy(selectedCategory = selectedCategory) }
    }

    /** Cambia la calificación mínima exigida a los productos. */
    fun onRatingChange(minimumRating: Float) {
        _uiState.update { it.copy(minimumRating = minimumRating) }
    }

    /** Cambia el criterio con el que se ordenan los resultados. */
    fun onSortChange(sortBy: String) {
        _uiState.update { it.copy(sortBy = sortBy) }
    }

    /** Restablece los filtros y conserva el texto de la búsqueda general. */
    fun onClearFilters() {
        _uiState.update { state ->
            SearchProductState(searchText = state.searchText)
        }
    }

    /** Construye un estado consistente sin duplicar las reglas de validación. */
    private fun SearchProductState.withValidation(
        launchDate: String = this.launchDate
    ): SearchProductState {
        val launchDateIsValid = launchDate.isBlank() || isValidLaunchDate(launchDate)

        return copy(
            launchDate = launchDate,
            isLaunchDateValid = launchDateIsValid,
            showLaunchDateError = launchDate.isNotBlank() &&
                launchDate.filter(Char::isDigit).length == 8 &&
                !launchDateIsValid
        )
    }
}
