package com.example.devicersapp.ui.screens.request_product

import androidx.lifecycle.ViewModel
import com.example.devicersapp.ui.utils.search.formatLaunchDate
import com.example.devicersapp.ui.utils.search.isValidLaunchDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Gestiona la validación y las acciones de presentación de la solicitud de producto. */
class RequestProductViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(RequestProductState())
    val uiState: StateFlow<RequestProductState> = _uiState

    /** Actualiza el nombre del producto y revisa si la solicitud ya se puede enviar. */
    fun onProductNameChange(productName: String) {
        _uiState.update { state -> state.withValidation(productName = productName) }
    }

    /** Actualiza la categoría escrita para el producto solicitado. */
    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category) }
    }

    /** Actualiza la marca escrita para el producto solicitado. */
    fun onBrandChange(brand: String) {
        _uiState.update { it.copy(brand = brand) }
    }

    /** Aplica el formato de la fecha y vuelve a calcular sus validaciones. */
    fun onReleaseDateChange(releaseDate: String) {
        _uiState.update { state ->
            state.withValidation(releaseDate = formatLaunchDate(releaseDate))
        }
    }

    /** Construye un estado consistente sin duplicar las reglas de validación. */
    private fun RequestProductState.withValidation(
        productName: String = this.productName,
        releaseDate: String = this.releaseDate
    ): RequestProductState {
        // La solicitud necesita al menos el nombre, y la fecha solo se acepta si existe en el calendario.
        val releaseDateIsValid = releaseDate.isBlank() || isValidLaunchDate(releaseDate)

        return copy(
            productName = productName,
            releaseDate = releaseDate,
            isReleaseDateValid = releaseDateIsValid,
            showReleaseDateError = releaseDate.isNotBlank() &&
                releaseDate.filter(Char::isDigit).length == 8 &&
                !releaseDateIsValid,
            canSendRequest = productName.isNotBlank() && releaseDateIsValid
        )
    }
}
