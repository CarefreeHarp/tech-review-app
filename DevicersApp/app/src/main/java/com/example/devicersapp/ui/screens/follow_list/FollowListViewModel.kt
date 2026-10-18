package com.example.devicersapp.ui.screens.follow_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.FollowRepository
import com.example.devicersapp.ui.models.FollowListSection
import com.example.devicersapp.ui.models.FollowListUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta los perfiles relacionados y conserva el estado de la pestaña seleccionada. */
@HiltViewModel
class FollowListViewModel @Inject constructor(private val follows: FollowRepository) : ViewModel() {
    private val state = MutableStateFlow(FollowListState())
    val uiState = state.asStateFlow()
    private var loadJob: Job? = null

    /** Recibe el perfil y la pestaña inicial desde navegación y carga sus relaciones actuales. */
    fun loadProfile(profileId: Int, initialSection: FollowListSection) {
        if (state.value.profileId != profileId) state.update { FollowListState(profileId = profileId, section = initialSection) }
        reload()
    }

    /** Reconsulta la sección activa al regresar desde un perfil o reintentar un fallo. */
    fun reload() {
        val profileId = state.value.profileId ?: return
        val section = state.value.section
        loadJob?.cancel()
        state.update { it.copy(loading = true, errorMessageResId = null, users = emptyList()) }
        loadJob = viewModelScope.launch {
            try {
                require(profileId > 0)
                val records = if (section == FollowListSection.FOLLOWERS) follows.getFollowerUsers(profileId)
                    else follows.getFollowingUsers(profileId)
                state.update { it.copy(loading = false, users = records.map { user ->
                    FollowListUser(user.id, user.username, user.biography.orEmpty(), user.profileImageUrl)
                }) }
            } catch (cancelled: CancellationException) { throw cancelled
            } catch (error: Exception) {
                state.update { it.copy(loading = false, errorMessageResId = R.string.follow_list_load_error) }
            }
        }
    }

    /** Cambia la pestaña y solicita sus usuarios, cancelando cualquier consulta anterior. */
    fun selectSection(section: FollowListSection) {
        if (state.value.section == section) return
        state.update { it.copy(section = section) }
        reload()
    }
}
