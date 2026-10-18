package com.example.devicersapp.data.datasource
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
/** Encapsula Firebase Cloud Messaging, compartido por ambas opciones de persistencia de datos. */
class MessagingRemoteDataSource @Inject constructor(private val messaging: FirebaseMessaging) {
    /** Token que identifica a este dispositivo ante FCM; las Cloud Functions lo usan como destinatario. */
    suspend fun getToken(): String {
        messaging.isAutoInitEnabled = true
        return messaging.token.await()
    }
    /** Desactiva el push de esta instalación al usar Retrofit, incluido un token de una ejecución anterior. */
    suspend fun disablePush() {
        messaging.isAutoInitEnabled = false
        messaging.deleteToken().await()
    }
}
