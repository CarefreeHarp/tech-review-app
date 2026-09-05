package com.example.devicersapp.ui.screens.found_products

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProductProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva y modifica el estado de la pantalla de productos encontrados. */
@HiltViewModel
class FoundProductsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(FoundProductsState())
    val uiState: StateFlow<FoundProductsState> = _uiState

    init {
        loadProducts()
    }

    /** Carga los productos disponibles para mostrar como resultados. */
    private fun loadProducts() {
        _uiState.update { currentState ->
            currentState.copy(
                results = LocalProductProvider.products
            )
        }
    }

    /** Actualiza el texto escrito en la barra de búsqueda. */
    fun onSearchTextChange(searchText: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchText = searchText
            )
        }
    }
}
