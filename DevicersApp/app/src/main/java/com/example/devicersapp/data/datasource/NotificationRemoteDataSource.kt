package com.example.devicersapp.data.datasource
import com.example.devicersapp.ui.models.*
/** Operaciones de notificaciones; devuelve modelos comunes y propaga los errores de la fuente seleccionada. */
interface NotificationRemoteDataSource {
    suspend fun getNotifications(userId: Int): List<NotificationInfo>
}
