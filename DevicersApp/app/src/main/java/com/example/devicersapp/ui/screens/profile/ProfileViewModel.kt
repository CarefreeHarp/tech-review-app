package com.example.devicersapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.UserRepository
import com.example.devicersapp.data.repository.FollowRepository
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
    private val profiles: UserRepository,
    private val follows: FollowRepository
) : ViewModel() {
    private val state = MutableStateFlow(ProfileState())
    val uiState = state.asStateFlow()
    private var loadJob: Job? = null

    /** Carga el ID solicitado sin sustituir perfiles inválidos por datos de muestra. */
    fun loadProfile(profileId: String?) {
        if (state.value.followInProgress) return
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
    /** Cambia el botón de inmediato y confirma la relación; restaura el perfil si falla la escritura. */
    fun onFollowClick() {
        val original = state.value
        val profile = original.profile ?: return
        if (original.followInProgress || original.isOwnProfile) return
        val following = !original.isFollowed
        state.update { it.copy(isFollowed = following, followInProgress = true, followError = null) }
        viewModelScope.launch {
            try {
                val actor = profiles.getAuthenticatedUser()
                val status = follows.setFollowing(actor.id, profile.id.toInt(), following).getOrThrow()
                state.update { current ->
                    if (current.profile?.id != profile.id) current else current.copy(
                        isFollowed = status.isFollowing, followInProgress = false,
                        profile = current.profile.copy(stats = current.profile.stats.map { stat ->
                            if (stat.labelResId == R.string.profile_followers) stat.copy(number = status.followersCount.toString()) else stat
                        }))
                }
            } catch (cancelled: CancellationException) { throw cancelled
            } catch (error: Exception) {
                state.update { current -> if (current.profile?.id != profile.id) current else current.copy(
                    isFollowed = original.isFollowed, followInProgress = false, followError = R.string.profile_follow_error) }
            }
        }
    }

}
