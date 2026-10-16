package com.example.devicersapp.ui.screens.own_profile

import com.example.devicersapp.data.repository.UserRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.ui.models.toProfileContent
import com.example.devicersapp.ui.models.toReviewContent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta el perfil asociado al usuario actual de Firebase Auth. */
@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val users: UserRepository,
    private val reviews: ReviewRepository,
    private val auth: com.example.devicersapp.data.repository.AuthRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OwnProfileState())
    val uiState: StateFlow<OwnProfileState> = _uiState
    private var loadJob: Job? = null
    private var hasResumed = false

    init {
        val deleteReviewId = savedStateHandle.get<Int>("deleteReviewId")?.takeIf { it > 0 }
        if (deleteReviewId != null) deleteReviewAndLoadProfile(deleteReviewId) else loadProfile()

    }

    /** Refresca al volver del editor; la primera carga ya se inicia en init. */
    fun onResume() {
        if (hasResumed) loadProfile()
        hasResumed = true
    }

    /** Cierra la sesión mantenida por Firebase Auth. */
    fun signOut() { auth.signOut() }

    /** Refresca las reseñas creadas, la biografía y los conteos del usuario de la sesión actual. */
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

    /** Ejecuta la eliminación desde el destino para que salir del detalle no cancele la escritura. */
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
                val userId = users.getAuthenticatedUser().id
                check(reviews.getReviewById(reviewId).getOrThrow().userId == userId)
                reviews.deleteReview(reviewId).getOrThrow()
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
        _uiState.update {
            it.copy(
                loading = true, deleting = deleting, errorMessageResId = null,
                profile = null, reviews = emptyList()
            )
        }
    }

    private suspend fun fetchProfile() {
        val user = users.getAuthenticatedUser()
        val data = users.getProfile(user.id)
        val profile = data.user.toProfileContent(data.reviews.size, data.followerCount, data.followingCount)
        val contents = data.reviews.map { it.toReviewContent() }
        val userId = user.id
        _uiState.update {
            it.copy(userId = userId.toString(), displayName = user.username, email = user.email,
                profileImageUrl = user.profileImageUrl, profile = profile, reviews = contents,
                loading = false, deleting = false)
        }
    }

}
