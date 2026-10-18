package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
/** Operaciones de notificaciones independientes de la tecnología de persistencia. */
class NotificationRepository(
    private val source: NotificationRemoteDataSource,
    private val users: UserRemoteDataSource,
    private val reviews: ReviewRemoteDataSource,
    private val products: ProductRepository
) {
    suspend fun getNotifications(userId: Int): List<NotificationInfo> = source.getNotifications(userId)

    /**
     * Reúne las notificaciones del destinatario con su actor y el artículo de la reseña relacionada.
     * Omite las generadas por perfiles inactivos o que apuntan a reseñas o artículos retirados.
     */
    suspend fun getActivity(userId: Int): Result<List<NotificationActivityInfo>> = repositoryResult {
        val notifications = source.getNotifications(userId)
        if (notifications.isEmpty()) return@repositoryResult emptyList()
        coroutineScope {
            val actors = async { users.getByIds(notifications.map { it.actorUserId }.toSet()) }
            val activeReviews = async { reviews.getReviews().filter { it.isActive }.associateBy { it.id } }
            val catalog = async { products.getProducts().getOrThrow().associateBy { it.id } }
            val profiles = actors.await()
            val records = activeReviews.await()
            val articles = catalog.await()
            notifications.mapNotNull { notification ->
                val actor = profiles[notification.actorUserId]?.takeIf { it.isActive } ?: return@mapNotNull null
                val productName = notification.reviewId?.let { reviewId ->
                    articles[records[reviewId]?.articleId]?.name ?: return@mapNotNull null
                }
                NotificationActivityInfo(notification, actor, productName)
            }
        }
    }
}
