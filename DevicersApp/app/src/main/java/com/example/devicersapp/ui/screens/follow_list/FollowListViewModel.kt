package com.example.devicersapp.ui.screens.follow_list

import androidx.lifecycle.ViewModel
import com.example.devicersapp.ui.models.FollowListSection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/** Conserva las pestañas de la lista sin consultar relaciones de seguimiento. */
@HiltViewModel
class FollowListViewModel @Inject constructor() : ViewModel() {
    private val state = MutableStateFlow(FollowListState(loading = false))
    val uiState = state.asStateFlow()

    /** Recibe el contexto de navegación sin solicitar usuarios a la base de datos. */
    fun loadProfile(profileId: Int, initialSection: FollowListSection) {
        state.update { FollowListState(profileId = profileId, section = initialSection, loading = false) }
    }

    /** Alterna únicamente la selección visual de las pestañas. */
    fun selectSection(section: FollowListSection) { state.update { it.copy(section = section) } }
}
