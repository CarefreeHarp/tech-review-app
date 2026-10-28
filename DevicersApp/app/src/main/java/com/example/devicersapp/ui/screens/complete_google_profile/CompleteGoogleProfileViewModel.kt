package com.example.devicersapp.ui.screens.complete_google_profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.UserRepository
import com.example.devicersapp.ui.models.UserDraft
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.Normalizer
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Crea el perfil de una cuenta Google nueva después de elegir un usuario legible. */
@HiltViewModel
class CompleteGoogleProfileViewModel @Inject constructor(
    private val auth: AuthRepository,
    private val users: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        CompleteGoogleProfileState(username = suggestedUsername())
    )
    val uiState = _uiState.asStateFlow()

    fun onUsernameChange(value: String) {
        val normalized = value.removePrefix("@").lowercase().filter {
            it in 'a'..'z' || it in '0'..'9' || it == '_'
        }.take(MAX_USERNAME_LENGTH)
        _uiState.update { it.copy(username = normalized, errorMessageResId = null) }
    }

    fun save(onCompleted: () -> Unit) {
        val username = _uiState.value.username.trim()
        if (!USERNAME_REGEX.matches(username)) {
            _uiState.update { it.copy(errorMessageResId = R.string.complete_profile_username_invalid) }
            return
        }
        _uiState.update { it.copy(saving = true, errorMessageResId = null) }
        viewModelScope.launch {
            try {
                if (!users.isUsernameAvailable(username)) {
                    _uiState.update { it.copy(saving = false, errorMessageResId = R.string.complete_profile_username_taken) }
                    return@launch
                }
                val account = checkNotNull(auth.currentUser) { "No hay una cuenta autenticada." }
                users.createIfMissing(
                    UserDraft(
                        firebaseUid = account.uid,
                        email = account.email.orEmpty(),
                        username = username,
                        profileImageUrl = account.photoUrl?.toString()
                    )
                )
                users.registerFcmToken()
                onCompleted()
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                _uiState.update { it.copy(saving = false, errorMessageResId = R.string.complete_profile_save_error) }
            }
        }
    }

    private fun suggestedUsername(): String {
        val account = auth.currentUser
        val source = account?.displayName?.takeIf { it.isNotBlank() }
            ?: account?.email?.substringBefore('@').orEmpty()
        return Normalizer.normalize(source, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "")
            .lowercase()
            .replace(Regex("[^a-z0-9]+"), "_")
            .trim('_')
            .take(MAX_USERNAME_LENGTH)
    }

    private companion object {
        const val MAX_USERNAME_LENGTH = 20
        val USERNAME_REGEX = Regex("^[a-z0-9_]{3,20}$")
    }
}
