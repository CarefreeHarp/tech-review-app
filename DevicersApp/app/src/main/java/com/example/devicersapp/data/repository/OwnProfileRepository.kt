package com.example.devicersapp.data.repository

import com.example.devicersapp.R
import com.example.devicersapp.data.datasource.OwnProfileRemoteDataSource
import com.example.devicersapp.data.dto.toProfileContent
import com.example.devicersapp.ui.models.ProfileContent
import com.example.devicersapp.ui.models.ReviewContent
import com.example.devicersapp.ui.utils.images.localImageResIdFor
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

/** Reúne desde Firestore la identidad, las métricas y las tarjetas del perfil de sesión. */
class OwnProfileRepository @Inject constructor(
    private val session: SessionRepository,
    private val source: OwnProfileRemoteDataSource
) {
    /** Actualiza el perfil compartido y carga únicamente la información visible en OwnProfile. */
    suspend fun getProfileContent(): Pair<ProfileContent, List<ReviewContent>> = coroutineScope {
        val user = session.refreshCurrentProfile()
        val reviews = async { source.getReviewsByUser(user.id) }
        val counts = async { source.getFollowCounts(user.id) }
        val records = reviews.await().filter { it.isActive && it.userId == user.id }
        val (followers, following) = counts.await()
        check(session.currentProfile.value?.firebaseUid == user.firebaseUid &&
            session.currentProfile.value?.id == user.id) { "La sesión cambió durante la consulta." }
        val cards = records.map { review ->
            val article = checkNotNull(review.article)
            ReviewContent(
                id = review.id, authorId = user.id.toString(), rating = review.rating,
                productNameResId = R.string.rate_product_name,
                productImageResId = localImageResIdFor(article.imageUrl) ?: R.drawable.logo_icono_claro,
                productMetadataResId = R.string.rate_product_brand,
                textResId = R.string.rate_product_experience,
                // Las tarjetas de perfil no muestran reacciones ni comentarios; el detalle los carga aparte.
                likes = 0, productName = article.name, productImageUrl = article.imageUrl,
                title = review.title, body = review.body,
                authorName = user.username, authorImageUrl = user.profileImageUrl
            )
        }
        user.toProfileContent(cards.size, followers, following) to cards
    }

    /** Solicita la eliminación únicamente con la identidad vigente de la sesión. */
    suspend fun deleteReview(reviewId: Int) {
        source.deleteReview(reviewId, session.requireCurrentProfile().id)
    }
}
