package com.example.devicersapp.ui.models

import androidx.annotation.StringRes

/**
 * Representa el contenido visible de un evento de actividad dentro de la aplicación.
 *
 * @param id Identificador único usado para conservar el estado de seguimiento de la tarjeta.
 * @param type Tipo de evento, que determina la insignia mostrada sobre el avatar.
 * @param actorProfileId Perfil existente que realizó el evento.
 * @param actionResId Acción que el autor realizó sobre el contenido del usuario.
 * @param targetReviewId Reseña existente en la que ocurrió un like o comentario.
 * @param targetProfileId Perfil existente al que dirige una notificación de nuevo seguidor.
 * @param time Instante de llegada del evento en milisegundos desde época Unix.
 * @param showFollowAction Indica si la tarjeta ofrece la acción visual para seguir al autor.
 */
data class ActivityContent(
    val id: String,
    val type: ActivityType,
    val actorProfileId: String,
    @param:StringRes val actionResId: Int,
    val targetReviewId: Int? = null,
    val targetProfileId: String? = null,
    val time: Long,
    val showFollowAction: Boolean = false
) {
    init {
        when (type) {
            ActivityType.LIKE, ActivityType.COMMENT -> require(targetReviewId != null)
            ActivityType.FOLLOW -> require(targetProfileId != null)
        }
    }
}

/** Distingue el tipo de evento de actividad para elegir su insignia y su color. */
enum class ActivityType { LIKE, COMMENT, FOLLOW }

/** Representa un grupo temporal de eventos de actividad dentro de la interfaz. */
data class ActivityGroupContent(
    val id: String,
    @param:StringRes val titleResId: Int,
    val notifications: List<ActivityContent>
)
