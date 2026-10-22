package com.example.devicersapp.ui.screens.activity

import com.example.devicersapp.ui.models.ActivityGroupContent

/** Representa el estado visible de la pantalla de actividad. */
data class ActivityState(
    val currentProfile: com.example.devicersapp.ui.models.UserInfo? = null,
    val activityGroups: List<ActivityGroupContent> = emptyList(),
    val profileImages: Map<Int, String?> = emptyMap(),
    val actorNames: Map<Int, String> = emptyMap(),
    val productNames: Map<Int, String> = emptyMap(),
    val loading: Boolean = false,
    val error: Int? = null,
    val followedUserIds: Set<Int> = emptySet(),
    val followsInProgress: Set<Int> = emptySet(),
    val followError: Int? = null
)
