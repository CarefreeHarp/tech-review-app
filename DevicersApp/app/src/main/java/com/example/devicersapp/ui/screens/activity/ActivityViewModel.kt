package com.example.devicersapp.ui.screens.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.devicersapp.R
import com.example.devicersapp.data.repository.NotificationRepository
import com.example.devicersapp.data.repository.UserRepository
import com.example.devicersapp.ui.models.ActivityContent
import com.example.devicersapp.ui.models.ActivityGroupContent
import com.example.devicersapp.ui.models.ActivityType
import com.example.devicersapp.ui.models.NotificationActivityInfo
import com.example.devicersapp.ui.models.NotificationTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneId
import javax.inject.Inject

/** Carga las notificaciones del usuario autenticado y las agrupa por día para la pantalla de actividad. */
@HiltViewModel
class ActivityViewModel @Inject constructor(
    private val users: UserRepository,
    private val notifications: NotificationRepository
) : ViewModel() {
    private val state = MutableStateFlow(ActivityState())
    val uiState = state.asStateFlow()
    init { loadActivity() }

    /** Consulta el perfil propio y sus notificaciones con los actores y artículos que muestran las tarjetas. */
    fun loadActivity() {
        state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            try {
                val profile = users.getAuthenticatedUser()
                val activity = notifications.getActivity(profile.id).getOrThrow()
                val contents = activity.mapNotNull { it.toActivityContent() }.sortedByDescending { it.time }
                state.update {
                    it.copy(
                        currentProfile = profile,
                        activityGroups = contents.groupByDay(),
                        profileImages = activity.associate { item -> item.actor.id to item.actor.profileImageUrl },
                        actorNames = activity.associate { item -> item.actor.id to item.actor.username },
                        productNames = activity.mapNotNull { item ->
                            val reviewId = item.notification.reviewId ?: return@mapNotNull null
                            item.productName?.let { name -> reviewId to name }
                        }.toMap(),
                        loading = false
                    )
                }
            } catch (cancelled: CancellationException) {
                throw cancelled
            } catch (error: Exception) {
                state.update { it.copy(loading = false, error = R.string.activity_load_error) }
            }
        }
    }

    /** Traduce el tipo persistido a la tarjeta visible; descarta tipos desconocidos o sin reseña de destino. */
    private fun NotificationActivityInfo.toActivityContent(): ActivityContent? {
        val type = when (notification.type) {
            NotificationTypes.FOLLOW -> ActivityType.FOLLOW
            NotificationTypes.REVIEW_LIKE, NotificationTypes.COMMENT_LIKE -> ActivityType.LIKE
            NotificationTypes.COMMENT -> ActivityType.COMMENT
            else -> return null
        }
        if (type != ActivityType.FOLLOW && notification.reviewId == null) return null
        val action = when (notification.type) {
            NotificationTypes.FOLLOW -> R.string.activity_action_followed
            NotificationTypes.REVIEW_LIKE -> R.string.activity_action_liked_review
            NotificationTypes.COMMENT_LIKE -> R.string.discovery_activity_liked_comment
            else -> R.string.discovery_activity_commented
        }
        return ActivityContent(
            id = notification.id,
            type = type,
            actorProfileId = actor.id.toString(),
            actionResId = action,
            targetReviewId = notification.reviewId,
            targetProfileId = actor.id.toString(),
            time = Instant.parse(notification.createdAt).toEpochMilli(),
            showFollowAction = type == ActivityType.FOLLOW
        )
    }

    /** Agrupa las tarjetas en hoy, ayer y anteriores según los días de calendario del dispositivo. */
    private fun List<ActivityContent>.groupByDay(now: Long = System.currentTimeMillis()): List<ActivityGroupContent> {
        val zone = ZoneId.systemDefault()
        val today = Instant.ofEpochMilli(now).atZone(zone).toLocalDate()
        val groups = groupBy {
            val date = Instant.ofEpochMilli(it.time).atZone(zone).toLocalDate()
            when {
                date >= today -> "today"
                date == today.minusDays(1) -> "yesterday"
                else -> "earlier"
            }
        }
        val titles = linkedMapOf(
            "today" to R.string.activity_group_today,
            "yesterday" to R.string.activity_group_yesterday,
            "earlier" to R.string.activity_group_earlier
        )
        return titles.mapNotNull { (id, title) -> groups[id]?.let { ActivityGroupContent(id, title, it) } }
    }
}
