package com.example.devicersapp.ui.screens.profile_saved_reviews

import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.CommentLikeRepository
import com.example.devicersapp.data.repository.CommentRepository
import com.example.devicersapp.data.repository.FollowRepository
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewBookmarkRepository
import com.example.devicersapp.data.repository.ReviewLikeRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.UsersRepository

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.dto.toProfileContent
import com.example.devicersapp.data.dto.toReviewContent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Consulta los guardados del usuario de la sesión y conserva la identidad de Firebase. */
@HiltViewModel
class ProfileSavedReviewsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val products: ProductRepository,
    private val users: UsersRepository,
    private val comments: CommentRepository,
    private val reviewLikes: ReviewLikeRepository,
    private val commentLikes: CommentLikeRepository,
    private val follows: FollowRepository,
    private val bookmarks: ReviewBookmarkRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileSavedReviewsState())
    val uiState: StateFlow<ProfileSavedReviewsState> = _uiState
    private var loadJob: Job? = null

    init {
        loadSavedReviews()
    }

    /** Refresca el perfil y las reseñas realmente guardadas en el backend. */
    fun loadSavedReviews() {
        loadJob?.cancel()
        val authenticatedUser = authRepository.currentUser
        _uiState.update {
            it.copy(email = authenticatedUser?.email.orEmpty(),
                profileImageUrl = authRepository.currentProfile?.profileImageUrl,
                loading = true, errorMessageResId = null,
                profile = null, savedReviews = emptyList())
        }
        loadJob = viewModelScope.launch {
            try {
                val user = checkNotNull(authRepository.currentProfile)
                val userId = user.id
                val (profile, saved) = coroutineScope {
                    val profile = async {
                        val own = reviewRepository.getReviewsByUser(userId).getOrThrow()
                            .count { it.isActive && it.userId == userId }
                        val relations = follows.getFollows()
                        user.toProfileContent(own, relations.count { it.followedId == userId },
                            relations.count { it.followerId == userId })
                    }
                    val saved = async { bookmarks.getSavedReviews(userId, reviewRepository, products, users, comments, reviewLikes, commentLikes).getOrThrow() }
                    profile.await() to saved.await()
                }
                check(authRepository.currentProfile?.id == userId)
                _uiState.update {
                    it.copy(profile = profile.copy(imageUrl = null),
                        savedReviews = saved.map { review -> review.toReviewContent() }, loading = false)
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                _uiState.update { it.copy(loading = false, errorMessageResId = R.string.profile_saved_load_error) }
            }
        }
    }

    /** Publica la selección de la pestaña de reseñas antes de navegar a ella. */
    fun onReviewsSelected() {
        _uiState.update { it.copy(isReviewsSelected = true) }
    }

    /** Mantiene activa la pestaña de elementos guardados. */
    fun onSavedSelected() {
        _uiState.update { it.copy(isReviewsSelected = false) }
    }
}
