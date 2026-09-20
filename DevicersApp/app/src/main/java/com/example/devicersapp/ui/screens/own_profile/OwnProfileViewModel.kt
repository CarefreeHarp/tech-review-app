package com.example.devicersapp.ui.screens.own_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.StorageRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Conserva el estado de la pantalla del perfil propio y gestiona la actualización de su foto. */
@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val profile = LocalProfileProvider.profile
    private val authenticatedUser = authRepository.currentUser
    private val _uiState = MutableStateFlow(
        OwnProfileState(
            userId = authenticatedUser?.uid,
            displayName = authenticatedUser?.displayName.orEmpty(),
            email = authenticatedUser?.email.orEmpty(),
            profile = profile,
            profileImageUrl = authenticatedUser?.photoUrl?.toString(),
            reviews = LocalReviewProvider.reviewsForProfile(profile.id)
        )
    )
    val uiState: StateFlow<OwnProfileState> = _uiState

    /** Sube la imagen seleccionada a Firebase Storage y actualiza su URL al completarse. */
    fun uploadImageToFirebase(uri: Uri) {
        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            if (result.isSuccess) {
                _uiState.update {
                    it.copy(profileImageUrl = result.getOrNull())
                }
            }
        }
    }
}
