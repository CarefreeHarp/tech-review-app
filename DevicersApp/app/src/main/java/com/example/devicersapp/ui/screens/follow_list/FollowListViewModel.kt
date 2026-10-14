package com.example.devicersapp.ui.screens.follow_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.FollowListRepository
import com.example.devicersapp.ui.models.FollowListSection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

/** Consulta el ID de navegación, independientemente del usuario que inició sesión. */
@HiltViewModel
class FollowListViewModel @Inject constructor(private val repository: FollowListRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(FollowListState())
    val uiState = _uiState.asStateFlow()
    private var loadJob: Job? = null

    /** Recibe el perfil y la sección pulsada en su cabecera. */
    fun loadProfile(profileId: Int, initialSection: FollowListSection) {
        loadUsers(profileId, initialSection)
    }

    /** Cambia el sentido de la consulta conservando el mismo perfil. */
    fun selectSection(section: FollowListSection) {
        val current = _uiState.value
        if (current.section != section) current.profileId?.let { loadUsers(it, section) }
    }

    /** Repite una consulta fallida sin perder el perfil o la pestaña seleccionada. */
    fun retry() {
        val current = _uiState.value
        current.profileId?.let { loadUsers(it, current.section) }
    }

    private fun loadUsers(profileId: Int, section: FollowListSection) {
        loadJob?.cancel()
        if (profileId <= 0) {
            _uiState.update { FollowListState(profileId = profileId, section = section,
                loading = false, errorMessageResId = R.string.profile_invalid_id) }
            return
        }
        _uiState.update { FollowListState(profileId = profileId, section = section) }
        loadJob = viewModelScope.launch {
            try {
                val users = repository.getUsers(profileId, section).distinctBy { it.id }
                    .sortedBy { it.username.lowercase(Locale.ROOT) }
                    .map { user ->
                        // Limita solo la copia visible y evita cortar los pares de caracteres de los emojis.
                        val biography = user.biography
                        val end = biography.offsetByCodePoints(0, minOf(50, biography.codePointCount(0, biography.length)))
                        user.copy(biography = biography.substring(0, end),
                            isBiographyTruncated = end < biography.length)
                    }
                _uiState.update { it.copy(users = users, loading = false) }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(loading = false, errorMessageResId = R.string.follow_list_load_error) }
            }
        }
    }
}
