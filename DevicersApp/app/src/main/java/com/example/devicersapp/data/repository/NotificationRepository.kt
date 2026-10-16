package com.example.devicersapp.data.repository
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*

/** Operaciones de notificaciones independientes de la tecnología de persistencia. */
class NotificationRepository(private val source: NotificationRemoteDataSource) {
    suspend fun getNotifications(userId: Int): List<NotificationInfo> = source.getNotifications(userId)
}
