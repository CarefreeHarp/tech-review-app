package com.example.devicersapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import com.example.devicersapp.data.local.LocalProfileProvider
import com.example.devicersapp.data.local.LocalReviewProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Obtiene y conserva el contenido visible del perfil solicitado. */
@HiltViewModel
class ProfileViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())
    val uiState: StateFlow<ProfileState> = _uiState

    /** Carga el perfil solicitado y las reseñas que le pertenecen. */
    fun loadProfile(profileId: String?) {
        val profile = profileId?.let(LocalProfileProvider::getPublicProfileById)
            ?: LocalProfileProvider.profile

        _uiState.update {
            ProfileState(
                profile = profile,
                reviews = LocalReviewProvider.reviewsForProfile(profile.id)
            )
        }
    }
}
