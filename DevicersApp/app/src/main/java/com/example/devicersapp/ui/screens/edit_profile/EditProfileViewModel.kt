package com.example.devicersapp.ui.screens.edit_profile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.EditProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Carga el perfil de sesión y administra su borrador sin recibir IDs externos. */
@HiltViewModel
class EditProfileViewModel @Inject constructor(private val repository: EditProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(EditProfileState())
    val uiState = _uiState.asStateFlow()

    init { loadProfile() }

    /** Precarga el formulario desde Firestore y permite reintentar una carga fallida. */
    fun loadProfile() {
        _uiState.update { it.copy(loading = true, errorMessageResId = null) }
        viewModelScope.launch {
            try {
                val profile = repository.getProfile()
                _uiState.update {
                    EditProfileState(username = profile.username, biography = profile.biography.orEmpty(),
                        email = profile.email, profileImageUrl = profile.profileImageUrl, loading = false, loaded = true)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(loading = false, errorMessageResId = R.string.edit_profile_load_error) }
            }
        }
    }

    /** Actualiza el nombre del borrador sin escribir todavía en Firebase. */
    fun onUsernameChange(username: String) { _uiState.update { it.copy(username = username, errorMessageResId = null) } }

    /** Conserva la biografía escrita, incluida una cadena vacía para borrarla. */
    fun onBiographyChange(biography: String) { _uiState.update { it.copy(biography = biography, errorMessageResId = null) } }

    /** Previsualiza la selección local; cancelar la edición no sube la foto. */
    fun onPhotoSelected(photo: Uri) { _uiState.update { it.copy(selectedPhoto = photo, errorMessageResId = null) } }

    /** Guarda el borrador y comunica el éxito; ante un error conserva los campos y la foto. */
    fun save(onSaved: () -> Unit) {
        val draft = _uiState.value
        if (!draft.loaded) return
        if (draft.username.isBlank()) {
            _uiState.update { it.copy(errorMessageResId = R.string.edit_profile_username_required) }
            return
        }
        viewModelScope.launch {
            try {
                repository.saveProfile(draft.username, draft.biography, draft.selectedPhoto)
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(errorMessageResId = R.string.edit_profile_save_error) }
                return@launch
            }
            onSaved()
        }
    }
}
