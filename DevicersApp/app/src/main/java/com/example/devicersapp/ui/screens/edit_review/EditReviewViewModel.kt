package com.example.devicersapp.ui.screens.edit_review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.core.config.CURRENT_USER_ID
import com.example.devicersapp.data.dto.toProductContent
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.ui.models.ReviewChanges
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Precarga una reseña propia y envía sus cambios mediante el repositorio de reseñas. */
@HiltViewModel
class EditReviewViewModel @Inject constructor(
    private val reviews: ReviewRepository,
    private val products: ProductRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(EditReviewState())
    val uiState: StateFlow<EditReviewState> = _uiState
    private var loadJob: Job? = null

    /** Comprueba el autor antes de exponer el formulario, también al abrir una ruta directa. */
    fun loadReview(reviewId: Int) {
        val current = _uiState.value
        if (current.saving || current.saved || (current.reviewId == reviewId && current.canEdit)) return
        loadJob?.cancel()
        _uiState.update { EditReviewState(loading = true) }
        loadJob = viewModelScope.launch {
            try {
                val review = reviews.getReviewById(reviewId).getOrThrow()
                if (review.userId != CURRENT_USER_ID) {
                    _uiState.update { EditReviewState(errorResId = R.string.edit_review_owner_error) }
                    return@launch
                }
                val product = products.getProductById(review.articleId).getOrThrow().toProductContent()
                // La creación guarda ventajas y desventajas como secciones dentro del cuerpo.
                // Solo se separan esos delimitadores; el resto del texto se conserva completo.
                var experience = review.body
                var disadvantage = ""
                val disadvantageIndex = experience.lastIndexOf("\n\nDesventajas: ")
                if (disadvantageIndex >= 0) {
                    disadvantage = experience.substring(disadvantageIndex + "\n\nDesventajas: ".length)
                    experience = experience.substring(0, disadvantageIndex)
                }
                var advantage = ""
                val advantageIndex = experience.lastIndexOf("\n\nVentajas: ")
                if (advantageIndex >= 0) {
                    advantage = experience.substring(advantageIndex + "\n\nVentajas: ".length)
                    experience = experience.substring(0, advantageIndex)
                }
                _uiState.update {
                    EditReviewState(
                        reviewId = review.id,
                        product = product,
                        rating = review.rating,
                        title = review.title.orEmpty(),
                        experience = experience,
                        advantage = advantage,
                        disadvantage = disadvantage,
                        canEdit = true
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { EditReviewState(errorResId = R.string.edit_review_load_error) }
            }
        }
    }

    /** Conserva el borrador ante fallos y evita guardar dos veces o alterar autor y producto. */
    fun save() {
        val draft = _uiState.value
        val reviewId = draft.reviewId ?: return
        if (!draft.canEdit || draft.saving || draft.saved || draft.loading) return
        if (draft.rating !in 1..5 || draft.experience.isBlank()) {
            _uiState.update { it.copy(errorResId = R.string.edit_review_validation_error) }
            return
        }
        _uiState.update { it.copy(saving = true, errorResId = null) }
        viewModelScope.launch {
            try {
                val body = buildString {
                    append(draft.experience)
                    if (draft.advantage.isNotBlank()) append("\n\nVentajas: ${draft.advantage}")
                    if (draft.disadvantage.isNotBlank()) append("\n\nDesventajas: ${draft.disadvantage}")
                }
                reviews.updateReview(reviewId, ReviewChanges(
                    rating = draft.rating, title = draft.title, body = body
                )).getOrThrow()
                _uiState.update { it.copy(saving = false, saved = true) }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(saving = false, errorResId = R.string.edit_review_save_error) }
            }
        }
    }

    /** Actualiza las estrellas mientras el formulario está disponible. */
    fun onRatingChange(rating: Int) = change { it.copy(rating = rating) }
    /** Conserva el título editado. */
    fun onTitleChange(title: String) = change { it.copy(title = title) }
    /** Conserva el cuerpo de la experiencia sin truncar reseñas existentes. */
    fun onExperienceChange(experience: String) = change { it.copy(experience = experience) }
    /** Conserva la sección de ventajas. */
    fun onAdvantageChange(advantage: String) = change { it.copy(advantage = advantage) }
    /** Conserva la sección de desventajas. */
    fun onDisadvantageChange(disadvantage: String) = change { it.copy(disadvantage = disadvantage) }

    private fun change(transform: (EditReviewState) -> EditReviewState) {
        _uiState.update { if (it.canEdit && !it.saving && !it.saved) transform(it) else it }
    }
}
