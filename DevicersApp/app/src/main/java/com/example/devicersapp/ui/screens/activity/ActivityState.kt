package com.example.devicersapp.ui.screens.activity

import com.example.devicersapp.ui.models.ActivityGroupContent

/** Representa el estado visible de la pantalla de actividad. */
data class ActivityState(
    val activityGroups: List<ActivityGroupContent> = emptyList(),
    val followedActivityIds: Set<String> = emptySet(),
    val profileImages: Map<Int, String?> = emptyMap(),
    val actorNames: Map<Int, String> = emptyMap(),
    val productNames: Map<Int, String> = emptyMap(),
    val loading: Boolean = false,
    val error: String? = null
)
