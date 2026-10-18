package com.example.devicersapp.ui.models

/**
 * Notificación vigente acompañada de las relaciones que necesita la pantalla de actividad.
 *
 * @param notification Notificación persistida del destinatario.
 * @param actor Perfil activo que generó la notificación.
 * @param productName Nombre del artículo de la reseña relacionada, o `null` si no apunta a una reseña.
 */
data class NotificationActivityInfo(
    val notification: NotificationInfo,
    val actor: UserInfo,
    val productName: String?
)
