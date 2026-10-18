package com.example.devicersapp.data.datasource.services

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.devicersapp.data.config.BackendConfig
import com.example.devicersapp.MainActivity
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.UserRepository
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

/** Recibe tokens renovados y muestra también los avisos que llegan con la aplicación abierta. */
@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {
    @Inject lateinit var users: UserRepository

    /** FCM ejecuta este callback en su hilo de trabajo; el guardado tiene un límite de cinco segundos. */
    override fun onNewToken(token: String) {
        super.onNewToken(token)
        if (BackendConfig.USE_FIRESTORE) runBlocking { users.registerFcmToken(token) }
    }

    /** En segundo plano Android muestra el mensaje; este callback cubre la aplicación en primer plano. */
    override fun onMessageReceived(message: RemoteMessage) {
        if (!BackendConfig.USE_FIRESTORE) return
        val notification = message.notification ?: return
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
        val manager = getSystemService(NotificationManager::class.java)
        val channel = "devicers_activity"
        manager.createNotificationChannel(NotificationChannel(channel, getString(R.string.push_activity_channel), NotificationManager.IMPORTANCE_DEFAULT))
        val intent = PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        manager.notify(message.messageId?.hashCode() ?: System.currentTimeMillis().toInt(),
            NotificationCompat.Builder(this, channel).setSmallIcon(R.drawable.like_icon)
                .setContentTitle(notification.title).setContentText(notification.body)
                .setContentIntent(intent).setAutoCancel(true).build())
    }
}
