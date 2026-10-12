package com.example.devicersapp.data.repository

import com.example.devicersapp.R
import com.example.devicersapp.data.datasource.DiscoveryRemoteDataSource
import com.example.devicersapp.data.dto.toSearchContent
import com.example.devicersapp.ui.models.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

data class DiscoveryCatalog(val products: List<ProductInfo>, val categories: List<CategoryInfo>)
data class DiscoveryProfiles(val profiles: List<ProfileSearchResultContent>, val followedIds: Set<String>)
data class DiscoveryActivity(
    val groups: List<ActivityGroupContent>,
    val actors: Map<Int, UserInfo>,
    val productNames: Map<Int, String>,
    val followedActivityIds: Set<String>
)

/** Ensambla únicamente los datos de búsqueda y actividad sin alterar repositorios compartidos. */
class DiscoveryRepository @Inject constructor(private val source: DiscoveryRemoteDataSource) {
    suspend fun getCatalog(): DiscoveryCatalog = coroutineScope {
        val products = async { source.getProducts() }
        val categories = async { source.getCategories() }
        val reviews = source.getActiveReviews().filter { it.isActive }.groupBy { it.articleId }
        DiscoveryCatalog(products.await().filter { it.isActive }.map {
            it.copy(reviews = reviews[it.id].orEmpty())
        }, categories.await())
    }

    suspend fun getProfiles(userId: Int): DiscoveryProfiles = coroutineScope {
        val users = async { source.getUsers() }
        val followed = async { source.getFollowedIds(userId) }
        val reviews = source.getActiveReviews().filter { it.isActive }.groupBy { it.userId }
        DiscoveryProfiles(users.await().filter { it.isActive && it.id != userId }.map {
            it.copy(reviews = reviews[it.id].orEmpty()).toSearchContent()
        }, followed.await().map(Int::toString).toSet())
    }

    suspend fun getActivity(userId: Int, now: Long = System.currentTimeMillis()): DiscoveryActivity = coroutineScope {
        val notifications = async { source.getNotifications(userId) }
        val users = async { source.getUsers().filter { it.isActive }.associateBy { it.id } }
        val products = async { source.getProducts().filter { it.isActive }.associateBy { it.id } }
        val reviews = async { source.getActiveReviews().filter { it.isActive }.associateBy { it.id } }
        val follows = async { source.getFollowedIds(userId) }
        val actors = users.await()
        val activeReviews = reviews.await()
        val activeProducts = products.await()
        val events = notifications.await().mapNotNull { event ->
            if (event.recipientId != userId || event.actorId !in actors) return@mapNotNull null
            val type = when (event.type) {
                "follow" -> ActivityType.FOLLOW
                "review_like", "comment_like" -> ActivityType.LIKE
                "comment" -> ActivityType.COMMENT
                else -> return@mapNotNull null
            }
            if (type != ActivityType.FOLLOW &&
                activeReviews[event.reviewId]?.articleId !in activeProducts) return@mapNotNull null
            val action = when (event.type) {
                "follow" -> R.string.activity_action_followed
                "review_like" -> R.string.activity_action_liked_review
                "comment_like" -> R.string.discovery_activity_liked_comment
                else -> R.string.discovery_activity_commented
            }
            ActivityContent(event.id, type, event.actorId.toString(), action,
                targetReviewId = event.reviewId, targetProfileId = event.actorId.toString(),
                time = event.time, showFollowAction = type == ActivityType.FOLLOW)
        }.sortedByDescending { it.time }
        val zone = ZoneId.systemDefault()
        val today = Instant.ofEpochMilli(now).atZone(zone).toLocalDate()
        val groups = events.groupBy {
            val date = Instant.ofEpochMilli(it.time).atZone(zone).toLocalDate()
            when { date >= today -> "today"; date == today.minusDays(1) -> "yesterday"; else -> "earlier" }
        }
        val titles = linkedMapOf("today" to R.string.activity_group_today,
            "yesterday" to R.string.activity_group_yesterday, "earlier" to R.string.activity_group_earlier)
        val followed = follows.await()
        DiscoveryActivity(titles.mapNotNull { (id, title) -> groups[id]?.let { ActivityGroupContent(id, title, it) } },
            actors, activeReviews.mapValues { (_, review) -> activeProducts[review.articleId]?.name.orEmpty() },
            events.filter { it.actorProfileId.toInt() in followed }.map { it.id }.toSet())
    }
}
