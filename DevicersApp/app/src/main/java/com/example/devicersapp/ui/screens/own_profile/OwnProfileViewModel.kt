package com.example.devicersapp.ui.screens.own_profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva y carga el estado de la pantalla del perfil propio. */
@HiltViewModel
class OwnProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(OwnProfileState())
    val uiState: StateFlow<OwnProfileState> = _uiState

    init {
        loadProfile()
    }

    /** Carga el perfil propio y las reseñas publicadas por dicho perfil. */
    private fun loadProfile() {
        val profile = LocalProfileProvider.profile

        _uiState.update { currentState ->
            currentState.copy(
                profile = profile,
                reviews = LocalReviewProvider.reviewsForProfile(profile.id)
            )
        }
    }
}
