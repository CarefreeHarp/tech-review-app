package com.example.devicersapp.ui.screens.remote_reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.dto.*
import com.example.devicersapp.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@HiltViewModel
class RemoteReviewsViewModel
@Inject
constructor(private val reviews: ReviewRepository, private val products: ProductRepository) :
    ViewModel() {
    private val state = MutableStateFlow(RemoteReviewsState())
    val uiState = state.asStateFlow()
    private var detailId: Int? = null
    private var loadJob: Job? = null

    fun load(reviewId: Int? = null) {
        if (state.value.saving) return
        detailId = reviewId
        loadJob?.cancel()
        state.update { it.copy(loading = true, error = null) }
        loadJob =
            viewModelScope.launch {
                try {
                    val catalog = products.getProducts().getOrThrow()
                    val records =
                        if (reviewId == null)
                            reviews.getReviewsByUser(CURRENT_USER_ID).getOrThrow().filter {
                                it.userId == CURRENT_USER_ID
                            }
                        else listOf(reviews.getReviewById(reviewId).getOrThrow())
                    state.update {
                        it.copy(
                            loading = false,
                            products = catalog,
                            reviews =
                                records.map { review ->
                                    review.copy(
                                        article = catalog.find { it.id == review.articleId },
                                        user =
                                            catalog
                                                .find { it.id == review.articleId }
                                                ?.reviews
                                                ?.find { it.id == review.id }
                                                ?.user,
                                    )
                                },
                        )
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    state.update {
                        it.copy(
                            loading = false,
                            error =
                                "No se pudieron cargar las reseñas. Comprueba la conexión y reintenta.",
                        )
                    }
                }
            }
    }

    fun retry() = load(detailId)

    fun edit(review: ReviewDto) {
        if (state.value.saving || review.userId != CURRENT_USER_ID) return
        state.update {
            it.copy(
                editing = true,
                editingId = review.id,
                articleId = review.articleId,
                rating = review.rating,
                title = review.title.orEmpty(),
                body = review.body,
                error = null,
            )
        }
    }

    fun cancel() {
        if (!state.value.saving)
            state.update { it.copy(editing = false, deleteId = null, error = null) }
    }

    fun rating(value: Int) {
        if (!state.value.saving) state.update { it.copy(rating = value) }
    }

    fun title(value: String) {
        if (!state.value.saving) state.update { it.copy(title = value) }
    }

    fun body(value: String) {
        if (!state.value.saving) state.update { it.copy(body = value) }
    }

    fun requestDelete(review: ReviewDto) {
        if (!state.value.saving && review.userId == CURRENT_USER_ID)
            state.update { it.copy(deleteId = review.id, error = null) }
    }

    fun save() {
        val draft = state.value
        if (draft.saving) return
        val article = draft.articleId
        if (
            article == null ||
                draft.products.none { it.id == article } ||
                draft.rating !in 1..5 ||
                draft.body.isBlank()
        ) {
            state.update {
                it.copy(
                    error =
                        "Selecciona un producto, una calificación de 1 a 5 y escribe tu experiencia."
                )
            }
            return
        }
        mutate {
            val id = draft.editingId ?: return@mutate
            require(reviews.getReviewById(id).getOrThrow().userId == CURRENT_USER_ID)
            val record =
                reviews.updateReview(
                    id,
                    UpdateReviewRequestDto(
                        rating = draft.rating,
                        title = draft.title.trim(),
                        body = draft.body.trim(),
                    ),
                ).getOrThrow()
            state.update { current ->
                current.copy(
                    editing = false,
                    reviews =
                        listOf(
                            record.copy(
                                article = current.products.find { it.id == record.articleId },
                                user = current.reviews.find { it.id == record.id }?.user,
                            )
                        ) + current.reviews.filterNot { it.id == record.id },
                )
            }
        }
    }

    fun delete() {
        val id = state.value.deleteId ?: return
        mutate {
            require(reviews.getReviewById(id).getOrThrow().userId == CURRENT_USER_ID)
            reviews.deleteReview(id).getOrThrow()
            state.update {
                it.copy(
                    deleteId = null,
                    reviews = it.reviews.filterNot { review -> review.id == id },
                )
            }
        }
    }

    private fun mutate(action: suspend () -> Unit) {
        if (state.value.saving) return
        loadJob?.cancel()
        state.update { it.copy(saving = true, loading = false, error = null) }
        viewModelScope.launch {
            try {
                action()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update {
                    it.copy(
                        error =
                            "No se pudo guardar el cambio. Tus datos se conservan; vuelve a intentarlo."
                    )
                }
            } finally {
                state.update { it.copy(saving = false) }
            }
        }
    }
}
