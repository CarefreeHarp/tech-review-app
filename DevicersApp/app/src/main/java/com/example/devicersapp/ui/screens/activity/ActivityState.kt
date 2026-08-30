package com.example.devicersapp.ui.screens.activity

import com.example.devicersapp.ui.models.ActivityGroupContent

/** Representa el estado visible de la pantalla de actividad. */
data class ActivityState(
    val activityGroups: List<ActivityGroupContent> = emptyList(),
    val followedActivityIds: Set<String> = emptySet()
)