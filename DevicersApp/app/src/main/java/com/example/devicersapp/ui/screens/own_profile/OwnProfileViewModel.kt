package com.example.devicersapp.ui.screens.own_profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import com.example.devicersapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva y carga el estado de la pantalla del perfil propio y de la sesión autenticada. */
@HiltViewModel
class OwnProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(OwnProfileState())
    val uiState: StateFlow<OwnProfileState> = _uiState

    init {
        loadProfile()
    }

    /** Carga el perfil propio, la cuenta autenticada y sus reseñas publicadas. */
    fun loadProfile() {
        val profile = LocalProfileProvider.profile
        val authenticatedUser = authRepository.currentUser

        _uiState.update { currentState ->
            currentState.copy(
                userId = authenticatedUser?.uid,
                displayName = authenticatedUser?.displayName.orEmpty(),
                email = authenticatedUser?.email.orEmpty(),
                profile = profile,
                reviews = LocalReviewProvider.reviewsForProfile(profile.id)
            )
        }
    }
}
