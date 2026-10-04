package com.example.devicersapp.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.UsersRepository
import com.example.devicersapp.ui.mappers.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@HiltViewModel
class ProfileViewModel
@Inject
constructor(private val users: UsersRepository, private val reviews: ReviewRepository) :
    ViewModel() {
    private val state = MutableStateFlow(ProfileState())
    val uiState = state.asStateFlow()
    private var loadJob: Job? = null

    fun loadProfile(profileId: String?) {
        loadJob?.cancel()
        val id = profileId?.toIntOrNull()
        if (id == null || id <= 0) {
            state.value =
                ProfileState(error = "Perfil no válido. Selecciona un usuario del backend.")
            return
        }
        state.value = ProfileState(loading = true)
        loadJob =
            viewModelScope.launch {
                try {
                    val user = users.getUserById(id)
                    val items = reviews.getReviewsByUser(id).filter { it.userId == id }
                    val articles = user.reviews.orEmpty().associateBy { it.id }
                    state.value =
                        ProfileState(
                            profile = user.toProfileContent(items.size),
                            reviews =
                                items.map {
                                    it.copy(article = articles[it.id]?.article).toReviewContent()
                                },
                        )
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    state.value =
                        ProfileState(
                            error =
                                "No se pudo cargar el perfil. Comprueba la conexión e inténtalo de nuevo."
                        )
                }
            }
    }
}
