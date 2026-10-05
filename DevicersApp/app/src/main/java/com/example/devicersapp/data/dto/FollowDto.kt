package com.example.devicersapp.data.dto

import com.example.devicersapp.ui.models.FollowInfo

/** Campos de la relación de seguimiento devueltos por `follows`. */
data class FollowDto(val follower_id: Int, val followed_id: Int)

/** Traduce los identificadores del backend al modelo del front. */
fun FollowDto.toFollowInfo() = FollowInfo(follower_id, followed_id)
