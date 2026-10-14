package com.example.devicersapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.ProfileRepository
import com.example.devicersapp.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta Firestore y conserva el estado del perfil público seleccionado. */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profiles: ProfileRepository,
    private val session: SessionRepository
) : ViewModel() {
    private val state = MutableStateFlow(ProfileState())
    val uiState = state.asStateFlow()
    private var loadJob: Job? = null

    /** Carga el ID solicitado sin sustituir perfiles inválidos por datos de muestra. */
    fun loadProfile(profileId: String?) {
        loadJob?.cancel()
        val id = profileId?.toIntOrNull()
        if (id == null || id <= 0) {
            state.update { ProfileState(error = R.string.profile_invalid_id) }
            return
        }
        state.update { ProfileState(loading = true) }
        loadJob = viewModelScope.launch {
            try {
                val content = profiles.getProfile(id, session.requireCurrentProfile().id)
                state.update {
                    ProfileState(profile = content.profile, reviews = content.reviews, isFollowed = content.isFollowed)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                state.update { ProfileState(error = R.string.public_profile_load_error) }
            }
        }
    }
}
