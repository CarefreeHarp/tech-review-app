package com.example.devicersapp.ui.screens.profile_saved_reviews

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.dto.toProfileContent
import com.example.devicersapp.data.dto.toReviewContent
import com.example.devicersapp.data.repository.AuthRepository
import com.example.devicersapp.data.repository.CommentLikeRepository
import com.example.devicersapp.data.repository.CommentRepository
import com.example.devicersapp.data.repository.ProductRepository
import com.example.devicersapp.data.repository.ReviewBookmarkRepository
import com.example.devicersapp.data.repository.ReviewLikeRepository
import com.example.devicersapp.data.repository.ReviewRepository
import com.example.devicersapp.data.repository.UsersRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ProfileSavedReviewsViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val products: ProductRepository,
    private val users: UsersRepository,
    private val comments: CommentRepository,
    private val reviewLikes: ReviewLikeRepository,
    private val commentLikes: CommentLikeRepository,
    private val bookmarks: ReviewBookmarkRepository,
    private val reviewRepository: ReviewRepository,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileSavedReviewsState())
    val uiState: StateFlow<ProfileSavedReviewsState> = _uiState

    private var loadJob: Job? = null

    init {
        loadSavedReviews()
    }

    fun loadSavedReviews() {
        loadJob?.cancel()

        val authenticatedUser = authRepository.currentUser

        _uiState.update {
            it.copy(
                email = authenticatedUser?.email.orEmpty(),
                profileImageUrl = authRepository.currentProfile?.profileImageUrl,
                loading = true,
                errorMessageResId = null,
                profile = null,
                savedReviews = emptyList()
            )
        }

        loadJob = viewModelScope.launch {
            try {
                val user = checkNotNull(authRepository.currentProfile)
                val userId = user.id

                val (profile, saved) = coroutineScope {

                    val profileDeferred = async {
                        val own = reviewRepository
                            .getReviewsByUser(userId)
                            .getOrThrow()
                            .count { it.isActive && it.userId == userId }

                        // Seguidores consultados directamente desde Firestore.
                        val relations = firestore
                            .collection("follows")
                            .get()
                            .await()
                            .documents

                        val followers = relations.count {
                            it.getLong("followed_id")?.toInt() == userId
                        }

                        val following = relations.count {
                            it.getLong("follower_id")?.toInt() == userId
                        }

                        user.toProfileContent(
                            own,
                            followers,
                            following
                        )
                    }

                    val savedDeferred = async {
                        bookmarks.getSavedReviews(
                            userId,
                            reviewRepository,
                            products,
                            users,
                            comments,
                            reviewLikes,
                            commentLikes
                        ).getOrThrow()
                    }

                    profileDeferred.await() to savedDeferred.await()
                }

                check(authRepository.currentProfile?.id == userId)

                _uiState.update {
                    it.copy(
                        profile = profile.copy(imageUrl = null),
                        savedReviews = saved.map { review ->
                            review.toReviewContent()
                        },
                        loading = false,
                        errorMessageResId = null
                    )
                }

            } catch (exception: CancellationException) {
                throw exception

            } catch (exception: Exception) {
                android.util.Log.e(
                    "SAVED_REVIEWS",
                    "Error cargando las reseñas guardadas",
                    exception
                )

                _uiState.update {
                    it.copy(
                        loading = false,
                        errorMessageResId = R.string.profile_saved_load_error
                    )
                }
            }
        }
    }

    fun onReviewsSelected() {
        _uiState.update {
            it.copy(isReviewsSelected = true)
        }
    }

    fun onSavedSelected() {
        _uiState.update {
            it.copy(isReviewsSelected = false)
        }
    }
}