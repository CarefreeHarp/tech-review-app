package com.example.devicersapp.ui.screens.request_product

import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.ProductRequestsRepository
import com.example.devicersapp.data.datasource.ProductSuggestion
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import java.util.UUID
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.ui.utils.search.formatLaunchDate
import com.example.devicersapp.ui.utils.search.isValidLaunchDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Gestiona la validación y las acciones de presentación de la solicitud de producto. */
@HiltViewModel
class RequestProductViewModel @Inject constructor(private val repository: ProductRequestsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestProductState())
    val uiState: StateFlow<RequestProductState> = _uiState

    private var requestId = UUID.randomUUID().toString()

    /** Confirma en Firestore antes de comunicar éxito o ejecutar la navegación. */
    fun submit(onSuccess: () -> Unit = {}) {
        val state = _uiState.value
        if (!state.canSendRequest || state.submitting || state.submitted) return
        _uiState.update { it.copy(submitting = true, canSendRequest = false, error = null) }
        viewModelScope.launch {
            try {
                repository.submit(requestId, ProductSuggestion(state.productName, state.category, state.brand, state.releaseDate))
                _uiState.update { it.copy(submitting = false, submitted = true, canSendRequest = false) }
            } catch (e: CancellationException) { throw e }
            catch (e: Exception) {
                _uiState.update { it.copy(submitting = false, error = "No se pudo enviar la solicitud. Intenta nuevamente.").withValidation() }
                return@launch
            }
            onSuccess()
        }
    }

    private fun beginEdit(): Boolean {
        if (_uiState.value.submitting) return false
        requestId = UUID.randomUUID().toString()
        _uiState.update { it.copy(submitted = false, error = null) }
        return true
    }

    /** Actualiza el nombre del producto y revisa si la solicitud ya se puede enviar. */
    fun onProductNameChange(productName: String) {
        if (!beginEdit()) return
        _uiState.update { state -> state.withValidation(productName = productName) }
    }

    /** Actualiza la categoría escrita para el producto solicitado. */
    fun onCategoryChange(category: String) {
        if (!beginEdit()) return
        _uiState.update { it.copy(category = category).withValidation() }
    }

    /** Actualiza la marca escrita para el producto solicitado. */
    fun onBrandChange(brand: String) {
        if (!beginEdit()) return
        _uiState.update { it.copy(brand = brand).withValidation() }
    }

    /** Aplica el formato de la fecha y vuelve a calcular sus validaciones. */
    fun onReleaseDateChange(releaseDate: String) {
        if (!beginEdit()) return
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
            canSendRequest = productName.isNotBlank() && releaseDateIsValid && !submitting && !submitted
        )
    }
}
