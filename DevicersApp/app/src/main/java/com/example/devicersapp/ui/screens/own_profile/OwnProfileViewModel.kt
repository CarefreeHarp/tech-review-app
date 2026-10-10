package com.example.devicersapp.ui.screens.own_profile

import com.example.devicersapp.data.repository.SessionRepository
import com.example.devicersapp.data.repository.StorageRepository

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.OwnProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta el perfil en Firestore mediante el ID de la sesión actual. */
@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val ownProfileRepository: OwnProfileRepository,
    private val savedStateHandle: SavedStateHandle,
    private val session: SessionRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OwnProfileState())
    val uiState: StateFlow<OwnProfileState> = _uiState
    private var loadJob: Job? = null

    init {
        val deleteReviewId = savedStateHandle.get<Int>("deleteReviewId")?.takeIf { it > 0 }
        if (deleteReviewId != null) deleteReviewAndLoadProfile(deleteReviewId) else loadProfile()
    }

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

    /** Ejecuta la eliminación desde el destino para que salir del detalle no cancele la escritura en Firestore. */
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
                ownProfileRepository.deleteReview(reviewId)
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
                userId = session.currentProfile.value?.id?.toString(),
                displayName = session.currentProfile.value?.username.orEmpty(),
                email = session.currentProfile.value?.email.orEmpty(),
                profileImageUrl = session.currentProfile.value?.profileImageUrl,
                loading = true, deleting = deleting, errorMessageResId = null,
                profile = null, reviews = emptyList()
            )
        }
    }

    private suspend fun fetchProfile() {
        val (profile, contents) = ownProfileRepository.getProfileContent()
        val user = session.requireCurrentProfile()
        check(profile.id == user.id.toString())
        val userId = user.id
        _uiState.update {
            it.copy(userId = userId.toString(), displayName = user.username, email = user.email,
                profileImageUrl = user.profileImageUrl, profile = profile, reviews = contents,
                loading = false, deleting = false)
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
