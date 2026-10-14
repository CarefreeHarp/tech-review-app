package com.example.devicersapp.data.repository

import com.example.devicersapp.data.datasource.ProfileRemoteDataSource
import com.example.devicersapp.data.dto.toProfileContent
import com.example.devicersapp.data.dto.toReviewContent
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/** Agrupa el contenido y la relación de seguimiento de un perfil público. */
data class PublicProfileContent(
    val profile: ProfileContent,
    val reviews: List<ReviewContent>,
    val isFollowed: Boolean
)

/** Ensambla el perfil desde su contrato Firestore sin consultar repositorios Retrofit. */
class ProfileRepository @Inject constructor(private val source: ProfileRemoteDataSource) {
    suspend fun getProfile(userId: Int, currentUserId: Int): PublicProfileContent = coroutineScope {
        val user = async { source.getUser(userId) }
        val reviews = async { source.getReviews(userId) }
        val counts = async { source.getFollowCounts(userId) }
        val followed = async { source.isFollowing(currentUserId, userId) }
        val author = user.await()
        check(author.id == userId && author.isActive)
        val cards = reviews.await().filter { it.isActive && it.userId == userId }.map {
            // La cuadrícula no muestra reacciones; el detalle las consulta por separado.
            it.copy(user = author, likes = it.likes ?: 0, comments = it.comments.orEmpty()).toReviewContent()
        }
        val (followers, following) = counts.await()
        PublicProfileContent(author.toProfileContent(cards.size, followers, following), cards, followed.await())
    }
}
