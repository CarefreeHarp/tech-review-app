package com.example.devicersapp.ui.screens.follow_list

import androidx.annotation.StringRes
import com.example.devicersapp.ui.models.FollowListSection
import com.example.devicersapp.ui.models.FollowListUser

/** Conserva el perfil consultado, la sección activa y todos los valores visibles de la lista. */
data class FollowListState(
    val profileId: Int? = null,
    val section: FollowListSection = FollowListSection.FOLLOWERS,
    val users: List<FollowListUser> = emptyList(),
    val loading: Boolean = true,
    @param:StringRes val errorMessageResId: Int? = null
)
