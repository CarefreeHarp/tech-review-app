package com.example.devicersapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.UserRepository
import com.example.devicersapp.ui.models.toProfileContent
import com.example.devicersapp.ui.models.toReviewContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta la fuente seleccionada y conserva el estado del perfil público seleccionado. */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profiles: UserRepository
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
                val currentId = profiles.getAuthenticatedUser().id
                if (id == currentId) {
                    state.update { ProfileState(isOwnProfile = true) }
                    return@launch
                }
                val content = profiles.getProfile(id)
                state.update {
                    ProfileState(profile = content.user.toProfileContent(content.reviews.size, content.followerCount, content.followingCount),
                        reviews = content.reviews.map { it.toReviewContent() }, isFollowed = content.isFollowed,
                        isOwnProfile = false)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                state.update { ProfileState(error = R.string.public_profile_load_error) }
            }
        }
    }
}
