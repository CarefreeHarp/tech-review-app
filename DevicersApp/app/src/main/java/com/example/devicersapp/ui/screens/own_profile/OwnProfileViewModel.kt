package com.example.devicersapp.ui.screens.own_profile

import com.example.devicersapp.domain.usecase.ReviewContentUseCase
import com.example.devicersapp.domain.usecase.ProfileContentUseCase

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.core.config.CURRENT_USER_ID
import com.example.devicersapp.data.dto.toReviewContent
import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.StorageRepository
import com.example.devicersapp.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta el perfil propio en la API y conserva la identidad y la foto de Firebase. */
@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository,
    private val profileContent: ProfileContentUseCase,
    private val reviewContent: ReviewContentUseCase,
    private val reviewRepository: ReviewRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState = MutableStateFlow(OwnProfileState())
    val uiState: StateFlow<OwnProfileState> = _uiState
    private var loadJob: Job? = null

    init {
        val deleteReviewId = savedStateHandle.get<Int>("deleteReviewId")?.takeIf { it > 0 }
        if (deleteReviewId != null) deleteReviewAndLoadProfile(deleteReviewId) else loadProfile()
    }

    /** Refresca las reseñas creadas, la biografía y los conteos del usuario temporal del backend. */
    fun loadProfile() {
        if (_uiState.value.deleting) return
        savedStateHandle.get<Int>("pendingDeleteReviewId")?.let {
            deleteReviewAndLoadProfile(it, retry = true)
            return
        }
        loadJob?.cancel()
        startLoading()
        loadJob = viewModelScope.launch {
            try {
                fetchProfile()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(loading = false, errorMessageResId = R.string.own_profile_load_error) }
            }
        }
    }

    /** Ejecuta la eliminación desde el destino para que salir del detalle no cancele el DELETE. */
    fun deleteReviewAndLoadProfile(reviewId: Int, retry: Boolean = false) {
        if (!retry && savedStateHandle.get<Int>("pendingDeleteReviewId") == reviewId &&
            _uiState.value.errorMessageResId != null) return
        if (_uiState.value.deleting) return
        if (savedStateHandle.get<Int>("completedDeleteReviewId") == reviewId) {
            loadProfile()
            return
        }
        loadJob?.cancel()
        savedStateHandle["pendingDeleteReviewId"] = reviewId
        startLoading(deleting = true)
        loadJob = viewModelScope.launch {
            try {
                val review = reviewRepository.getReviewById(reviewId).getOrThrow()
                require(review.userId == CURRENT_USER_ID)
                reviewRepository.deleteReview(reviewId).getOrThrow()
                savedStateHandle["completedDeleteReviewId"] = reviewId
                savedStateHandle.remove<Int>("pendingDeleteReviewId")
                // Mantiene la pantalla de carga hasta que el perfil ya refleje la eliminación.
                fetchProfile()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                val error = if (savedStateHandle.get<Int>("pendingDeleteReviewId") != null)
                    R.string.review_delete_error else R.string.own_profile_load_error
                _uiState.update { it.copy(loading = false, deleting = false, errorMessageResId = error) }
            }
        }
    }

    private fun startLoading(deleting: Boolean = false) {
        val authenticatedUser = authRepository.currentUser
        _uiState.update {
            it.copy(
                userId = authenticatedUser?.uid,
                displayName = authenticatedUser?.displayName.orEmpty(),
                email = authenticatedUser?.email.orEmpty(),
                profileImageUrl = authenticatedUser?.photoUrl?.toString(),
                loading = true, deleting = deleting, errorMessageResId = null,
                profile = null, reviews = emptyList()
            )
        }
    }

    private suspend fun fetchProfile() {
        val records = reviewRepository.getReviewsByUser(CURRENT_USER_ID).getOrThrow()
            .filter { it.isActive && it.userId == CURRENT_USER_ID }
        val profile = profileContent.getProfileContent(CURRENT_USER_ID, records.size)
        val contents = reviewContent.getReviewContents(records).map { it.toReviewContent() }
        _uiState.update {
            // La foto del perfil propio se administra únicamente mediante Firebase.
            it.copy(profile = profile.copy(imageUrl = null), reviews = contents, loading = false, deleting = false)
        }
    }

    /** Sube la imagen seleccionada a Firebase Storage y actualiza su URL al completarse. */
    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess) {
                _uiState.update { it.copy(profileImageUrl = result.getOrNull()) }
            }
        }
    }
}
