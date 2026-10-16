package com.example.devicersapp.data.datasource.implementations.firestore
import com.example.devicersapp.data.datasource.*
import com.example.devicersapp.ui.models.*
import com.example.devicersapp.data.dto.ResponseDTO.firestore.*
import com.example.devicersapp.data.dto.ResponseDTO.shared.*
import com.example.devicersapp.data.dto.RequestDTO.firestore.*
import com.google.firebase.firestore.*
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

/** Implementa las operaciones de notificaciones con las rutas y campos de Firestore v9. */
class NotificationRemoteDataSourceFirestoreImplementation @Inject constructor(
    private val firestore: FirebaseFirestore
) : NotificationRemoteDataSource {
    override suspend fun getNotifications(userId: Int): List<NotificationInfo> {
        require(userId > 0)
        return firestore.collection("notifications").whereEqualTo("user_id", userId).get(Source.SERVER).await().documents
            .map { it.toNotificationFirestoreDto().toNotificationInfo() }
    }
}
