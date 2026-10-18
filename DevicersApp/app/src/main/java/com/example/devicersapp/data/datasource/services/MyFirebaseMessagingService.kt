package com.example.devicersapp.data.datasource.services
import com.google.firebase.messaging.FirebaseMessagingService

/**
 * Punto de entrada de Firebase Cloud Messaging en la aplicación.
 *
 * Con la app en segundo plano, Android muestra directamente las notificaciones que envían las
 * Cloud Functions. Aquí se sobrescribirían métodos como onMessageReceived() para personalizar qué
 * hacer cuando llega un mensaje con la app abierta.
 */
class MyFirebaseMessagingService : FirebaseMessagingService()
