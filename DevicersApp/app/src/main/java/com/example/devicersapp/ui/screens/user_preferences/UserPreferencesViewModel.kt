package com.example.devicersapp.ui.screens.user_preferences

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

/** Expone únicamente la presentación vacía; no consulta usuarios, sesión ni servicios remotos. */
@HiltViewModel
class UserPreferencesViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(UserPreferencesState())
    val uiState = _uiState.asStateFlow()
}
