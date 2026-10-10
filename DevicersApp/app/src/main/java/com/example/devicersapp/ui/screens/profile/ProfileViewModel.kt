package com.example.devicersapp.ui.screens.profile

import com.example.devicersapp.data.repository.CommentLikeRepository
import com.example.devicersapp.data.repository.CommentRepository
import com.example.devicersapp.data.repository.FollowRepository
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewLikeRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.UsersRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.data.repository.SessionRepository
import com.example.devicersapp.data.dto.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

@HiltViewModel
class ProfileViewModel
@Inject
constructor(
    private val users: UsersRepository,
    private val reviews: ReviewRepository,
    private val products: ProductRepository,
    private val comments: CommentRepository,
    private val reviewLikes: ReviewLikeRepository,
    private val commentLikes: CommentLikeRepository,
    private val follows: FollowRepository,
    private val session: SessionRepository
) :
    ViewModel() {
    private val state = MutableStateFlow(ProfileState())
    val uiState = state.asStateFlow()
    private var loadJob: Job? = null

    fun loadProfile(profileId: String?) {
        loadJob?.cancel()
        val id = profileId?.toIntOrNull()
        if (id == null || id <= 0) {
            state.update { ProfileState(error = "Perfil no válido. Selecciona un usuario del backend.") }
            return
        }
        state.update { ProfileState(loading = true) }
        loadJob =
            viewModelScope.launch {
                try {
                    val currentUserId = session.requireCurrentProfile().id
                    val user = users.getUserById(id)
                    val items = reviews.getReviewsByUser(id).getOrThrow().filter { it.userId == id }
                    val contents = products.getReviewContents(items.filter { it.isActive }, users, comments, reviewLikes, commentLikes)
                    val isFollowed = follows.getFollows().any { it.followerId == currentUserId && it.followedId == id }
                    state.update {
                        ProfileState(
                            profile = user.toProfileContent(contents.size),
                            isFollowed = isFollowed,
                            reviews =
                                contents.map { it.toReviewContent() },
                        )
                    }
                } catch (e: CancellationException) {
                    throw e
                } catch (e: Exception) {
                    state.update {
                        ProfileState(
                            error =
                                "No se pudo cargar el perfil. Comprueba la conexión e inténtalo de nuevo."
                        )
                    }
                }
            }
    }
}
